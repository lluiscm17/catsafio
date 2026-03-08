package net.lluis.catsafio.entity.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class FantasmaMeleeGoal extends Goal {

    private final FantasmaEntity fantasma;
    private LivingEntity target;
    private int attackCooldown = 0;
    private static final double ATTACK_RANGE_SQ = 2.5 * 2.5;

    public FantasmaMeleeGoal(FantasmaEntity fantasma) {
        this.fantasma = fantasma;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        target = fantasma.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        return target != null && target.isAlive();
    }

    @Override
    public void tick() {
        if (target == null) return;
        if (attackCooldown > 0) attackCooldown--;

        fantasma.getLookControl().setLookAt(target, 30f, 30f);

        if (attackCooldown == 0 && fantasma.distanceToSqr(target) <= ATTACK_RANGE_SQ) {
            fantasma.doHurtTarget(target);
            fantasma.swing(net.minecraft.world.InteractionHand.MAIN_HAND);
            attackCooldown = 20; // 1s entre ataques
        }
    }

    @Override
    public void stop() {
        target = null;
    }
}
