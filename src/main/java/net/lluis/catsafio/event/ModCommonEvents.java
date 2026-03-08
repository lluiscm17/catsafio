package net.lluis.catsafio.event;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.entity.ModEntities;
import net.lluis.catsafio.entity.custom.CreeperCakeEntity;
import net.lluis.catsafio.entity.custom.FantasmaEntity;
import net.lluis.catsafio.entity.custom.MurcielagoWardenEntity;
import net.lluis.catsafio.entity.custom.WildfireEntity;
import net.lluis.catsafio.entity.custom.InfernalBullEntity;
import net.lluis.catsafio.entity.custom.SapoEntity;
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
            SpawnPlacements.register(ModEntities.INFERNAL_BULL.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.WORLD_SURFACE,
                InfernalBullEntity::canSpawn);

            SpawnPlacements.register(ModEntities.TORTUGA_INFERNAL.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.WORLD_SURFACE,
                TortugaInfernalEntity::canSpawn);

            SpawnPlacements.register(ModEntities.CREEPER_CAKE.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.WORLD_SURFACE,
                CreeperCakeEntity::checkSpawnRules);

            SpawnPlacements.register(ModEntities.SAPO.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.WORLD_SURFACE,
                SapoEntity::checkSpawnRules);

            SpawnPlacements.register(ModEntities.FANTASMA.get(),
                SpawnPlacements.Type.NO_RESTRICTIONS,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                FantasmaEntity::checkSpawnRules);

            SpawnPlacements.register(ModEntities.MURCIELAGO_WARDEN.get(),
                SpawnPlacements.Type.NO_RESTRICTIONS,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MurcielagoWardenEntity::checkSpawnRules);

            SpawnPlacements.register(ModEntities.WILDFIRE.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                WildfireEntity::checkSpawnRules);
        });
    }
}
