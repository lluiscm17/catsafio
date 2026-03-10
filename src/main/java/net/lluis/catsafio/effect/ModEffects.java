package net.lluis.catsafio.effect;

import net.lluis.catsafio.Catsafio;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {

    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Catsafio.MOD_ID);

    public static final RegistryObject<MobEffect> PROTECCION_SOLAR =
            EFFECTS.register("proteccion_solar", ProteccionSolarEffect::new);

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
