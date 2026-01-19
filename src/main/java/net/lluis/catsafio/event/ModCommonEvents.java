package net.lluis.catsafio.event;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.entity.ModEntities;
import net.lluis.catsafio.entity.custom.InfernalBullEntity;
import net.lluis.catsafio.entity.custom.TortugaInfernalEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCommonEvents {
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            SpawnPlacements.register(ModEntities.INFERNAL_BULL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, InfernalBullEntity::canSpawn);
            SpawnPlacements.register(ModEntities.TORTUGA_INFERNAL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, TortugaInfernalEntity::canSpawn);
        });
    }
}
