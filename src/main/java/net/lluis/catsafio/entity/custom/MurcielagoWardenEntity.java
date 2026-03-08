package net.lluis.catsafio.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class MurcielagoWardenEntity extends Monster implements GeoEntity {

    // ── Estados ────────────────────────────────────────────────────────
    public static final EntityDataAccessor<Integer> STATE =
        SynchedEntityData.defineId(MurcielagoWardenEntity.class, EntityDataSerializers.INT);

    public static final int STATE_DORMIDO   = 0;
    public static final int STATE_PATRULLA  = 1;
    public static final int STATE_PERSIGUE  = 2;

    // Rango de detección
    private static final double DETECT_RANGE   = 16.0;
    private static final double ATTACK_RANGE   = 1.8; // radio de aura
    private static final int    ATTACK_COOLDOWN = 20; // 1s entre golpes

    private int attackTimer  = 0;
    private int patrolTimer  = 0;
    private Vec3 patrolDir   = Vec3.ZERO;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public MurcielagoWardenEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
        this.moveControl = new MurcielagoMoveControl(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH,          60.0)
            .add(Attributes.MOVEMENT_SPEED,       0.38)
            .add(Attributes.FOLLOW_RANGE,         20.0)
            .add(Attributes.ATTACK_DAMAGE,         6.0)
            .add(Attributes.ARMOR,                 4.0)
            .add(Attributes.KNOCKBACK_RESISTANCE,  0.5);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STATE, STATE_PATRULLA);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MurcielagoAuraAttackGoal(this));
        this.goalSelector.addGoal(2, new MurcielagoPursueGoal(this));
        this.goalSelector.addGoal(3, new MurcielagoPatrolGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 12f));

        // Activación por daño: HurtByTargetGoal activa el target
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(
            this, Player.class, true,
            e -> e instanceof Player p && !p.isCreative() && !p.isSpectator()));
    }

    // ── Activación por daño ───────────────────────────────────────────
    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);
        if (result && getState() == STATE_DORMIDO) {
            setState(STATE_PERSIGUE);
        }
        return result;
    }

    @Override
    public void tick() {
        super.tick();

        // Gravedad cero — mantenerse en el aire
        this.setDeltaMovement(this.getDeltaMovement().multiply(1, 0.8, 1));

        if (this.level().isClientSide) return;
        if (attackTimer > 0) attackTimer--;

        LivingEntity target = this.getTarget();

        // ── Gestión de estados ─────────────────────────────────────────
        if (target != null && target.isAlive()
                && this.distanceToSqr(target) <= DETECT_RANGE * DETECT_RANGE) {
            setState(STATE_PERSIGUE);
        } else if (getState() == STATE_PERSIGUE) {
            this.setTarget(null);
            setState(STATE_PATRULLA);
        }
    }

    // ── Aura de daño por contacto ─────────────────────────────────────
    public void tryAuraAttack() {
        if (attackTimer > 0) return;
        for (Player player : this.level().getEntitiesOfClass(
                Player.class, this.getBoundingBox().inflate(ATTACK_RANGE),
                p -> !p.isCreative() && !p.isSpectator())) {
            player.hurt(this.damageSources().mobAttack(this), 6.0f);
            attackTimer = ATTACK_COOLDOWN;
            this.swing(net.minecraft.world.InteractionHand.MAIN_HAND);
            break;
        }
    }

    // ── Patrullaje: cambiar dirección al chocar ───────────────────────
    public void updatePatrol() {
        patrolTimer--;
        if (patrolTimer <= 0 || isCollidingWithWall()) {
            // Nueva dirección aleatoria horizontal + pequeña componente vertical
            float yaw = this.random.nextFloat() * 360f;
            double dx = Math.cos(Math.toRadians(yaw));
            double dy = (this.random.nextFloat() - 0.5) * 0.4;
            double dz = Math.sin(Math.toRadians(yaw));
            patrolDir  = new Vec3(dx, dy, dz).normalize().scale(0.25);
            patrolTimer = 40 + this.random.nextInt(40); // 2-4s
        }
        this.setDeltaMovement(patrolDir);
    }

    private boolean isCollidingWithWall() {
        Vec3 mv = this.getDeltaMovement();
        return this.horizontalCollision
            || (Math.abs(mv.x) < 0.01 && Math.abs(mv.z) < 0.01 && patrolTimer < 30);
    }

    // ── Getters/Setters estado ────────────────────────────────────────
    public int  getState()           { return this.entityData.get(STATE); }
    public void setState(int state)  { this.entityData.set(STATE, state); }

    // ── Spawn solo en Deep Dark ───────────────────────────────────────
    public static boolean checkSpawnRules(EntityType<MurcielagoWardenEntity> type,
                                          ServerLevelAccessor level,
                                          MobSpawnType reason,
                                          BlockPos pos, RandomSource random) {
        return level.getBiome(pos).is(Biomes.DEEP_DARK)
            && level.getRawBrightness(pos, 0) == 0
            && pos.getY() < 0;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader level) { return true; }

    // ── NBT ───────────────────────────────────────────────────────────
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("MurcielagoState", getState());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("MurcielagoState"))
            setState(tag.getInt("MurcielagoState"));
    }

    // ── GeckoLib ──────────────────────────────────────────────────────
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 3, state -> {
            if (getState() == STATE_PERSIGUE || state.isMoving()) {
                return state.setAndContinue(RawAnimation.begin()
                    .then("walk", Animation.LoopType.LOOP));
            }
            return state.setAndContinue(RawAnimation.begin()
                .then("idle", Animation.LoopType.LOOP));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }
}
