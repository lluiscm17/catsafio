package net.lluis.catsafio.entity.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class FlashbangChaseGoal extends Goal {

    private final FlashbangEntity flashbang;
    private LivingEntity target;

    public FlashbangChaseGoal(FlashbangEntity flashbang) {
        this.flashbang = flashbang;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        target = flashbang.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        return target != null && target.isAlive() && !flashbang.hasExploded;
    }

    @Override
    public void tick() {
        if (target == null) return;
        flashbang.getLookControl().setLookAt(target, 30f, 30f);
        // Perseguir a máxima velocidad
        flashbang.getNavigation().moveTo(target, 1.4);
    }

    @Override
    public void stop() {
        flashbang.getNavigation().stop();
        target = null;
    }


}
