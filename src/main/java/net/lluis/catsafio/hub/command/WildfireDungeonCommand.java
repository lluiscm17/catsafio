package net.lluis.catsafio.hub.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.lluis.catsafio.entity.ModEntities;
import net.lluis.catsafio.entity.custom.WildfireEntity;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Optional;

public class WildfireDungeonCommand {

    // Número de Wildfires que spawnean al generar la dungeon
    private static final int WILDFIRE_COUNT = 4;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("wildfiredungeon")
            .requires(src -> src.hasPermission(2))
            .executes(ctx -> spawnDungeon(ctx.getSource(), 0))
            .then(Commands.argument("wildfires", IntegerArgumentType.integer(1, 20))
                .executes(ctx -> spawnDungeon(
                    ctx.getSource(),
                    IntegerArgumentType.getInteger(ctx, "wildfires"))))
        );
    }

    private static int spawnDungeon(CommandSourceStack source, int wildfireCount) {
        ServerLevel level = source.getLevel();
        BlockPos center = BlockPos.containing(source.getPosition());

        // ── Cargar y colocar la estructura NBT ────────────────────────
        StructureTemplateManager manager = level.getStructureManager();
        ResourceLocation structureId = new ResourceLocation("catsafio", "wildfire_dungeon");
        Optional<StructureTemplate> templateOpt = manager.get(structureId);

        if (templateOpt.isEmpty()) {
            source.sendFailure(Component.literal(
                "§cNo se encontró la estructura 'wildfire_dungeon'. " +
                "Asegúrate de que existe en: " +
                "data/catsafio/structures/wildfire_dungeon.nbt"));
            return 0;
        }

        StructureTemplate template = templateOpt.get();
        StructurePlaceSettings settings = new StructurePlaceSettings();

        // Colocar la estructura centrada en la posición del jugador
        template.placeInWorld(level, center, center, settings,
            level.getRandom(), 2);

        // ── Spawnar Wildfires distribuidos por la dungeon ──────────────
        int count = wildfireCount > 0 ? wildfireCount : WILDFIRE_COUNT;
        spawnWildfires(level, center, count, template);

        source.sendSuccess(() -> Component.literal(
            "§a✔ Dungeon Wildfire generada en " +
            center.getX() + ", " + center.getY() + ", " + center.getZ() +
            " con §c" + count + " Wildfires§a."), true);

        return 1;
    }

    private static void spawnWildfires(ServerLevel level, BlockPos center,
                                        int count, StructureTemplate template) {
        // Distribuir Wildfires por la estructura
        int sizeX = template.getSize().getX();
        int sizeZ = template.getSize().getZ();

        for (int i = 0; i < count; i++) {
            // Posición aleatoria dentro de la estructura
            double angle = (2 * Math.PI / count) * i;
            double radius = Math.min(sizeX, sizeZ) * 0.3;

            int wx = center.getX() + (int)(Math.cos(angle) * radius);
            int wy = center.getY() + 3; // 3 bloques sobre el suelo
            int wz = center.getZ() + (int)(Math.sin(angle) * radius);

            WildfireEntity wildfire = ModEntities.WILDFIRE.get()
                .create(level);
            if (wildfire != null) {
                wildfire.moveTo(wx, wy, wz, 0, 0);
                wildfire.finalizeSpawn(level,
                    level.getCurrentDifficultyAt(new BlockPos(wx, wy, wz)),
                    net.minecraft.world.entity.MobSpawnType.COMMAND,
                    null, null);
                level.addFreshEntity(wildfire);
            }
        }
    }
}
