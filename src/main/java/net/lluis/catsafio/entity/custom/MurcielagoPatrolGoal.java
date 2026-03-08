package net.lluis.catsafio.entity.custom;

import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

/**
 * Patrullaje aéreo horizontal — cambia de dirección al chocar
 * con paredes u obstáculos de forma autónoma.
 */
public class MurcielagoPatrolGoal extends Goal {

    private final MurcielagoWardenEntity murcielago;

    public MurcielagoPatrolGoal(MurcielagoWardenEntity entity) {
        this.murcielago = entity;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return murcielago.getState() == MurcielagoWardenEntity.STATE_PATRULLA;
    }

    @Override
    public boolean canContinueToUse() { return canUse(); }

    @Override
    public void tick() {
        murcielago.updatePatrol();
    }
}
