package net.lluis.catsafio.event;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.entity.ModEntities;
import net.lluis.catsafio.entity.custom.InfernalBullEntity;
import net.lluis.catsafio.entity.custom.TortugaInfernalEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.INFERNAL_BULL.get(), InfernalBullEntity.createAttributes().build());
        event.put(ModEntities.TORTUGA_INFERNAL.get(), TortugaInfernalEntity.createAttributes().build());
    }
}
