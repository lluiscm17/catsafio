package net.lluis.catsafio.event;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.entity.ModEntities;
import net.lluis.catsafio.entity.custom.*;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.INFERNAL_BULL.get(),    InfernalBullEntity.createAttributes().build());
        event.put(ModEntities.TORTUGA_INFERNAL.get(), TortugaInfernalEntity.createAttributes().build());
        event.put(ModEntities.CREEPER_CAKE.get(),     CreeperCakeEntity.createAttributes().build());
        event.put(ModEntities.SAPO.get(),             SapoEntity.createAttributes().build());
        event.put(ModEntities.FANTASMA.get(),          FantasmaEntity.createAttributes().build());
        event.put(ModEntities.WILDFIRE.get(),           WildfireEntity.createAttributes().build());
        event.put(ModEntities.FLASHBANG.get(),          FlashbangEntity.createAttributes().build());
        event.put(ModEntities.MURCIELAGO_WARDEN.get(),  MurcielagoWardenEntity.createAttributes().build());
        event.put(ModEntities.ORB.get(),                 OrbEntity.createAttributes().build());
        event.put(ModEntities.RUNA.get(), RunaEntity.createAttributes().build());
        event.put(ModEntities.CREEPER_WARDEN.get(), CreeperWardenEntity.createAttributes().build());
        event.put(ModEntities.GORGONA.get(), GorgonaEntity.createAttributes().build());
        event.put(ModEntities.BOMBA_AMARILLA.get(), BombaAmarillaEntity.createAttributes().build());

    }
}
