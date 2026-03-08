package net.lluis.catsafio.entity.ai;

import net.lluis.catsafio.entity.custom.CreeperCakeEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

/**
 * Goal de ataque sin cooldown: en cuanto detecta al jugador en rango
 * se acerca y activa la mecha directamente.
 */
public class CreeperCakeAttackGoal extends Goal {

    private final CreeperCakeEntity creeper;
    private static final double EXPLOSION_RANGE = 3.0; // distancia para activar mecha

    public CreeperCakeAttackGoal(CreeperCakeEntity creeper) {
        this.creeper = creeper;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return creeper.getTarget() instanceof Player p && p.isAlive();
    }

    @Override
    public void tick() {
        Player target = (Player) creeper.getTarget();
        if (target == null) return;

        double dist = creeper.distanceToSqr(target);

        // Moverse hacia el jugador
        creeper.getNavigation().moveTo(target, 1.0);

        // Sin cooldown: en cuanto está en rango, activar mecha
        if (dist <= EXPLOSION_RANGE * EXPLOSION_RANGE) {
            creeper.startFuse();
            creeper.getNavigation().stop();
        }
    }

    @Override
    public void stop() {
        creeper.getNavigation().stop();
    }
}
