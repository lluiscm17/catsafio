package net.lluis.catsafio.entity.custom;

import net.lluis.catsafio.network.SapoPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class SapoEntity extends PathfinderMob implements GeoEntity {

    // ── Datos sincronizados cliente/servidor ───────────────────────────
    public static final EntityDataAccessor<Boolean> TONGUE_OUT =
            SynchedEntityData.defineId(SapoEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> MOUTH_OPEN =
            SynchedEntityData.defineId(SapoEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> SWALLOWED_ENTITY_ID =
            SynchedEntityData.defineId(SapoEntity.class, EntityDataSerializers.INT);

    // Constantes
    public  static final int   TONGUE_RANGE_BLOCKS = 10;
    private static final int   TONGUE_GRAB_TICKS   = 120; // 6s antes de tragar
    private static final int   DAMAGE_INTERVAL     = 20;  // 1s
    private static final float DAMAGE_PER_INTERVAL = 4f;
    public  static final int   COOLDOWN_TICKS       = 600; // 30s por jugador

    // Estado servidor
    private UUID grabbedPlayerUUID   = null;
    private UUID swallowedPlayerUUID = null;
    private int  tongueGrabTicks     = 0;
    private int  damageTimer         = 0;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public SapoEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.setMaxUpStep(1.5f);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH,              250.0)
                .add(Attributes.MOVEMENT_SPEED,          0.18)
                .add(Attributes.ARMOR,                   8.0)
                .add(Attributes.KNOCKBACK_RESISTANCE,    0.8)
                .add(Attributes.FOLLOW_RANGE,            24.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SapoTongueGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 14f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TONGUE_OUT,        false);
        this.entityData.define(MOUTH_OPEN,        false);
        this.entityData.define(SWALLOWED_ENTITY_ID, -1);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) return;

        // ── Fase 1: arrastrando con la lengua ──────────────────────────
        if (grabbedPlayerUUID != null) {
            Player grabbed = getPlayerByUUID(grabbedPlayerUUID);
            if (grabbed == null || !grabbed.isAlive()) {
                releaseGrabbed();
                return;
            }

            tongueGrabTicks++;

            // Arrastrar hacia el sapo
            Vec3 dir = this.position().add(0, 1, 0)
                    .subtract(grabbed.position()).normalize().scale(0.35);
            grabbed.setDeltaMovement(dir);
            grabbed.hurtMarked = true;

            // Tras 6s → tragar
            if (tongueGrabTicks >= TONGUE_GRAB_TICKS) {
                swallowPlayer(grabbed);
            }
            return;
        }

        // ── Fase 2: jugador tragado ────────────────────────────────────
        if (swallowedPlayerUUID != null) {
            Player swallowed = getPlayerByUUID(swallowedPlayerUUID);
            if (swallowed == null || !swallowed.isAlive()) {
                releaseSwallowed();
                return;
            }

            // Mantener al jugador dentro del sapo
            swallowed.teleportTo(this.getX(), this.getY() + 0.5, this.getZ());
            swallowed.setDeltaMovement(Vec3.ZERO);
            swallowed.fallDistance = 0;

            // Daño cada segundo
            damageTimer++;
            if (damageTimer >= DAMAGE_INTERVAL) {
                damageTimer = 0;
                swallowed.hurt(this.damageSources().mobAttack(this), DAMAGE_PER_INTERVAL);
            }

            // Sincronizar ID para que el renderer sepa a quién dibujar
            this.entityData.set(SWALLOWED_ENTITY_ID, swallowed.getId());
        }
    }

    // ── API pública ────────────────────────────────────────────────────

    /** ¿Puede el sapo coger a este jugador? (sin cooldown activo para él) */
    public boolean canGrabPlayer(Player player) {
        if (grabbedPlayerUUID != null || swallowedPlayerUUID != null) return false;
        // Cooldown por jugador se gestiona en SapoTongueGoal
        return true;
    }

    public void grabPlayer(Player player) {
        grabbedPlayerUUID = player.getUUID();
        tongueGrabTicks   = 0;
        this.entityData.set(TONGUE_OUT, true);
        this.entityData.set(MOUTH_OPEN, true);
        if (player instanceof ServerPlayer sp) {
            SapoPackets.sendCaptureStart(sp);
        }
    }

    private void swallowPlayer(Player player) {
        grabbedPlayerUUID = null;
        swallowedPlayerUUID = player.getUUID();
        tongueGrabTicks = 0;
        damageTimer     = 0;
        this.entityData.set(TONGUE_OUT, false);
        if (player instanceof ServerPlayer sp) {
            SapoPackets.sendSwallowed(sp, true);
        }
        this.entityData.set(SWALLOWED_ENTITY_ID, player.getId());
    }

    /** Llamado al morir el sapo — libera al jugador tragado */
    @Override
    public void die(DamageSource source) {
        super.die(source);
        releaseSwallowed();
        releaseGrabbed();
    }

    private void releaseGrabbed() {
        if (grabbedPlayerUUID != null) {
            Player p = getPlayerByUUID(grabbedPlayerUUID);
            if (p instanceof ServerPlayer sp) {
                SapoPackets.sendCaptureEnd(sp);
            }
        }
        grabbedPlayerUUID = null;
        tongueGrabTicks   = 0;
        this.entityData.set(TONGUE_OUT, false);
        this.entityData.set(MOUTH_OPEN, false);
    }

    public void releaseSwallowed() {
        if (swallowedPlayerUUID != null) {
            Player p = getPlayerByUUID(swallowedPlayerUUID);
            if (p instanceof ServerPlayer sp) {
                SapoPackets.sendSwallowed(sp, false);
            }
        }
        swallowedPlayerUUID = null;
        damageTimer = 0;
        this.entityData.set(SWALLOWED_ENTITY_ID, -1);
        this.entityData.set(MOUTH_OPEN, false);
    }

    // ── Spawn día Y noche — sin restricción de luz ───────────────────
    public static boolean checkSpawnRules(EntityType<SapoEntity> type,
                                          ServerLevelAccessor level,
                                          MobSpawnType reason,
                                          BlockPos pos,
                                          RandomSource random) {
        if (level.getBiome(pos).is(net.minecraft.tags.BiomeTags.IS_NETHER)) return false;
        if (level.getBiome(pos).is(net.minecraft.tags.BiomeTags.IS_END))    return false;
        // Sin restricción de luz — acepta cualquier nivel (día y noche)
        return level.getBlockState(pos.below()).isSolid()
                && level.getRawBrightness(pos, 0) <= 15;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader level) { return true; }

    // ── Getters ────────────────────────────────────────────────────────
    private Player getPlayerByUUID(UUID uuid) {
        return this.level().getPlayerByUUID(uuid);
    }

    public boolean hasGrabbed()          { return grabbedPlayerUUID   != null; }
    public boolean hasSwallowed()        { return swallowedPlayerUUID != null; }
    public UUID    getGrabbedUUID()      { return grabbedPlayerUUID;           }
    public UUID    getSwallowedUUID()    { return swallowedPlayerUUID;         }

    /** Getter público para el renderer (getEntityData es protected) */
    public int getSwallowedEntityId() {
        return this.entityData.get(SWALLOWED_ENTITY_ID);
    }

    // ── Persistencia ───────────────────────────────────────────────────
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (swallowedPlayerUUID != null) tag.putUUID("SwallowedPlayer", swallowedPlayerUUID);
        if (grabbedPlayerUUID   != null) tag.putUUID("GrabbedPlayer",   grabbedPlayerUUID);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("SwallowedPlayer")) swallowedPlayerUUID = tag.getUUID("SwallowedPlayer");
        if (tag.hasUUID("GrabbedPlayer"))   grabbedPlayerUUID   = tag.getUUID("GrabbedPlayer");
    }

    // ── GeckoLib ────────────────────────────────────────────────────────
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 3, state -> {
            if (this.entityData.get(TONGUE_OUT)) {
                return state.setAndContinue(RawAnimation.begin()
                        .then("entityeufonia.frog_big.attack_1", Animation.LoopType.PLAY_ONCE));
            }
            if (this.entityData.get(MOUTH_OPEN)) {
                return state.setAndContinue(RawAnimation.begin()
                        .then("entityeufonia.frog_big.attack_2", Animation.LoopType.PLAY_ONCE));
            }
            if (state.isMoving()) {
                return state.setAndContinue(RawAnimation.begin()
                        .then("entityeufonia.frog_big.walk", Animation.LoopType.LOOP));
            }
            return state.setAndContinue(RawAnimation.begin()
                    .then("entityeufonia.frog_big.idle", Animation.LoopType.LOOP));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }
}