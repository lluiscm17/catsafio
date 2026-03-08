package net.lluis.catsafio.entity.custom;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class WildfireFireball extends SmallFireball {

    private static final float DAMAGE = 8f; // 4 corazones

    public WildfireFireball(Level level, LivingEntity shooter,
                             double dx, double dy, double dz) {
        super(level, shooter, dx, dy, dz);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity hit = result.getEntity();
        if (hit instanceof LivingEntity living && this.getOwner() instanceof WildfireEntity wildfire) {
            // Daño ignorando fire resistance
            wildfire.applyFireDamageIgnoringResistance(living, DAMAGE);
        }
    }
}
