package net.lluis.catsafio.entity.custom;

import net.lluis.catsafio.network.OrbPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class OrbEntity extends PathfinderMob implements GeoEntity {

    // ── Datos sincronizados ────────────────────────────────────────────
    public static final EntityDataAccessor<String> SELECTED_MOB =
        SynchedEntityData.defineId(OrbEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Integer> SPAWN_INTERVAL =
        SynchedEntityData.defineId(OrbEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> IS_ACTIVE =
        SynchedEntityData.defineId(OrbEntity.class, EntityDataSerializers.BOOLEAN);

    private static final int DETECT_RANGE = 16; // bloques de detección
    private int spawnTimer = 0;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public OrbEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
        this.noPhysics = true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 50.0)
            .add(Attributes.MOVEMENT_SPEED, 0.0)   // no se mueve
            .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SELECTED_MOB,    "minecraft:zombie");
        this.entityData.define(SPAWN_INTERVAL,  100); // 5s por defecto
        this.entityData.define(IS_ACTIVE,       false);
    }

    // ── Click derecho en creativo → abrir menú ────────────────────────
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.level().isClientSide && player.isCreative()
                && player instanceof ServerPlayer sp) {
            OrbPackets.sendOpenGui(sp, this.getId(),
                this.entityData.get(SELECTED_MOB),
                this.entityData.get(SPAWN_INTERVAL));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    // ── Tick: detectar jugadores y spawnear ───────────────────────────
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) return;

        // Flotar suavemente
        double wave = Math.sin(this.tickCount * 0.05) * 0.03;
        this.setDeltaMovement(0, wave, 0);

        // Detectar jugadores cercanos
        boolean playerNear = !this.level()
            .getEntitiesOfClass(Player.class,
                this.getBoundingBox().inflate(DETECT_RANGE),
                p -> !p.isCreative() && !p.isSpectator())
            .isEmpty();

        this.entityData.set(IS_ACTIVE, playerNear);

        if (!playerNear) {
            spawnTimer = 0;
            return;
        }

        spawnTimer++;
        int interval = this.entityData.get(SPAWN_INTERVAL);
        if (spawnTimer >= interval) {
            spawnTimer = 0;
            spawnMob();
        }
    }

    private void spawnMob() {
        if (!(this.level() instanceof ServerLevel serverLevel)) return;

        String mobId = this.entityData.get(SELECTED_MOB);
        EntityType<?> type = ForgeRegistries.ENTITY_TYPES
            .getValue(new ResourceLocation(mobId));
        if (type == null) return;

        Entity entity = type.create(serverLevel);
        if (entity == null) return;

        // Spawnear alrededor del orb
        double angle = this.random.nextDouble() * Math.PI * 2;
        double dist  = 2 + this.random.nextDouble() * 2;
        double spawnX = this.getX() + Math.cos(angle) * dist;
        double spawnY = this.getY();
        double spawnZ = this.getZ() + Math.sin(angle) * dist;

        entity.moveTo(spawnX, spawnY, spawnZ, 0, 0);
        if (entity instanceof Mob mob) {
            mob.finalizeSpawn(serverLevel,
                serverLevel.getCurrentDifficultyAt(this.blockPosition()),
                MobSpawnType.MOB_SUMMONED, null, null);
        }
        serverLevel.addFreshEntity(entity);
    }

    // ── Métodos públicos para OrbPackets ──────────────────────────────
    public void setSelectedMob(String mobId) {
        this.entityData.set(SELECTED_MOB, mobId);
    }

    public void setSpawnInterval(int ticks) {
        this.entityData.set(SPAWN_INTERVAL, Math.max(20, ticks));
    }

    public String getSelectedMob()  { return this.entityData.get(SELECTED_MOB);    }
    public int    getSpawnInterval(){ return this.entityData.get(SPAWN_INTERVAL);   }
    public boolean isActive()       { return this.entityData.get(IS_ACTIVE);        }

    // ── NBT ───────────────────────────────────────────────────────────
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("SelectedMob",    this.entityData.get(SELECTED_MOB));
        tag.putInt("SpawnInterval",     this.entityData.get(SPAWN_INTERVAL));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("SelectedMob"))
            this.entityData.set(SELECTED_MOB, tag.getString("SelectedMob"));
        if (tag.contains("SpawnInterval"))
            this.entityData.set(SPAWN_INTERVAL, tag.getInt("SpawnInterval"));
    }

    public static boolean checkSpawnRules(EntityType<OrbEntity> type,
                                          ServerLevelAccessor level,
                                          MobSpawnType reason,
                                          BlockPos pos, RandomSource random) {
        return reason == MobSpawnType.COMMAND || reason == MobSpawnType.SPAWNER;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader level) { return true; }

    // ── GeckoLib ───────────────────────────────────────────────────────
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 3, state -> {
            if (isActive()) {
                return state.setAndContinue(RawAnimation.begin()
                    .then("spawner_open", Animation.LoopType.LOOP));
            }
            return state.setAndContinue(RawAnimation.begin()
                .then("idle", Animation.LoopType.LOOP));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }
}
