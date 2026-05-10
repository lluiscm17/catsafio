package net.lluis.catsafio.roulette;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.lluis.catsafio.Catsafio;
import net.minecraft.server.MinecraftServer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouletteConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Map<String, RouletteData> configs = new HashMap<>();
    private static File configFile;

    public static void load(MinecraftServer server) {
        File worldDir = server.getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT).toFile();
        configFile = new File(worldDir, "roulettes.json");

        if (!configFile.exists()) {
            // No config file – create defaults and save them
            createDefaultConfig();
            save();
            Catsafio.LOGGER.info("Created default roulette config at {}", configFile.getAbsolutePath());
            return;
        }

        try (FileReader reader = new FileReader(configFile)) {
            // Parse JSON; may throw JsonSyntaxException if malformed
            RouletteConfigData data = GSON.fromJson(reader, RouletteConfigData.class);
            if (data != null && data.roulettes != null) {
                configs = data.roulettes;
                Catsafio.LOGGER.info("Loaded roulette configs from {}", configFile.getAbsolutePath());
            } else {
                Catsafio.LOGGER.warn("Roulette config empty or missing 'roulettes' field – regenerating defaults");
                createDefaultConfig();
                save();
            }
        } catch (com.google.gson.JsonSyntaxException e) {
            // Corrupted JSON – backup and recreate defaults
            Catsafio.LOGGER.error("Malformed roulette JSON in {} – resetting to defaults", configFile.getAbsolutePath(), e);
            try {
                java.nio.file.Files.move(configFile.toPath(),
                    new java.io.File(configFile.getParentFile(), "roulettes.json.corrupt").toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception me) {
                Catsafio.LOGGER.error("Failed to back up corrupted roulette config", me);
            }
            createDefaultConfig();
            save();
        } catch (Exception e) {
            Catsafio.LOGGER.error("Failed to load roulette configs", e);
            createDefaultConfig();
            save();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(configFile)) {
            RouletteConfigData data = new RouletteConfigData();
            data.roulettes = configs;
            GSON.toJson(data, writer);
        } catch (Exception e) {
            Catsafio.LOGGER.error("Failed to save roulette configs", e);
        }
    }

    private static void createDefaultConfig() {
        configs.clear();
        
        configs.put("azul", new RouletteData(
            "§bRuleta Azul ha terminado!",
            List.of("say Ruleta azul completada!")
        ));
        
        configs.put("amarilla", new RouletteData(
            "§eRuleta Amarilla ha terminado!",
            List.of("say Ruleta amarilla completada!")
        ));
        
        configs.put("verde", new RouletteData(
            "§aRuleta Verde ha terminado!",
            List.of("say Ruleta verde completada!")
        ));
        
        configs.put("naranja", new RouletteData(
            "§6Ruleta Naranja ha terminado!",
            List.of("say Ruleta naranja completada!")
        ));
        
        configs.put("turquesa", new RouletteData(
            "§3Ruleta Turquesa ha terminado!",
            List.of("say Ruleta turquesa completada!")
        ));
        
        configs.put("morada", new RouletteData(
            "§5Ruleta Morada ha terminado!",
            List.of("say Ruleta morada completada!")
        ));
        
        configs.put("rosada", new RouletteData(
            "§dRuleta Rosada ha terminado!",
            List.of("say Ruleta rosada completada!")
        ));
        configs.put("roja", new RouletteData(
            "§cRuleta Roja ha terminado!",
            List.of("say Ruleta roja completada!")
        ));
    }

    public static RouletteData get(String color) {
        return configs.getOrDefault(color, new RouletteData("Ruleta terminada!", new ArrayList<>()));
    }

    public static class RouletteConfigData {
        public Map<String, RouletteData> roulettes = new HashMap<>();
    }

    public static class RouletteData {
        public String message;
        public List<String> commands;

        public RouletteData() {}

        public RouletteData(String message, List<String> commands) {
            this.message = message;
            this.commands = commands;
        }
    }
}
