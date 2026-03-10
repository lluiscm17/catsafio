package net.lluis.catsafio.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ProteccionSolarEffect extends MobEffect {

    public ProteccionSolarEffect() {
        // BENEFICIAL = icono con borde azul; 0xFFD700 = color dorado en el HUD
        super(MobEffectCategory.BENEFICIAL, 0xFFD700);
    }
}
