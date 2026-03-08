package net.lluis.catsafio.entity.custom;

import net.lluis.catsafio.network.GorgonaPetrificationPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GorgonaEntity extends Monster implements GeoEntity {

    private static final EntityDataAccessor<Boolean> IS_STARING = 
        SynchedEntityData.defineId(GorgonaEntity.class, EntityDataSerializers.BOOLEAN);
    
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    
    // Sistema de petrificación
    private static final double STARE_RANGE = 7.0; // 7 bloques de rango
    private static final float STARE_FOV = 70.0f; // Campo de visión (grados)
    private static final int STARE_DURATION = 100; // 5 segundos (100 ticks)
    
    // Trackear tiempo de mirada por jugador
    private final Map<UUID, Integer> stareTimers = new HashMap<>();

    public GorgonaEntity(EntityType<? extends GorgonaEntity> type, Level level) {
        super(type, level);
        this.xpReward = 20;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 100.0D) // 50 corazones
            .add(Attributes.MOVEMENT_SPEED, 0.25D) // Velocidad de creeper
            .add(Attributes.ATTACK_DAMAGE, 5.0D) // 2.5 corazones
            .add(Attributes.FOLLOW_RANGE, 32.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.4D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_STARING, false);
    }

    @Override
    public void tick() {
        super.tick();
        
        if (!this.level().isClientSide) {
            checkPetrificationStare();
        }
    }

    /**
     * Sistema de petrificación por mirada
     */
    private void checkPetrificationStare() {
        LivingEntity target = this.getTarget();
        
        if (target instanceof ServerPlayer player) {
            UUID playerId = player.getUUID();
            
            // Verificar si el jugador ya está petrificado
            if (GorgonaPetrificationPacket.isPetrified(playerId)) {
                // Si ya está petrificado, solo atacar cuerpo a cuerpo
                stareTimers.remove(playerId);
                setStaring(false);
                return;
            }
            
            // Verificar distancia
            double distance = this.distanceTo(player);
            if (distance > STARE_RANGE) {
                // Fuera de rango - resetear timer
                stareTimers.remove(playerId);
                setStaring(false);
                return;
            }
            
            // Verificar si está mirando al jugador
            if (isLookingAt(player)) {
                setStaring(true);
                
                // Incrementar timer
                int currentTime = stareTimers.getOrDefault(playerId, 0);
                currentTime++;
                stareTimers.put(playerId, currentTime);
                
                // Sonido de advertencia cada segundo
                if (currentTime % 20 == 0 && currentTime < STARE_DURATION) {
                    player.playSound(SoundEvents.SCULK_CLICKING, 1.0f, 1.0f + (currentTime / 100.0f));
                }
                
                // Petrificar después de 5 segundos
                if (currentTime >= STARE_DURATION) {
                    petrifyPlayer(player);
                    stareTimers.remove(playerId);
                }
            } else {
                // No está mirando - decrementar timer gradualmente
                int currentTime = stareTimers.getOrDefault(playerId, 0);
                if (currentTime > 0) {
                    currentTime = Math.max(0, currentTime - 2);
                    if (currentTime > 0) {
                        stareTimers.put(playerId, currentTime);
                    } else {
                        stareTimers.remove(playerId);
                    }
                }
                setStaring(false);
            }
        } else {
            setStaring(false);
        }
    }

    /**
     * Verifica si la Gorgona está mirando directamente al objetivo
     */
    private boolean isLookingAt(LivingEntity target) {
        Vec3 lookVec = this.getViewVector(1.0F);
        Vec3 toTarget = new Vec3(
            target.getX() - this.getX(),
            target.getEyeY() - this.getEyeY(),
            target.getZ() - this.getZ()
        ).normalize();
        
        double dotProduct = lookVec.dot(toTarget);
        double angleInDegrees = Math.toDegrees(Math.acos(Math.max(-1.0, Math.min(1.0, dotProduct))));
        
        return angleInDegrees < (STARE_FOV / 2.0);
    }

    /**
     * Petrifica al jugador
     */
    private void petrifyPlayer(ServerPlayer player) {
        GorgonaPetrificationPacket packet = new GorgonaPetrificationPacket(true);
        GorgonaPetrificationPacket.CHANNEL.send(
            PacketDistributor.PLAYER.with(() -> player), 
            packet
        );
        
        player.playSound(SoundEvents.WARDEN_SONIC_BOOM, 1.0f, 0.5f);
        GorgonaPetrificationPacket.setPetrified(player.getUUID(), true);
    }

    public boolean isStaring() {
        return this.entityData.get(IS_STARING);
    }

    public void setStaring(boolean staring) {
        this.entityData.set(IS_STARING, staring);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsStaring", this.isStaring());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setStaring(tag.getBoolean("IsStaring"));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<GorgonaEntity> state) {
        if (this.isAggressive() && this.swinging) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("attack"));
            return PlayState.CONTINUE;
        }
        
        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("walk"));
            return PlayState.CONTINUE;
        }
        
        state.getController().setAnimation(RawAnimation.begin().thenLoop("idle"));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
