package net.lluis.catsafio.entity.custom;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.*;

/**
 * Goal de la lengua: se acerca al jugador y lo coge.
 * Cooldown de 30s por UUID de jugador (no afecta a otros jugadores).
 */
public class SapoTongueGoal extends Goal {

    private final SapoEntity sapo;
    private Player target;

    // Cooldown individual por jugador UUID
    private final Map<UUID, Integer> playerCooldowns = new HashMap<>();

    private static final double MOVE_RANGE_SQ   = (SapoEntity.TONGUE_RANGE_BLOCKS * SapoEntity.TONGUE_RANGE_BLOCKS);
    private static final double GRAB_RANGE_SQ   = 3.5 * 3.5;

    public SapoTongueGoal(SapoEntity sapo) {
        this.sapo = sapo;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (sapo.hasGrabbed() || sapo.hasSwallowed()) return false;
        target = sapo.getTarget() instanceof Player p ? p : null;
        if (target == null || !target.isAlive()) return false;
        // Verificar cooldown individual del jugador
        int cd = playerCooldowns.getOrDefault(target.getUUID(), 0);
        if (cd > 0) return false;
        return sapo.distanceToSqr(target) <= MOVE_RANGE_SQ;
    }

    @Override
    public boolean canContinueToUse() {
        if (sapo.hasGrabbed() || sapo.hasSwallowed()) return false;
        return target != null && target.isAlive()
               && sapo.distanceToSqr(target) <= MOVE_RANGE_SQ;
    }

    @Override
    public void tick() {
        // Actualizar cooldowns
        playerCooldowns.replaceAll((uuid, cd) -> Math.max(0, cd - 1));

        if (target == null) return;
        sapo.getLookControl().setLookAt(target, 30f, 30f);

        double dist = sapo.distanceToSqr(target);

        if (dist <= GRAB_RANGE_SQ) {
            // Coger al jugador
            sapo.grabPlayer(target);
            // Activar cooldown para este jugador
            playerCooldowns.put(target.getUUID(), SapoEntity.COOLDOWN_TICKS);
            stop();
        } else {
            sapo.getNavigation().moveTo(target, 1.0);
        }
    }

    @Override
    public void stop() {
        sapo.getNavigation().stop();
        target = null;
    }
}
