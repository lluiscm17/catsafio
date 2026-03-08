package net.lluis.catsafio.entity.custom;

import net.lluis.catsafio.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FantasmaEntity extends PathfinderMob implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    // Solo puede ser dañado por la espada fantasmal
    private static final String PHANTOM_SWORD_ID = "catsafio:phantom_sword";

    public FantasmaEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.noPhysics = true; // atraviesa paredes
        this.setNoGravity(true); // flota
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
            .add(Attributes.MAX_HEALTH,        30.0)  // 15 corazones
            .add(Attributes.MOVEMENT_SPEED,    0.25)
            .add(Attributes.FOLLOW_RANGE,      20.0)
            .add(Attributes.ATTACK_DAMAGE,     4.0);  // 2 corazones
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new FantasmaMeleeGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 12f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    // ── Solo daña la espada fantasmal ──────────────────────────────────
    @Override
    public boolean hurt(DamageSource source, float amount) {
        // Siempre ignora daño excepto de la espada fantasmal
        if (source.getDirectEntity() instanceof Player player) {
            ItemStack held = player.getMainHandItem();
            if (held.getItem() == ModItems.PHANTOM_SWORD.get()) {
                return super.hurt(source, amount);
            }
        }
        // Daño por comandos o admin (para testing)
        if (source == this.level().damageSources().genericKill()) {
            return super.hurt(source, amount);
        }
        return false;
    }

    // ── Atraviesa paredes: sin colisión con bloques ────────────────────
    @Override
    public boolean isPushable() { return false; }

    @Override
    public boolean canBeCollidedWith() { return false; }

    @Override
    protected boolean isAffectedByFluids() { return false; }

    @Override
    public boolean checkSpawnObstruction(LevelReader level) { return true; }

    // ── Movimiento flotante: siempre se mueve hacia el jugador en 3D ──
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) return;

        // Flotar suavemente arriba/abajo
        double targetY = this.getY();
        LivingEntity target = this.getTarget();
        if (target != null) {
            // Volar hacia el jugador en los 3 ejes
            Vec3 dir = target.position().add(0, 1, 0)
                .subtract(this.position()).normalize().scale(0.25);
            this.setDeltaMovement(dir);
        } else {
            // Flotar arriba y abajo suavemente cuando no hay target
            double wave = Math.sin(this.tickCount * 0.05) * 0.05;
            this.setDeltaMovement(this.getDeltaMovement().x, wave, this.getDeltaMovement().z);
        }
    }

    // ── Spawn solo en cuevas (nivel de luz bajo, bajo tierra) ─────────
    public static boolean checkSpawnRules(EntityType<FantasmaEntity> type,
                                          ServerLevelAccessor level,
                                          MobSpawnType reason,
                                          BlockPos pos,
                                          RandomSource random) {
        // Solo en Overworld
        if (level.getBiome(pos).is(net.minecraft.tags.BiomeTags.IS_NETHER)) return false;
        if (level.getBiome(pos).is(net.minecraft.tags.BiomeTags.IS_END))    return false;
        // Solo bajo tierra (y < 60) y sin luz
        return pos.getY() < 60 && level.getRawBrightness(pos, 0) == 0;
    }

    // ── GeckoLib ───────────────────────────────────────────────────────
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 3, state -> {
            if (this.swinging) {
                return state.setAndContinue(RawAnimation.begin()
                    .then("attack", Animation.LoopType.PLAY_ONCE));
            }
            if (state.isMoving()) {
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
