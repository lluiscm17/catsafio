package net.lluis.catsafio.entity.custom;

import net.lluis.catsafio.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class CreeperWardenEntity extends Monster implements GeoEntity {

    private static final EntityDataAccessor<Integer> SWELL_DIR = SynchedEntityData.defineId(CreeperWardenEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_POWERED = SynchedEntityData.defineId(CreeperWardenEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_IGNITED = SynchedEntityData.defineId(CreeperWardenEntity.class, EntityDataSerializers.BOOLEAN);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private int oldSwell;
    private int swell;
    private int maxSwell = 30; // 1.5 segundos para explotar
    private int explosionRadius = 5;

    // Sistema de detección por vibraciones (como el Warden)
    private int vibrationCooldown = 0;
    private LivingEntity vibrationTarget;

    public CreeperWardenEntity(EntityType<? extends CreeperWardenEntity> type, Level level) {
        super(type, level);
        this.xpReward = 15;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new CreeperWardenSwellGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, net.minecraft.world.entity.animal.Cat.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, net.minecraft.world.entity.animal.Ocelot.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SWELL_DIR, -1);
        this.entityData.define(IS_POWERED, false);
        this.entityData.define(IS_IGNITED, false);
    }

    @Override
    public void tick() {
        if (this.isAlive()) {
            this.oldSwell = this.swell;

            if (this.isIgnited()) {
                this.setSwellDir(1);
            }

            int swellDir = this.getSwellDir();
            if (swellDir > 0 && this.swell == 0) {
                this.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
                this.gameEvent(GameEvent.PRIME_FUSE);
            }

            this.swell += swellDir;
            if (this.swell < 0) {
                this.swell = 0;
            }

            if (this.swell >= this.maxSwell) {
                this.swell = this.maxSwell;
                this.explodeCreeper();
            }

            // Sistema de detección por vibraciones
            if (!this.level().isClientSide) {
                detectVibrations();
            }
        }

        super.tick();
    }

    /**
     * Detección por vibraciones similar al Warden
     */
    private void detectVibrations() {
        if (vibrationCooldown > 0) {
            vibrationCooldown--;
            return;
        }

        // Detectar jugadores que se mueven cerca - CORREGIDO: getEntitiesOfClass
        AABB searchBox = this.getBoundingBox().inflate(20.0D);
        List<Player> nearbyPlayers = this.level().getEntitiesOfClass(Player.class, searchBox, EntitySelector.NO_SPECTATORS);

        for (Player player : nearbyPlayers) {
            // Si el jugador está agachado, es más difícil de detectar
            double detectionRange = player.isCrouching() ? 5.0D : 15.0D;

            if (this.distanceToSqr(player) < detectionRange * detectionRange) {
                // Detectar si el jugador se está moviendo
                if (player.getDeltaMovement().lengthSqr() > 0.001) {
                    this.vibrationTarget = player;
                    this.setTarget(player);
                    vibrationCooldown = 20; // 1 segundo de cooldown

                    // Sonido de detección
                    this.playSound(SoundEvents.WARDEN_LISTENING, 1.0F, 1.0F);
                    break;
                }
            }
        }
    }

    private void explodeCreeper() {
        if (!this.level().isClientSide) {
            float power = this.isPowered() ? 2.0F : 1.0F;
            this.dead = true;
            this.level().explode(this, this.getX(), this.getY(), this.getZ(),
                    (float)this.explosionRadius * power, Level.ExplosionInteraction.MOB);
            this.discard();
        }
    }

    public float getSwelling(float partialTicks) {
        return net.minecraft.util.Mth.lerp(partialTicks, (float)this.oldSwell, (float)this.swell) / (float)(this.maxSwell - 2);
    }

    public int getSwellDir() {
        return this.entityData.get(SWELL_DIR);
    }

    public void setSwellDir(int direction) {
        this.entityData.set(SWELL_DIR, direction);
    }

    public boolean isPowered() {
        return this.entityData.get(IS_POWERED);
    }

    public void setPowered(boolean powered) {
        this.entityData.set(IS_POWERED, powered);
    }

    public boolean isIgnited() {
        return this.entityData.get(IS_IGNITED);
    }

    public void ignite() {
        this.entityData.set(IS_IGNITED, true);
    }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightning) {
        super.thunderHit(level, lightning);
        this.setPowered(true);
    }

    @Override
    protected void actuallyHurt(DamageSource source, float amount) {
        super.actuallyHurt(source, amount);

        // Al recibir daño, detecta al atacante por vibración
        if (source.getEntity() instanceof LivingEntity attacker) {
            this.vibrationTarget = attacker;
            this.setTarget(attacker);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putShort("Fuse", (short)this.maxSwell);
        tag.putByte("ExplosionRadius", (byte)this.explosionRadius);
        tag.putBoolean("ignited", this.isIgnited());
        tag.putBoolean("powered", this.isPowered());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Fuse", 99)) {
            this.maxSwell = tag.getShort("Fuse");
        }
        if (tag.contains("ExplosionRadius", 99)) {
            this.explosionRadius = tag.getByte("ExplosionRadius");
        }
        if (tag.getBoolean("ignited")) {
            this.ignite();
        }
        this.setPowered(tag.getBoolean("powered"));
    }

    // GeckoLib
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<CreeperWardenEntity> state) {
        // Animación de explosión
        if (this.getSwellDir() > 0) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("animation.creeper_warden.explode"));
            return PlayState.CONTINUE;
        }

        // Animación de caminar
        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.creeper_warden.walk"));
            return PlayState.CONTINUE;
        }

        // Animación idle
        state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.creeper_warden.idle"));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    // Goal personalizado para la explosión
    static class CreeperWardenSwellGoal extends Goal {
        private final CreeperWardenEntity creeper;
        private LivingEntity target;

        public CreeperWardenSwellGoal(CreeperWardenEntity creeper) {
            this.creeper = creeper;
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.creeper.getTarget();
            return this.creeper.getSwellDir() > 0 || target != null && this.creeper.distanceToSqr(target) < 9.0D;
        }

        @Override
        public void start() {
            this.creeper.getNavigation().stop();
            this.target = this.creeper.getTarget();
        }

        @Override
        public void stop() {
            this.target = null;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (this.target == null) {
                this.creeper.setSwellDir(-1);
            } else if (this.creeper.distanceToSqr(this.target) > 49.0D) {
                this.creeper.setSwellDir(-1);
            } else if (!this.creeper.getSensing().hasLineOfSight(this.target)) {
                this.creeper.setSwellDir(-1);
            } else {
                this.creeper.setSwellDir(1);
            }
        }
    }
}