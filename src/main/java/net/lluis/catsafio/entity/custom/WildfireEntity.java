package net.lluis.catsafio.entity.custom;

import net.lluis.catsafio.Catsafio;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class WildfireEntity extends Monster implements GeoEntity {

    // ── Datos sincronizados ────────────────────────────────────────────
    // 0 = 4 escudos (full), 1 = 3, 2 = 2, 3 = 1, 4 = sin escudos
    public static final EntityDataAccessor<Integer> SHIELD_COUNT =
            SynchedEntityData.defineId(WildfireEntity.class, EntityDataSerializers.INT);

    // Umbrales de vida para quitar escudos
    private static final float HP_SHIELD_3 = 112f; // pierde escudo 4 → 3
    private static final float HP_SHIELD_2 = 75f;  // pierde escudo 3 → 2
    private static final float HP_SHIELD_1 = 37f;  // pierde escudo 2 → 1
    // a 0 HP muere (último escudo se pierde al morir)

    private static final int FIREBALL_COOLDOWN = 40; // 2s entre ataques
    private int fireballTimer = 0;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public WildfireEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.setNoGravity(true); // flota como un blaze
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH,           150.0)
                .add(Attributes.MOVEMENT_SPEED,        0.23)
                .add(Attributes.FOLLOW_RANGE,          48.0)
                .add(Attributes.ARMOR,                  6.0)
                .add(Attributes.KNOCKBACK_RESISTANCE,   0.5);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new WildfireFireballGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 12f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHIELD_COUNT, 0); // empieza con 4 escudos (índice 0)
    }

    @Override
    public void tick() {
        super.tick();

        // Vuelo activo: mantenerse a ~4 bloques sobre el suelo
        if (!this.level().isClientSide) {
            LivingEntity target = this.getTarget();
            double targetY = this.getY();
            if (target != null) {
                // Volar a la altura del jugador +2 bloques
                targetY = target.getY() + 2.0;
            } else {
                // Sin target: flotar a 4 bloques sobre el suelo
                for (int i = 0; i < 5; i++) {
                    if (!this.level().getBlockState(this.blockPosition().below(i)).isAir()) {
                        targetY = this.getY();
                        break;
                    }
                }
                targetY = this.getY() + 0.5;
            }
            double dy = targetY - this.getY();
            double yVel = Math.signum(dy) * Math.min(Math.abs(dy) * 0.1, 0.3);
            this.setDeltaMovement(this.getDeltaMovement().x, yVel, this.getDeltaMovement().z);
        }

        if (this.level().isClientSide) return;

        // Actualizar escudos según vida actual
        float hp = this.getHealth();
        int currentShields = this.entityData.get(SHIELD_COUNT);
        int newShields = currentShields;

        if (hp <= HP_SHIELD_1 && currentShields < 3)      newShields = 3; // 1 escudo
        else if (hp <= HP_SHIELD_2 && currentShields < 2) newShields = 2; // 2 escudos
        else if (hp <= HP_SHIELD_3 && currentShields < 1) newShields = 1; // 3 escudos

        if (newShields != currentShields) {
            this.entityData.set(SHIELD_COUNT, newShields);
            // Efecto visual al perder escudo
            this.level().broadcastEntityEvent(this, (byte) 4);
            this.playSound(SoundEvents.SHIELD_BREAK, 1.5f, 0.8f);
        }
    }

    // ── Resistencia al fuego — IGNORA fire resistance del jugador ─────
    public void shootFireball(LivingEntity target) {
        if (this.level().isClientSide) return;

        // Puntería: apuntar al centro del cuerpo del objetivo
        double tx = target.getX() - this.getX();
        double ty = target.getY() + target.getBbHeight() * 0.5 - this.getEyeY();
        double tz = target.getZ() - this.getZ();

        // Normalizar con pequeña corrección vertical
        double dist = Math.sqrt(tx * tx + ty * ty + tz * tz);
        tx /= dist;
        ty /= dist;
        tz /= dist;

        WildfireFireball fireball = new WildfireFireball(this.level(), this, tx, ty, tz);
        fireball.setPos(this.getX(), this.getEyeY(), this.getZ());
        this.level().addFreshEntity(fireball);
        this.playSound(SoundEvents.BLAZE_SHOOT, 1.0f, 1.0f);
    }

    // ── Daño de fuego que ignora fire resistance ───────────────────────
    public void applyFireDamageIgnoringResistance(LivingEntity target, float damage) {
        // Quitar temporalmente fire resistance, aplicar daño, restaurar
        MobEffectInstance fireRes = target.getEffect(MobEffects.FIRE_RESISTANCE);
        if (fireRes != null) {
            target.removeEffect(MobEffects.FIRE_RESISTANCE);
            target.hurt(this.damageSources().inFire(), damage);
            // Restaurar con el tiempo restante
            target.addEffect(new MobEffectInstance(
                    MobEffects.FIRE_RESISTANCE,
                    fireRes.getDuration(),
                    fireRes.getAmplifier(),
                    fireRes.isAmbient(),
                    fireRes.isVisible()
            ));
        } else {
            target.hurt(this.damageSources().inFire(), damage);
        }
        target.setSecondsOnFire(5);
    }

    // ── Getter público para el renderer ───────────────────────────────
    public int getShieldCount() {
        return this.entityData.get(SHIELD_COUNT);
    }

    // ── Spawn solo en Nether ───────────────────────────────────────────
    public static boolean checkSpawnRules(EntityType<WildfireEntity> type,
                                          ServerLevelAccessor level,
                                          MobSpawnType reason,
                                          BlockPos pos,
                                          RandomSource random) {
        return level.getBiome(pos).is(net.minecraft.tags.BiomeTags.IS_NETHER)
                && level.getBlockState(pos.below()).isSolid();
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader level) { return true; }

    // ── Persistencia ───────────────────────────────────────────────────
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("ShieldCount", this.entityData.get(SHIELD_COUNT));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(SHIELD_COUNT, tag.getInt("ShieldCount"));
    }

    // ── GeckoLib ───────────────────────────────────────────────────────
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 3, state -> {
            if (state.isMoving()) {
                return state.setAndContinue(RawAnimation.begin()
                        .then("animation.wildfire.walk", Animation.LoopType.LOOP));
            }
            return state.setAndContinue(RawAnimation.begin()
                    .then("animation.wildfire.idle", Animation.LoopType.LOOP));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }
}