package net.lluis.catsafio.entity.custom;

import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

/**
 * Control de movimiento aéreo suave — sin física terrestre.
 * Se mueve hacia el destino interpolando velocidad para vuelo fluido.
 */
public class MurcielagoMoveControl extends MoveControl {

    private final MurcielagoWardenEntity murcielago;

    public MurcielagoMoveControl(MurcielagoWardenEntity entity) {
        super(entity);
        this.murcielago = entity;
    }

    @Override
    public void tick() {
        if (this.operation == Operation.MOVE_TO) {
            Vec3 target = new Vec3(
                this.wantedX - murcielago.getX(),
                this.wantedY - murcielago.getY(),
                this.wantedZ - murcielago.getZ()
            );

            double dist = target.length();
            if (dist < 0.5) {
                this.operation = Operation.WAIT;
                murcielago.setDeltaMovement(
                    murcielago.getDeltaMovement().scale(0.5));
                return;
            }

            // Velocidad suave: máximo speedModifier, mínimo para no pararse
            double speed = Math.min(this.speedModifier * 0.3, dist * 0.25);
            Vec3 dir = target.normalize().scale(speed);

            // Interpolar con la velocidad actual para vuelo fluido
            Vec3 current = murcielago.getDeltaMovement();
            murcielago.setDeltaMovement(
                current.x + (dir.x - current.x) * 0.3,
                current.y + (dir.y - current.y) * 0.3,
                current.z + (dir.z - current.z) * 0.3
            );

            // Rotar hacia el objetivo
            float yaw = (float)(Math.toDegrees(Math.atan2(dir.z, dir.x)) - 90);
            murcielago.setYRot(yaw);
            murcielago.yBodyRot = yaw;

        } else if (this.operation == Operation.WAIT) {
            // Deceleración gradual
            murcielago.setDeltaMovement(
                murcielago.getDeltaMovement().scale(0.85));
        }
    }
}
