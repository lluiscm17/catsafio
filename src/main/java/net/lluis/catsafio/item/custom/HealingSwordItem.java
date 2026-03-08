package net.lluis.catsafio.item.custom;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class HealingSwordItem extends SwordItem {
    private static final float HEAL_CHANCE = 0.20f; // 20% de probabilidad
    private static final float HEAL_AMOUNT = 2.0f; // 1 corazón (2 puntos de vida)

    public HealingSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        // Solo funciona si el atacante es un jugador
        if (pAttacker instanceof Player player) {
            // Generar número aleatorio entre 0 y 1
            if (player.getRandom().nextFloat() < HEAL_CHANCE) {
                // Curar al jugador
                player.heal(HEAL_AMOUNT);
                
                // Efecto visual opcional: pequeño efecto de regeneración
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20, 0, false, false, true));
            }
        }
        
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
