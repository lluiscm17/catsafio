package net.lluis.catsafio.entity.custom;

import net.lluis.catsafio.entity.ai.CreeperCakeAttackGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CreeperCakeEntity extends PathfinderMob implements GeoEntity {

    // ── Sincronización ──────────────────────────────────────────────────
    private static final EntityDataAccessor<Integer> FUSE_TICKS =
            SynchedEntityData.defineId(CreeperCakeEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_IGNITED =
            SynchedEntityData.defineId(CreeperCakeEntity.class, EntityDataSerializers.BOOLEAN);

    // Sin cooldown: detecta al jugador y explota inmediatamente
    private static final int EXPLOSION_FUSE = 20;   // 1 segundo de mecha
    private static final int EXPLOSION_RADIUS = 3;
    private int fuseTicks = 0;
    private boolean hasExploded = false;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public CreeperCakeEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.28)
                .add(Attributes.FOLLOW_RANGE, 16.0)
                .add(Attributes.ARMOR, 0.0);
    }

    @Override
    protected void registerGoals() {
        // Sin cooldown de explosión: ataca directamente
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new CreeperCakeAttackGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FUSE_TICKS, 0);
        this.entityData.define(IS_IGNITED, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            // Si está en modo explosión (sin cooldown)
            if (this.entityData.get(IS_IGNITED)) {
                fuseTicks++;
                if (fuseTicks >= EXPLOSION_FUSE && !hasExploded) {
                    explode();
                }
            }
        }
    }

    public void startFuse() {
        if (!this.entityData.get(IS_IGNITED)) {
            this.entityData.set(IS_IGNITED, true);
            this.playSound(SoundEvents.CREEPER_PRIMED, 1.0f, 0.5f);
            fuseTicks = 0;
        }
    }

    private void explode() {
        if (hasExploded) return;
        hasExploded = true;

        // Notificar a todos los jugadores en rango para mostrar la pantalla del pastel
        AABB area = this.getBoundingBox().inflate(EXPLOSION_RADIUS + 2);
        this.level().getEntitiesOfClass(Player.class, area).forEach(player -> {
            if (player instanceof net.minecraft.server.level.ServerPlayer sp) {
                net.lluis.catsafio.network.CreeperCakePacket.send(sp);
            }
        });

        // Explosión real
        this.level().explode(this, this.getX(), this.getY(), this.getZ(),
                EXPLOSION_RADIUS, Level.ExplosionInteraction.MOB);
        this.discard();
    }

    // ── Spawn día Y noche — sin restricción de luz ───────────────────
    public static boolean checkSpawnRules(EntityType<CreeperCakeEntity> type,
                                          ServerLevelAccessor level,
                                          MobSpawnType reason,
                                          BlockPos pos,
                                          RandomSource random) {
        if (level.getBiome(pos).is(net.minecraft.tags.BiomeTags.IS_NETHER)) return false;
        if (level.getBiome(pos).is(net.minecraft.tags.BiomeTags.IS_END))    return false;
        // Solo bloque sólido debajo — sin comprobar nivel de luz
        return level.getBlockState(pos.below()).isSolid()
                && level.getRawBrightness(pos, 0) <= 15; // acepta cualquier nivel de luz
    }

    @Override
    public boolean checkSpawnObstruction(net.minecraft.world.level.LevelReader level) {
        return true;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("FuseTicks", fuseTicks);
        tag.putBoolean("IsIgnited", this.entityData.get(IS_IGNITED));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        fuseTicks = tag.getInt("FuseTicks");
        this.entityData.set(IS_IGNITED, tag.getBoolean("IsIgnited"));
    }

    // ── GeckoLib ────────────────────────────────────────────────────────
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 2, state -> {
            if (this.entityData.get(IS_IGNITED)) {
                return state.setAndContinue(RawAnimation.begin().then("animation.creepercake.explode", Animation.LoopType.PLAY_ONCE));
            }
            if (state.isMoving()) {
                return state.setAndContinue(RawAnimation.begin().then("animation.creepercake.walk", Animation.LoopType.LOOP));
            }
            return state.setAndContinue(RawAnimation.begin().then("animation.creepercake.idle", Animation.LoopType.LOOP));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }
}