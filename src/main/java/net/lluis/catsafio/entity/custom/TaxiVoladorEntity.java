package net.lluis.catsafio.entity.custom;

import net.lluis.catsafio.entity.ModEntities;
import net.lluis.catsafio.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class TaxiVoladorEntity extends Entity implements GeoEntity {

    private static final EntityDataAccessor<Boolean> HAS_PASSENGER =
            SynchedEntityData.defineId(TaxiVoladorEntity.class, EntityDataSerializers.BOOLEAN);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final float FLIGHT_SPEED = 0.6f; // Velocidad horizontal (ajustada)
    private static final float VERTICAL_SPEED = 0.4f; // Velocidad subida (ajustada)
    private static final float HOVER_DECAY = 0.98f; // Mantiene altura más tiempo

    private double verticalMomentum = 0;

    public TaxiVoladorEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
        this.blocksBuilding = true;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(HAS_PASSENGER, false);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            // Lado del servidor
            Entity controller = this.getControllingPassenger();

            if (controller instanceof Player player) {
                // Controlar el taxi - rotar con el jugador
                this.setYRot(player.getYRot());
                this.yRotO = this.getYRot();
                this.setXRot(player.getXRot() * 0.5F);

                handleFlightControls(player);
            } else {
                // Sin conductor: flotar en el aire, no caer
                Vec3 cur = this.getDeltaMovement();
                this.setDeltaMovement(cur.x * 0.8, 0, cur.z * 0.8);
                verticalMomentum = 0;
            }

            // move() aplica colisión con bloques correctamente (setPos la ignoraba)
            this.move(MoverType.SELF, this.getDeltaMovement());

            // Al tocar el suelo detener momentum vertical
            if (this.onGround()) {
                verticalMomentum = 0;
                Vec3 cur = this.getDeltaMovement();
                this.setDeltaMovement(cur.x * 0.8, 0, cur.z * 0.8);
            }
        }
    }

    private void handleFlightControls(Player player) {
        // Sistema de movimiento basado en la rotación del jugador
        float yaw = player.getYRot();

        // Obtener input
        float forward = player.zza;
        float strafe = player.xxa;

        // Calcular velocidad horizontal objetivo
        Vec3 lookVec = Vec3.directionFromRotation(0, yaw);
        Vec3 rightVec = new Vec3(-lookVec.z, 0, lookVec.x).normalize();

        double targetX = 0;
        double targetZ = 0;

        if (Math.abs(forward) > 0.01) {
            targetX += lookVec.x * forward * FLIGHT_SPEED;
            targetZ += lookVec.z * forward * FLIGHT_SPEED;
        }
        if (Math.abs(strafe) > 0.01) {
            targetX += rightVec.x * strafe * FLIGHT_SPEED;
            targetZ += rightVec.z * strafe * FLIGHT_SPEED;
        }

        // Lerp horizontal: suaviza aceleración y frenado para evitar tirones
        Vec3 current = this.getDeltaMovement();
        double smoothX = current.x + (targetX - current.x) * 0.4;
        double smoothZ = current.z + (targetZ - current.z) * 0.4;

        // Control vertical con lerp (evita cambios bruscos de velocidad vertical)
        if (player.jumping) {
            verticalMomentum += (VERTICAL_SPEED - verticalMomentum) * 0.4;
        } else if (player.isShiftKeyDown()) {
            verticalMomentum += (-VERTICAL_SPEED - verticalMomentum) * 0.4;
        } else {
            // Sin input vertical: frenar suavemente hasta 0
            verticalMomentum *= 0.85;
            if (Math.abs(verticalMomentum) < 0.005) verticalMomentum = 0;
        }

        verticalMomentum = Math.max(-1.0, Math.min(1.0, verticalMomentum));

        this.setDeltaMovement(smoothX, verticalMomentum, smoothZ);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!this.level().isClientSide) {
            ItemStack stack = player.getItemInHand(hand);

            // Solo se puede destruir con el item especial
            if (stack.is(ModItems.TAXI_DESTRUCTOR.get())) {
                this.discard();
                return InteractionResult.SUCCESS;
            }

            // Montar
            if (!player.isPassenger()) {
                if (this.getPassengers().isEmpty()) {
                    // Primera posición: conductor
                    player.startRiding(this);
                    return InteractionResult.SUCCESS;
                } else if (this.getPassengers().size() == 1) {
                    // Segunda posición: pasajero
                    player.startRiding(this);
                    this.entityData.set(HAS_PASSENGER, true);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (this.getPassengers().size() < 2) {
            this.entityData.set(HAS_PASSENGER, false);
        }
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // INVULNERABLE - Solo se puede destruir con el item especial
        return false;
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        // Máximo 2 pasajeros
        return this.getPassengers().size() < 2;
    }

    @Override
    public void positionRider(Entity passenger, MoveFunction callback) {
        if (this.hasPassenger(passenger)) {
            int index = this.getPassengers().indexOf(passenger);

            // Posición conductor (adelante)
            double xOffset = 0;
            double yOffset = 0.5;
            double zOffset = 0;

            if (index == 1) {
                // Posición pasajero (atrás)
                zOffset = 0.8;
            }

            // Aplicar rotación
            Vec3 offset = new Vec3(xOffset, yOffset, zOffset)
                    .yRot((float)Math.toRadians(-this.getYRot()));

            callback.accept(passenger,
                    this.getX() + offset.x,
                    this.getY() + offset.y,
                    this.getZ() + offset.z);
        }
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        // Desmontar al lado
        Vec3 offset = new Vec3(2, 0, 0).yRot((float)Math.toRadians(-this.getYRot()));
        return new Vec3(this.getX() + offset.x, this.getY(), this.getZ() + offset.z);
    }

    @Override
    public boolean shouldRiderSit() {
        return true;
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.5;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.entityData.set(HAS_PASSENGER, tag.getBoolean("HasPassenger"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putBoolean("HasPassenger", this.entityData.get(HAS_PASSENGER));
    }

    // ════════════════════════════════════════════════════════════
    // ANIMACIONES GECKOLIB
    // ════════════════════════════════════════════════════════════

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<TaxiVoladorEntity> state) {
        Entity controller = this.getFirstPassenger();

        if (controller instanceof Player player) {
            // Con conductor: animación de vuelo
            if (player.xxa != 0 || player.zza != 0) {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("walk"));
            } else {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("Standing"));
            }
        } else {
            // Sin conductor: idle
            state.getController().setAnimation(RawAnimation.begin().thenLoop("idle"));
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        return entity instanceof LivingEntity living ? living : null;
    }

    @Override
    public boolean isControlledByLocalInstance() {
        // El servidor controla el movimiento. Sin esto, el cliente ignora
        // las actualizaciones de posición del servidor y el taxi no se mueve.
        return false;
    }
}