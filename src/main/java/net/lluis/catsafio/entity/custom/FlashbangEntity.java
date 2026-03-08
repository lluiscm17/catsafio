package net.lluis.catsafio.entity.custom;

import net.lluis.catsafio.network.FlashbangPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FlashbangEntity extends PathfinderMob implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    // Cooldown para no flashear al mismo jugador dos veces seguidas
    private int flashCooldown = 0;
    public boolean hasExploded = false;

    public FlashbangEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH,      8.0)   // 4 corazones
                .add(Attributes.MOVEMENT_SPEED,  0.55)  // muy rápido
                .add(Attributes.FOLLOW_RANGE,    24.0)
                .add(Attributes.ATTACK_DAMAGE,   2.0);  // 1 corazón
    }

    @Override
    protected void registerGoals() {
        // Perseguir al jugador más cercano a máxima velocidad
        this.goalSelector.addGoal(1, new FlashbangChaseGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8f));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(
                this, Player.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (flashCooldown > 0) flashCooldown--;
        if (this.level().isClientSide) return;

        // Ignorar jugadores en creativo/espectador como target
        LivingEntity target = this.getTarget();
        if (target instanceof Player p && (p.isCreative() || p.isSpectator())) {
            this.setTarget(null);
            return;
        }

        // Detectar colisión con jugador (solo supervivencia)
        if (!hasExploded) {
            for (Player player : this.level().getEntitiesOfClass(
                    Player.class, this.getBoundingBox().inflate(0.5))) {
                if (player.isCreative() || player.isSpectator()) continue;
                flashPlayer(player);
                hasExploded = true;
                this.hurt(this.damageSources().genericKill(), Float.MAX_VALUE);
                break;
            }
        }
    }

    private void flashPlayer(Player player) {
        // Daño
        player.hurt(this.damageSources().mobAttack(this), 2.0f);

        // Enviar packet de flash al cliente
        if (player instanceof ServerPlayer sp) {
            FlashbangPackets.sendFlash(sp);
        }
    }

    // No spawna naturalmente
    public static boolean checkSpawnRules(EntityType<FlashbangEntity> type,
                                          ServerLevelAccessor level,
                                          MobSpawnType reason,
                                          BlockPos pos,
                                          RandomSource random) {
        return reason == MobSpawnType.COMMAND || reason == MobSpawnType.SPAWNER;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader level) { return true; }

    // ── GeckoLib ───────────────────────────────────────────────────────
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 2, state -> {
            if (hasExploded) {
                return state.setAndContinue(RawAnimation.begin()
                        .then("flashbang.explode", Animation.LoopType.PLAY_ONCE));
            }
            if (state.isMoving()) {
                return state.setAndContinue(RawAnimation.begin()
                        .then("flashbang.walk", Animation.LoopType.LOOP));
            }
            return state.setAndContinue(RawAnimation.begin()
                    .then("flashbang.idle", Animation.LoopType.LOOP));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }
}