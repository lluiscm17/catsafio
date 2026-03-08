package net.lluis.catsafio.entity.custom;

import net.lluis.catsafio.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
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

public class RunaEntity extends LivingEntity implements GeoEntity {

    private static final EntityDataAccessor<Boolean> ANIMATING = SynchedEntityData.defineId(RunaEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ANIMATION_TICKS = SynchedEntityData.defineId(RunaEntity.class, EntityDataSerializers.INT);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final int ANIMATION_DURATION = 60; // 3 segundos (60 ticks)
    private Player rewardPlayer = null;

    public RunaEntity(EntityType<? extends RunaEntity> type, Level level) {
        super(type, level);
        this.setInvulnerable(true);
        this.noPhysics = false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIMATING, false);
        this.entityData.define(ANIMATION_TICKS, 0);
    }

    @Override
    public void tick() {
        super.tick();

        // No tiene gravedad, se queda flotando
        this.setDeltaMovement(Vec3.ZERO);

        if (this.entityData.get(ANIMATING)) {
            int ticks = this.entityData.get(ANIMATION_TICKS);

            if (!this.level().isClientSide()) {
                // Spawner partículas de fuego en radio de 5 bloques
                if (ticks % 3 == 0) {
                    spawnFireParticles();
                }

                ticks++;
                this.entityData.set(ANIMATION_TICKS, ticks);

                // Terminar animación y dar recompensa
                if (ticks >= ANIMATION_DURATION) {
                    if (rewardPlayer != null && rewardPlayer.isAlive()) {
                        // Dar 5 deditas azules
                        ItemStack reward = new ItemStack(ModItems.DEDITA_AZUL.get(), 5);
                        if (!rewardPlayer.getInventory().add(reward)) {
                            rewardPlayer.drop(reward, false);
                        }

                        // Sonido de completado
                        rewardPlayer.playSound(SoundEvents.PLAYER_LEVELUP, 1.0f, 1.5f);

                        // Efecto final de partículas
                        if (this.level() instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(
                                    ParticleTypes.TOTEM_OF_UNDYING,
                                    this.getX(), this.getY() + 1, this.getZ(),
                                    30, 0.5, 0.5, 0.5, 0.1
                            );
                        }
                    }

                    this.entityData.set(ANIMATING, false);
                    this.entityData.set(ANIMATION_TICKS, 0);
                    rewardPlayer = null;
                }
            }
        }
    }

    private void spawnFireParticles() {
        if (this.level() instanceof ServerLevel serverLevel) {
            Vec3 center = this.position().add(0, 0.5, 0);

            // Generar partículas en círculo
            for (int i = 0; i < 8; i++) {
                double angle = (i / 8.0) * Math.PI * 2;
                double radius = 5.0;

                double offsetX = Math.cos(angle) * radius;
                double offsetZ = Math.sin(angle) * radius;
                double offsetY = random.nextDouble() * 2;

                serverLevel.sendParticles(
                        ParticleTypes.FLAME,
                        center.x + offsetX,
                        center.y + offsetY,
                        center.z + offsetZ,
                        2,
                        0.1, 0.1, 0.1,
                        0.02
                );

                // Añadir algunas partículas de humo
                if (i % 2 == 0) {
                    serverLevel.sendParticles(
                            ParticleTypes.LARGE_SMOKE,
                            center.x + offsetX,
                            center.y + offsetY,
                            center.z + offsetZ,
                            1,
                            0, 0.1, 0,
                            0
                    );
                }
            }
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!this.level().isClientSide()) {
            System.out.println("DEBUG: Interact called on server");
            System.out.println("DEBUG: Item in hand: " + stack.getItem());
            System.out.println("DEBUG: Is Dedita Celeste: " + stack.is(ModItems.DEDITA_CELESTE.get()));
        }

        // Verificar si tiene dedita celeste
        if (stack.is(ModItems.DEDITA_CELESTE.get())) {
            if (!this.level().isClientSide()) {
                System.out.println("DEBUG: Has Dedita Celeste!");

                // Ya está animando, no hacer nada
                if (this.entityData.get(ANIMATING)) {
                    System.out.println("DEBUG: Already animating");
                    return InteractionResult.PASS;
                }

                // Consumir dedita celeste
                stack.shrink(1);
                System.out.println("DEBUG: Consumed Dedita, starting animation");

                // Iniciar animación
                this.entityData.set(ANIMATING, true);
                this.entityData.set(ANIMATION_TICKS, 0);
                this.rewardPlayer = player;

                // Sonido de inicio
                this.playSound(SoundEvents.PORTAL_TRIGGER, 1.0f, 1.2f);

                return InteractionResult.SUCCESS;
            }

            return InteractionResult.CONSUME;
        }

        return super.interact(player, hand);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // Inmune a todo daño
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void pushEntities() {
        // No empuja entidades
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Animating", this.entityData.get(ANIMATING));
        tag.putInt("AnimationTicks", this.entityData.get(ANIMATION_TICKS));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(ANIMATING, tag.getBoolean("Animating"));
        this.entityData.set(ANIMATION_TICKS, tag.getInt("AnimationTicks"));
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    // ========== MÉTODOS ABSTRACTOS REQUERIDOS ==========

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return java.util.Collections.emptyList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        // No usa equipo
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    // ========== GeckoLib ==========

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<RunaEntity> state) {
        // ESTÁTICA - Sin animación idle
        if (this.entityData.get(ANIMATING)) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.runa.glow"));
        } else {
            // No hay animación idle - se queda estática
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}