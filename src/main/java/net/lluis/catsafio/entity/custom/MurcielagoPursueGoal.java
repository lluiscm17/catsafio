package net.lluis.catsafio.entity.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

/**
 * Persecución aérea dinámica — ajusta trayectoria para interceptar
 * al jugador prediciendo su posición futura.
 */
public class MurcielagoPursueGoal extends Goal {

    private final MurcielagoWardenEntity murcielago;
    private LivingEntity target;

    public MurcielagoPursueGoal(MurcielagoWardenEntity entity) {
        this.murcielago = entity;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return murcielago.getState() == MurcielagoWardenEntity.STATE_PERSIGUE
            && murcielago.getTarget() != null
            && murcielago.getTarget().isAlive();
    }

    @Override
    public boolean canContinueToUse() { return canUse(); }

    @Override
    public void start() {
        target = murcielago.getTarget();
    }

    @Override
    public void tick() {
        if (target == null || !target.isAlive()) return;

        murcielago.getLookControl().setLookAt(target, 30f, 30f);

        // Predicción: apuntar donde estará el jugador en ~5 ticks
        Vec3 playerVel = target.getDeltaMovement();
        Vec3 predicted = target.position()
            .add(0, target.getBbHeight() * 0.5, 0)
            .add(playerVel.scale(5));

        // Mover hacia la posición predicha con velocidad alta
        murcielago.getMoveControl().setWantedPosition(
            predicted.x, predicted.y, predicted.z, 1.4);

        // Zigzag leve para movimiento impredecible
        if (murcielago.tickCount % 12 == 0) {
            Vec3 perp = new Vec3(
                -playerVel.z + (murcielago.getRandom().nextFloat() - 0.5) * 0.5,
                (murcielago.getRandom().nextFloat() - 0.5) * 0.3,
                playerVel.x  + (murcielago.getRandom().nextFloat() - 0.5) * 0.5
            ).normalize().scale(0.15);

            murcielago.setDeltaMovement(
                murcielago.getDeltaMovement().add(perp));
        }
    }

    @Override
    public void stop() {
        target = null;
        murcielago.getNavigation().stop();
    }
}
