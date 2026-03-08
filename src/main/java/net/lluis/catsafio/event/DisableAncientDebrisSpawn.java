package net.lluis.catsafio.event;

import net.lluis.catsafio.Catsafio;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Desactiva la generación natural de Ancient Debris (Escombros Ancestrales) en el Nether
 * 
 * Reemplaza Ancient Debris generado naturalmente con Netherrack
 */
@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DisableAncientDebrisSpawn {

    private static int debrisRemoved = 0;

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;
        
        // Solo en el Nether
        if (!serverLevel.dimension().equals(Level.NETHER)) return;

        // Obtener posición del chunk
        int chunkX = event.getChunk().getPos().x;
        int chunkZ = event.getChunk().getPos().z;
        
        // Coordenadas del chunk en bloques
        int startX = chunkX * 16;
        int startZ = chunkZ * 16;
        
        // Buscar y reemplazar Ancient Debris en todo el chunk
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 128; y++) { // Ancient Debris genera entre Y 8-119
                    BlockPos pos = new BlockPos(startX + x, y, startZ + z);
                    BlockState state = serverLevel.getBlockState(pos);
                    
                    if (state.is(Blocks.ANCIENT_DEBRIS)) {
                        // Reemplazar con Netherrack
                        serverLevel.setBlock(pos, Blocks.NETHERRACK.defaultBlockState(), 3);
                        debrisRemoved++;
                        
                        if (debrisRemoved % 100 == 0) {
                            Catsafio.LOGGER.info("Removed {} Ancient Debris blocks total", debrisRemoved);
                        }
                    }
                }
            }
        }
    }
}
