package net.lluis.catsafio.entity.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class WildfireFireballGoal extends Goal {

    private final WildfireEntity wildfire;
    private LivingEntity target;
    private int chargeTimer = 0;
    private static final int CHARGE_TIME  = 10; // 0.5s cargando
    private static final int BURST_COUNT  = 3;  // 3 bolas por ráfaga
    private int burstShot = 0;
    private int burstTimer = 0;
    private static final int COOLDOWN = 30; // 1.5s entre ráfagas

    public WildfireFireballGoal(WildfireEntity wildfire) {
        this.wildfire = wildfire;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        target = wildfire.getTarget();
        return target != null && target.isAlive()
                && wildfire.distanceToSqr(target) <= 48 * 48;
    }

    @Override
    public boolean canContinueToUse() {
        return target != null && target.isAlive();
    }

    @Override
    public void tick() {
        if (target == null) return;

        wildfire.getLookControl().setLookAt(target, 30f, 30f);

        // Mantener distancia óptima (8-16 bloques)
        double dist = wildfire.distanceToSqr(target);
        if (dist < 8 * 8) {
            // Alejarse del jugador
            wildfire.getNavigation().moveTo(
                    wildfire.getX() - (target.getX() - wildfire.getX()),
                    wildfire.getY(),
                    wildfire.getZ() - (target.getZ() - wildfire.getZ()),
                    1.0
            );
        } else if (dist > 20 * 20) {
            wildfire.getNavigation().moveTo(target, 1.0);
        } else {
            wildfire.getNavigation().stop();
        }

        chargeTimer++;

        if (chargeTimer >= CHARGE_TIME) {
            // Ráfaga de disparos
            burstTimer++;
            if (burstTimer % 5 == 0 && burstShot < BURST_COUNT) {
                wildfire.shootFireball(target);
                // El daño lo aplica el proyectil al impactar (SmallFireball)
                burstShot++;
            }
            if (burstShot >= BURST_COUNT) {
                chargeTimer = -COOLDOWN;
                burstShot = 0;
                burstTimer = 0;
            }
        }
    }

    @Override
    public void stop() {
        target = null;
        chargeTimer = 0;
        burstShot = 0;
    }
}