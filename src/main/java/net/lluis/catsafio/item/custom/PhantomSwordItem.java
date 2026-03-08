package net.lluis.catsafio.item.custom;

import net.lluis.catsafio.entity.custom.FantasmaEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class PhantomSwordItem extends SwordItem {

    public PhantomSwordItem(Tier tier, int attackDamageModifier, float attackSpeedModifier,
                             Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Daña normalmente a todos los mobs
        // El fantasma solo puede ser dañado si el atacante lleva esta espada
        // (la restricción está en FantasmaEntity.hurt())
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, net.minecraft.world.level.block.state.BlockState state) {
        return 1.0f;
    }
}
