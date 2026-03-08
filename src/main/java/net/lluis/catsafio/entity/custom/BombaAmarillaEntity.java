package net.lluis.catsafio.entity.custom;

import net.lluis.catsafio.network.BombaAmarillaAttachPacket;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class BombaAmarillaEntity extends PathfinderMob implements GeoEntity {

    private static final EntityDataAccessor<Boolean> IS_ATTACHED =
            SynchedEntityData.defineId(BombaAmarillaEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ATTACHED_PLAYER_ID =
            SynchedEntityData.defineId(BombaAmarillaEntity.class, EntityDataSerializers.INT);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final double DETECTION_RANGE = 32.0; // 32 bloques
    private static final int EXPLOSION_TIME = 300; // 15 segundos (300 ticks)
    private static final float EXPLOSION_POWER = 6.0f; // Muy potente

    private int attachedTimer = 0;
    private UUID attachedPlayerUUID = null;

    public BombaAmarillaEntity(EntityType<? extends BombaAmarillaEntity> type, Level level) {
        super(type, level);
        this.xpReward = 15;
        this.setNoGravity(true); // Vuela
        this.moveControl = new net.minecraft.world.entity.ai.control.FlyingMoveControl(this, 20, true);
        this.navigation = new net.minecraft.world.entity.ai.navigation.FlyingPathNavigation(this, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.FLYING_SPEED, 0.6D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.FOLLOW_RANGE, DETECTION_RANGE);
    }

    @Override
    protected net.minecraft.world.entity.ai.navigation.PathNavigation createNavigation(Level level) {
        net.minecraft.world.entity.ai.navigation.FlyingPathNavigation navigation =
                new net.minecraft.world.entity.ai.navigation.FlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, net.minecraft.world.level.block.state.BlockState state, BlockPos pos) {
        // No verificar daño de caída - es voladora
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false; // No recibe daño de caída
    }

    @Override
    public boolean onClimbable() {
        return false; // No trepa
    }

    @Override
    public void travel(net.minecraft.world.phys.Vec3 travelVector) {
        if (this.isAttached()) {
            // Cuando está enganchada, no moverse
            super.travel(net.minecraft.world.phys.Vec3.ZERO);
            return;
        }

        if (this.isEffectiveAi() || this.isControlledByLocalInstance()) {
            // Usar movimiento normal de entidad que respeta colisiones
            if (this.isInWater()) {
                this.moveRelative(0.02F, travelVector);
                this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.8D));
            } else if (this.isInLava()) {
                this.moveRelative(0.02F, travelVector);
                this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
            } else {
                // Vuelo en aire - usa FLYING_SPEED
                float friction = 0.91F;

                this.moveRelative(this.getSpeed(), travelVector);
                this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(friction));
            }
        } else {
            super.travel(travelVector);
        }

        this.calculateEntityAnimation(false);
    }

    @Override
    public float getSpeed() {
        return (float)this.getAttributeValue(Attributes.FLYING_SPEED);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new BombaSweepAttackGoal(this));
        this.goalSelector.addGoal(2, new net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 32.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    // ════════════════════════════════════════════════════════════
    // ATAQUE DE BARRIDO - Se lanza hacia el jugador
    // ════════════════════════════════════════════════════════════

    static class BombaSweepAttackGoal extends Goal {
        private final BombaAmarillaEntity bomba;
        private int cooldown;

        public BombaSweepAttackGoal(BombaAmarillaEntity bomba) {
            this.bomba = bomba;
            this.setFlags(java.util.EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (bomba.isAttached()) return false;
            if (cooldown > 0) {
                cooldown--;
                return false;
            }

            LivingEntity target = bomba.getTarget();
            if (target == null) return false;

            double distance = bomba.distanceTo(target);
            return distance < 16.0 && distance > 3.0;
        }

        @Override
        public void start() {
            cooldown = 100; // 5 segundos entre ataques
        }

        @Override
        public void tick() {
            LivingEntity target = bomba.getTarget();
            if (target == null) return;

            // Usar el MoveControl para moverse (respeta pathfinding)
            bomba.getMoveControl().setWantedPosition(
                    target.getX(),
                    target.getY(),
                    target.getZ(),
                    1.5D // Velocidad de ataque
            );
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_ATTACHED, false);
        this.entityData.define(ATTACHED_PLAYER_ID, -1);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            // Si está enganchada a un jugador
            if (isAttached()) {
                handleAttachedBehavior();
            } else {
                // Comportamiento de vuelo normal
                handleFlyingBehavior();
            }
        }
    }

    private void handleFlyingBehavior() {
        // Intentar engancharse al jugador más cercano
        LivingEntity target = this.getTarget();

        if (target instanceof ServerPlayer player) {
            double distance = this.distanceTo(player);

            // Si está lo suficientemente cerca, engancharse
            if (distance < 1.5) {
                attachToPlayer(player);
            }
        }
    }

    private void attachToPlayer(ServerPlayer player) {
        this.setAttached(true);
        this.attachedPlayerUUID = player.getUUID();
        this.entityData.set(ATTACHED_PLAYER_ID, player.getId());
        this.attachedTimer = 0;

        // Enviar packet al cliente para iniciar UI
        BombaAmarillaAttachPacket packet = new BombaAmarillaAttachPacket(true, EXPLOSION_TIME);
        BombaAmarillaAttachPacket.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                packet
        );

        // Sonido de enganche
        player.playSound(SoundEvents.CREEPER_PRIMED, 1.0f, 1.5f);

        // Marcar como enganchada
        BombaAmarillaAttachPacket.setAttached(player.getUUID(), true);

        // Hacer invisible y sin colisión
        this.setInvisible(true);
        this.setNoGravity(true);
    }

    private void handleAttachedBehavior() {
        attachedTimer++;

        // Buscar jugador enganchado
        Player player = this.level().getPlayerByUUID(attachedPlayerUUID);

        if (player == null || !player.isAlive()) {
            // Jugador desconectado o muerto - explotar
            explode();
            return;
        }

        // Verificar si el jugador se liberó
        if (!BombaAmarillaAttachPacket.isAttached(attachedPlayerUUID)) {
            // El jugador pulsó espacio suficientes veces - desaparecer sin explotar
            this.playSound(SoundEvents.ITEM_BREAK, 1.0f, 1.0f);
            this.discard();
            return;
        }

        // Seguir al jugador (posición encima de la cabeza)
        this.setPos(player.getX(), player.getY() + player.getBbHeight() + 0.5, player.getZ());

        // Sonido de tick cada segundo
        if (attachedTimer % 20 == 0) {
            float pitch = 0.8f + (attachedTimer / (float)EXPLOSION_TIME) * 0.7f;
            player.playSound(SoundEvents.CREEPER_PRIMED, 0.5f, pitch);
        }

        // Explotar después de 15 segundos
        if (attachedTimer >= EXPLOSION_TIME) {
            explode();
        }
    }

    private void explode() {
        if (this.level().isClientSide) return;

        // Notificar al jugador que falló
        if (attachedPlayerUUID != null) {
            Player player = this.level().getPlayerByUUID(attachedPlayerUUID);
            if (player instanceof ServerPlayer serverPlayer) {
                BombaAmarillaAttachPacket packet = new BombaAmarillaAttachPacket(false, 0);
                BombaAmarillaAttachPacket.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> serverPlayer),
                        packet
                );
                BombaAmarillaAttachPacket.setAttached(attachedPlayerUUID, false);
            }
        }

        BlockPos pos = this.blockPosition();

        // Si está en agua, eliminar agua en radio de explosión
        if (this.isInWater()) {
            removeWaterInRadius(pos, 6);
        }

        // Crear explosión muy potente
        this.level().explode(
                this,
                this.getX(),
                this.getY(),
                this.getZ(),
                EXPLOSION_POWER,
                Level.ExplosionInteraction.MOB
        );

        // Sonido de explosión
        this.playSound(SoundEvents.GENERIC_EXPLODE, 4.0f, 0.8f);

        // Eliminar entidad
        this.discard();
    }

    private void removeWaterInRadius(BlockPos center, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x*x + y*y + z*z <= radius*radius) {
                        BlockPos pos = center.offset(x, y, z);
                        FluidState fluidState = this.level().getFluidState(pos);

                        if (fluidState.getType() == Fluids.WATER || fluidState.getType() == Fluids.FLOWING_WATER) {
                            this.level().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }
    }

    public boolean isAttached() {
        return this.entityData.get(IS_ATTACHED);
    }

    public void setAttached(boolean attached) {
        this.entityData.set(IS_ATTACHED, attached);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsAttached", this.isAttached());
        tag.putInt("AttachedTimer", this.attachedTimer);
        if (attachedPlayerUUID != null) {
            tag.putUUID("AttachedPlayerUUID", attachedPlayerUUID);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setAttached(tag.getBoolean("IsAttached"));
        this.attachedTimer = tag.getInt("AttachedTimer");
        if (tag.hasUUID("AttachedPlayerUUID")) {
            this.attachedPlayerUUID = tag.getUUID("AttachedPlayerUUID");
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<BombaAmarillaEntity> state) {
        if (isAttached()) {
            // Sin animación cuando está enganchada (invisible)
            return PlayState.STOP;
        }

        // Animación de vuelo cuando está en el aire o moviendose
        if (state.isMoving() || !this.onGround()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("fly"));
        } else {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("idle"));
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    
}