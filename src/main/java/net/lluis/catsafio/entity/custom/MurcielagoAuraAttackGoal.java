package net.lluis.catsafio.entity.custom;

import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

/**
 * Daño por contacto — comprueba cada tick si hay jugadores
 * dentro del radio de aura y aplica daño con cooldown.
 */
public class MurcielagoAuraAttackGoal extends Goal {

    private final MurcielagoWardenEntity murcielago;

    public MurcielagoAuraAttackGoal(MurcielagoWardenEntity entity) {
        this.murcielago = entity;
        this.setFlags(EnumSet.noneOf(Flag.class)); // no bloquea otros goals
    }

    @Override
    public boolean canUse() {
        return murcielago.getState() == MurcielagoWardenEntity.STATE_PERSIGUE;
    }

    @Override
    public boolean canContinueToUse() { return canUse(); }

    @Override
    public void tick() {
        murcielago.tryAuraAttack();
    }
}
