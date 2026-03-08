package net.lluis.catsafio.event;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.entity.ModEntities;
import net.lluis.catsafio.entity.custom.*;
import net.lluis.catsafio.hub.command.SpawnControlCommand;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

/**
 * VERSIÓN DEBUG - Con logs para troubleshooting
 * Usa esta versión temporalmente para encontrar el problema
 */
@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CustomMobSpawnHandler {

    private static final Random RANDOM = new Random();
    private static int tickCounter = 0;
    private static final int CHECK_INTERVAL = 200; // 10 segundos

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getEntity() instanceof Mob mob)) return;

        String entityId = getEntityId(mob);
        if (entityId == null) return;

        if (!SpawnControlCommand.isSpawnEnabled(entityId)) {
            Catsafio.LOGGER.info("BLOCKED spawn of {} (disabled in SpawnControl)", entityId);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        tickCounter++;
        if (tickCounter < CHECK_INTERVAL) return;
        tickCounter = 0;

        for (ServerLevel level : event.getServer().getAllLevels()) {
            if (!level.dimension().equals(ServerLevel.OVERWORLD)) continue;

            level.players().forEach(player -> {
                // FLASHBANG
                if (SpawnControlCommand.isSpawnEnabled("flashbang") && RANDOM.nextDouble() < 0.2) {
                    spawnNearPlayer(level, player.blockPosition(), ModEntities.FLASHBANG.get(), 1, 2, 32, 64);
                }

                // SAPO
                if (SpawnControlCommand.isSpawnEnabled("sapo") && !level.isDay() && RANDOM.nextDouble() < 0.15) {
                    spawnNearPlayer(level, player.blockPosition(), ModEntities.SAPO.get(), 1, 2, 32, 64);
                }

                // CREEPER CAKE
                if (SpawnControlCommand.isSpawnEnabled("creepercake") && level.isDay() && RANDOM.nextDouble() < 0.1) {
                    spawnNearPlayer(level, player.blockPosition(), ModEntities.CREEPER_CAKE.get(), 1, 1, 32, 64);
                }

                // FANTASMA - Solo noche
                if (SpawnControlCommand.isSpawnEnabled("fantasma") && !level.isDay() && RANDOM.nextDouble() < 0.12) {
                    spawnNearPlayer(level, player.blockPosition(), ModEntities.FANTASMA.get(), 1, 2, 32, 64);
                }

                // BOMBA AMARILLA - Día y noche (como especificaste)
                if (SpawnControlCommand.isSpawnEnabled("bomba_amarilla") && RANDOM.nextDouble() < 0.10) {
                    spawnNearPlayer(level, player.blockPosition(), ModEntities.BOMBA_AMARILLA.get(), 1, 1, 32, 64);
                }

                // ========== CREEPER WARDEN - VERSION DEBUG ==========
                // OPCIÓN A: Solo Deep Dark (original)
                if (SpawnControlCommand.isSpawnEnabled("creeper_warden") && RANDOM.nextDouble() < 0.08) {
                    Catsafio.LOGGER.info("=== CREEPER WARDEN SPAWN ATTEMPT ===");
                    Catsafio.LOGGER.info("Player: {} at {}", player.getName().getString(), player.blockPosition());
                    spawnInDeepDark(level, player.blockPosition());
                }

                // OPCIÓN B: Cualquier bioma (descomenta para testing)
                // if (SpawnControlCommand.isSpawnEnabled("creeper_warden") && RANDOM.nextDouble() < 0.5) {
                //     Catsafio.LOGGER.info("=== CREEPER WARDEN SPAWN (TESTING MODE) ===");
                //     spawnNearPlayer(level, player.blockPosition(), ModEntities.CREEPER_WARDEN.get(), 1, 1, 32, 64);
                // }
            });
        }
    }

    private static void spawnInDeepDark(ServerLevel level, BlockPos playerPos) {
        int radius = 32 + RANDOM.nextInt(32);
        double angle = RANDOM.nextDouble() * Math.PI * 2;

        int offsetX = (int)(Math.cos(angle) * radius);
        int offsetZ = (int)(Math.sin(angle) * radius);

        BlockPos searchPos = new BlockPos(
                playerPos.getX() + offsetX,
                playerPos.getY(),
                playerPos.getZ() + offsetZ
        );

        Catsafio.LOGGER.info("Checking position: {}", searchPos);
        Catsafio.LOGGER.info("Biome at position: {}", level.getBiome(searchPos));

        // Verificar si es Deep Dark
        if (!level.getBiome(searchPos).is(Biomes.DEEP_DARK)) {
            Catsafio.LOGGER.info("NOT Deep Dark - spawn cancelled");
            return;
        }

        Catsafio.LOGGER.info("IS Deep Dark - attempting spawn");

        int groundY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                searchPos.getX(), searchPos.getZ());
        BlockPos spawnPos = new BlockPos(searchPos.getX(), groundY, searchPos.getZ());

        if (!level.getBlockState(spawnPos).isAir() ||
                !level.getBlockState(spawnPos.above()).isAir() ||
                !level.getBlockState(spawnPos.above(2)).isAir()) {
            Catsafio.LOGGER.info("No space at {} - spawn cancelled", spawnPos);
            return;
        }

        CreeperWardenEntity creeperWarden = ModEntities.CREEPER_WARDEN.get().create(level);
        if (creeperWarden != null) {
            creeperWarden.moveTo(
                    spawnPos.getX() + 0.5,
                    spawnPos.getY(),
                    spawnPos.getZ() + 0.5,
                    RANDOM.nextFloat() * 360F,
                    0
            );

            creeperWarden.finalizeSpawn(level, level.getCurrentDifficultyAt(spawnPos),
                    MobSpawnType.NATURAL, null, null);

            level.addFreshEntity(creeperWarden);

            Catsafio.LOGGER.info("✅ CREEPER WARDEN SPAWNED at {}", spawnPos);
        } else {
            Catsafio.LOGGER.error("❌ Failed to create Creeper Warden entity!");
        }
    }

    private static <T extends Mob> void spawnNearPlayer(ServerLevel level, BlockPos playerPos,
                                                        EntityType<T> entityType, int min, int max,
                                                        int minRadius, int maxRadius) {
        int radius = minRadius + RANDOM.nextInt(maxRadius - minRadius);
        double angle = RANDOM.nextDouble() * Math.PI * 2;

        int offsetX = (int)(Math.cos(angle) * radius);
        int offsetZ = (int)(Math.sin(angle) * radius);

        BlockPos spawnPos = new BlockPos(
                playerPos.getX() + offsetX,
                0,
                playerPos.getZ() + offsetZ
        );

        int groundY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                spawnPos.getX(), spawnPos.getZ());
        spawnPos = new BlockPos(spawnPos.getX(), groundY, spawnPos.getZ());

        if (!level.getBlockState(spawnPos).isAir() ||
                !level.getBlockState(spawnPos.above()).isAir()) {
            return;
        }

        int count = min + (max > min ? RANDOM.nextInt(max - min + 1) : 0);
        for (int i = 0; i < count; i++) {
            T entity = entityType.create(level);
            if (entity != null) {
                double varX = (RANDOM.nextDouble() - 0.5) * 4;
                double varZ = (RANDOM.nextDouble() - 0.5) * 4;

                entity.moveTo(
                        spawnPos.getX() + varX + 0.5,
                        spawnPos.getY(),
                        spawnPos.getZ() + varZ + 0.5,
                        RANDOM.nextFloat() * 360F,
                        0
                );

                entity.finalizeSpawn(level, level.getCurrentDifficultyAt(spawnPos),
                        MobSpawnType.NATURAL, null, null);

                level.addFreshEntity(entity);
            }
        }
    }

    private static String getEntityId(Mob mob) {
        if (mob instanceof FlashbangEntity) return "flashbang";
        if (mob instanceof SapoEntity) return "sapo";
        if (mob instanceof CreeperCakeEntity) return "creepercake";
        if (mob instanceof FantasmaEntity) return "fantasma";
        if (mob instanceof WildfireEntity) return "wildfire";
        if (mob instanceof MurcielagoWardenEntity) return "murcielago_warden";
        if (mob instanceof InfernalBullEntity) return "infernal_bull";
        if (mob instanceof TortugaInfernalEntity) return "tortuga_infernal";
        if (mob instanceof CreeperWardenEntity) return "creeper_warden";
        if (mob instanceof BombaAmarillaEntity) return "bomba_amarilla";
        return null;
    }
}