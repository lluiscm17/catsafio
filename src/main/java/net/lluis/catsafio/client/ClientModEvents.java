package net.lluis.catsafio.client;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.client.model.CustomCascoInfernit;
import net.lluis.catsafio.client.model.CustomHat;
import net.lluis.catsafio.client.model.CustomPartyHat;
import net.lluis.catsafio.client.model.CustomSuit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)

public class ClientModEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CustomCascoInfernit.LAYER_LOCATION, CustomCascoInfernit::createBodyLayer);
        event.registerLayerDefinition(CustomHat.LAYER_LOCATION, CustomHat::createBodyLayer);
        event.registerLayerDefinition(CustomSuit.LAYER_LOCATION, CustomSuit::createBodyLayer);
        event.registerLayerDefinition(CustomPartyHat.LAYER_LOCATION, CustomPartyHat::createBodyLayer);
    }

}
