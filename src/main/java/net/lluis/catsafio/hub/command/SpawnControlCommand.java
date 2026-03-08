package net.lluis.catsafio.hub.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.lluis.catsafio.Catsafio;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Comando para activar/desactivar el spawn de entidades custom.
 * El estado se guarda automáticamente al cerrar el servidor.
 *
 * Uso:
 *   /spawncontrol disable <entidad>
 *   /spawncontrol enable  <entidad>
 *   /spawncontrol list
 */
@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpawnControlCommand {

    private static final Map<String, Boolean> SPAWN_ENABLED = new HashMap<>();
    private static final String SAVE_FILE_NAME = "catsafio_spawn_control.dat";

    static {
        // Valores por defecto - TODOS DESACTIVADOS por defecto
        // Solo entidades que tienen spawn natural
        SPAWN_ENABLED.put("creepercake",         false);
        SPAWN_ENABLED.put("sapo",                false);
        SPAWN_ENABLED.put("fantasma",            false);
        SPAWN_ENABLED.put("wildfire",            false);
        SPAWN_ENABLED.put("flashbang",           false);
        SPAWN_ENABLED.put("murcielago_warden",   false);
        SPAWN_ENABLED.put("infernal_bull",       false);
        SPAWN_ENABLED.put("tortuga_infernal",    false);
        SPAWN_ENABLED.put("creeper_warden",      false);
        SPAWN_ENABLED.put("bomba_amarilla",      false);
    }

    public static boolean isSpawnEnabled(String entityId) {
        return SPAWN_ENABLED.getOrDefault(entityId, false); // Por defecto: desactivado
    }

    /**
     * Cargar estado guardado al iniciar el servidor
     */
    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        loadSpawnStates(event.getServer());
    }

    /**
     * Guardar estado al cerrar el servidor
     */
    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        saveSpawnStates(event.getServer());
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("spawncontrol")
                .requires(src -> src.hasPermission(2))

                // /spawncontrol list
                .then(Commands.literal("list")
                        .executes(ctx -> {
                            StringBuilder sb = new StringBuilder("§6=== Spawn Control ===\n");
                            SPAWN_ENABLED.entrySet().stream()
                                    .sorted(Map.Entry.comparingByKey())
                                    .forEach(entry ->
                                            sb.append(entry.getValue() ? "§a● " : "§c○ ")
                                                    .append("§f").append(entry.getKey()).append("\n"));
                            ctx.getSource().sendSuccess(() -> Component.literal(sb.toString()), false);
                            return 1;
                        }))

                // /spawncontrol enable <entidad>
                .then(Commands.literal("enable")
                        .then(Commands.argument("entity", StringArgumentType.word())
                                .executes(ctx -> {
                                    String id = StringArgumentType.getString(ctx, "entity");
                                    if (!SPAWN_ENABLED.containsKey(id)) {
                                        ctx.getSource().sendFailure(Component.literal("§cEntidad desconocida: " + id));
                                        return 0;
                                    }
                                    SPAWN_ENABLED.put(id, true);
                                    saveSpawnStates(ctx.getSource().getServer());
                                    ctx.getSource().sendSuccess(() ->
                                            Component.literal("§aSpawn activado: §f" + id), true);
                                    return 1;
                                })))

                // /spawncontrol disable <entidad>
                .then(Commands.literal("disable")
                        .then(Commands.argument("entity", StringArgumentType.word())
                                .executes(ctx -> {
                                    String id = StringArgumentType.getString(ctx, "entity");
                                    if (!SPAWN_ENABLED.containsKey(id)) {
                                        ctx.getSource().sendFailure(Component.literal("§cEntidad desconocida: " + id));
                                        return 0;
                                    }
                                    SPAWN_ENABLED.put(id, false);
                                    saveSpawnStates(ctx.getSource().getServer());
                                    ctx.getSource().sendSuccess(() ->
                                            Component.literal("§cSpawn desactivado: §f" + id), true);
                                    return 1;
                                })))

                // /spawncontrol reset - Desactivar TODAS
                .then(Commands.literal("reset")
                        .executes(ctx -> {
                            SPAWN_ENABLED.replaceAll((k, v) -> false);
                            saveSpawnStates(ctx.getSource().getServer());
                            ctx.getSource().sendSuccess(() ->
                                    Component.literal("§e✓ Todos los spawns desactivados"), true);
                            return 1;
                        }))
        );
    }

    /**
     * Guardar estados de spawn en archivo
     */
    private static void saveSpawnStates(MinecraftServer server) {
        try {
            File worldDir = server.getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT).toFile();
            File saveFile = new File(worldDir, SAVE_FILE_NAME);

            CompoundTag tag = new CompoundTag();
            SPAWN_ENABLED.forEach(tag::putBoolean);

            NbtIo.writeCompressed(tag, saveFile);
            Catsafio.LOGGER.info("Spawn states saved: {}", saveFile.getAbsolutePath());
        } catch (IOException e) {
            Catsafio.LOGGER.error("Failed to save spawn states", e);
        }
    }

    /**
     * Cargar estados de spawn desde archivo
     */
    private static void loadSpawnStates(MinecraftServer server) {
        try {
            File worldDir = server.getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT).toFile();
            File saveFile = new File(worldDir, SAVE_FILE_NAME);

            if (!saveFile.exists()) {
                Catsafio.LOGGER.info("No spawn states file found, using defaults");
                return;
            }

            CompoundTag tag = NbtIo.readCompressed(saveFile);

            // Cargar estados guardados
            for (String key : SPAWN_ENABLED.keySet()) {
                if (tag.contains(key)) {
                    SPAWN_ENABLED.put(key, tag.getBoolean(key));
                }
            }

            Catsafio.LOGGER.info("Spawn states loaded: {}", saveFile.getAbsolutePath());

            // Log del estado cargado
            SPAWN_ENABLED.forEach((id, enabled) ->
                    Catsafio.LOGGER.info("  {} -> {}", id, enabled ? "ENABLED" : "DISABLED"));

        } catch (IOException e) {
            Catsafio.LOGGER.error("Failed to load spawn states", e);
        }
    }
}