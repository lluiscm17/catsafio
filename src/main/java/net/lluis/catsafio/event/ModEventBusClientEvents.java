package net.lluis.catsafio.event;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.entity.client.InfernalBullModel;
import net.lluis.catsafio.entity.client.ModModelLayers;
import net.lluis.catsafio.entity.client.TortugaInfernalModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.INFERNAL_BULL_LAYER, InfernalBullModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.TORTUGA_INFERNAL_LAYER, TortugaInfernalModel::createBodyLayer);
    }
}
