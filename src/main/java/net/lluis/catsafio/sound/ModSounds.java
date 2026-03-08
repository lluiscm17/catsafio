package net.lluis.catsafio.sound;

import net.lluis.catsafio.Catsafio;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Catsafio.MOD_ID);

    // Sonido del Flashbang
    public static final RegistryObject<SoundEvent> FLASHBANG_SOUND = registerSoundEvent("flashbang_sound");

    // Sonido de la animación de muerte
    public static final RegistryObject<SoundEvent> DEATH_ANIMATION = registerSoundEvent("death_animation");
    public static final RegistryObject<SoundEvent> RULETA = registerSoundEvent("ruleta");
    public static final RegistryObject<SoundEvent> RULETA_ROSADA = registerSoundEvent("ruleta_rosada");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = new ResourceLocation(Catsafio.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}