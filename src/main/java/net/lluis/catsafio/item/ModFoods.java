package net.lluis.catsafio.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties POKELITO = new FoodProperties.Builder().nutrition(4).fast()
            .saturationMod(0.4f).effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 1200), 1f).build();
}
