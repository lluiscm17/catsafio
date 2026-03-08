package net.lluis.catsafio.quest;

import com.google.gson.*;
import net.lluis.catsafio.Catsafio;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class QuestManager {

    private static final QuestManager INSTANCE = new QuestManager();
    public static QuestManager get() { return INSTANCE; }

    private final List<Quest> quests = new ArrayList<>();
    private Path questsFile;
    private Path saveFile;

    // ── Inicialización ────────────────────────────────────────────
    public void init(Path configDir) {
        questsFile = configDir.resolve(Catsafio.MOD_ID).resolve("quests.json");
        saveFile   = configDir.resolve(Catsafio.MOD_ID).resolve("quest_progress.json");
        createDefaultQuestsFileIfNeeded();
        loadQuests();
        loadProgress();
    }

    // ── Carga misiones desde quests.json ──────────────────────────
    private void loadQuests() {
        quests.clear();
        try {
            String json = Files.readString(questsFile);
            JsonArray arr = JsonParser.parseString(json).getAsJsonArray();
            for (JsonElement el : arr) {
                JsonObject obj = el.getAsJsonObject();
                String id          = obj.get("id").getAsString();
                String title       = obj.get("title").getAsString();
                String description = obj.get("description").getAsString();
                Quest.Type type    = Quest.Type.valueOf(obj.get("type").getAsString().toUpperCase());
                String targetId    = obj.get("target_id").getAsString();
                int targetAmount   = obj.get("target_amount").getAsInt();
                boolean locked     = obj.has("locked") && obj.get("locked").getAsBoolean();

                List<ItemStack> rewards = new ArrayList<>();
                if (obj.has("rewards")) {
                    for (JsonElement r : obj.getAsJsonArray("rewards")) {
                        JsonObject rObj = r.getAsJsonObject();
                        String itemId   = rObj.get("item").getAsString();
                        int count       = rObj.has("count") ? rObj.get("count").getAsInt() : 1;
                        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));
                        if (item != null) rewards.add(new ItemStack(item, count));
                    }
                }

                Quest.State initial = locked ? Quest.State.LOCKED : Quest.State.IN_PROGRESS;
                quests.add(new Quest(id, title, description, type, targetId, targetAmount, rewards, initial));
            }
        } catch (Exception e) {
            Catsafio.LOGGER.error("[Catsafio] Error cargando quests.json: {}", e.getMessage());
        }
    }

    // ── Guarda/carga progreso por jugador ─────────────────────────
    public void saveProgress(UUID playerUUID) {
        try {
            JsonObject root = new JsonObject();
            JsonObject playerData = new JsonObject();
            for (Quest q : quests) {
                JsonObject qData = new JsonObject();
                qData.addProperty("state",    q.getState().name());
                qData.addProperty("progress", q.getProgress());
                playerData.add(q.getId(), qData);
            }
            root.add(playerUUID.toString(), playerData);
            Files.writeString(saveFile, new GsonBuilder().setPrettyPrinting().create().toJson(root));
        } catch (Exception e) {
            Catsafio.LOGGER.error("[Catsafio] Error guardando progreso: {}", e.getMessage());
        }
    }

    public void loadProgress() {
        if (!Files.exists(saveFile)) return;
        try {
            String json = Files.readString(saveFile);
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            // Cargar primer jugador encontrado (en singleplayer siempre es uno)
            if (root.size() > 0) {
                JsonObject playerData = root.entrySet().iterator().next().getValue().getAsJsonObject();
                for (Quest q : quests) {
                    if (playerData.has(q.getId())) {
                        JsonObject qData = playerData.getAsJsonObject(q.getId());
                        q.setState(Quest.State.valueOf(qData.get("state").getAsString()));
                        q.setProgress(qData.get("progress").getAsInt());
                    }
                }
            }
        } catch (Exception e) {
            Catsafio.LOGGER.error("[Catsafio] Error cargando progreso: {}", e.getMessage());
        }
    }

    // ── API pública ───────────────────────────────────────────────
    public List<Quest> getAllQuests()         { return Collections.unmodifiableList(quests); }

    public Optional<Quest> getById(String id) {
        return quests.stream().filter(q -> q.getId().equals(id)).findFirst();
    }

    /** Llamado cuando el jugador mata un mob */
    public void onKill(String mobId) {
        for (Quest q : quests) {
            if (q.getType() == Quest.Type.KILL
                    && q.isInProgress()
                    && q.getTargetId().equals(mobId)) {
                q.incrementProgress(1);
            }
        }
    }

    /** Llamado cuando el jugador craftea un item */
    public void onCraft(String itemId) {
        for (Quest q : quests) {
            if (q.getType() == Quest.Type.CRAFT
                    && q.isInProgress()
                    && q.getTargetId().equals(itemId)) {
                q.incrementProgress(1);
            }
        }
    }

    /** Comando: /questadmin unlock <id> */
    public boolean unlockQuest(String id) {
        return getById(id).map(q -> {
            if (q.isLocked()) { q.setState(Quest.State.IN_PROGRESS); return true; }
            return false;
        }).orElse(false);
    }

    /** Comando: /questadmin lock <id> */
    public boolean lockQuest(String id) {
        return getById(id).map(q -> {
            if (!q.isLocked()) { q.setState(Quest.State.LOCKED); return true; }
            return false;
        }).orElse(false);
    }

    /** Comando: /questadmin complete <id> */
    public boolean completeQuest(String id) {
        return getById(id).map(q -> {
            q.setState(Quest.State.COMPLETED);
            q.setProgress(q.getTargetAmount());
            return true;
        }).orElse(false);
    }

    /** Comando: /questadmin reset <id> */
    public boolean resetQuest(String id) {
        return getById(id).map(q -> {
            q.setState(Quest.State.IN_PROGRESS);
            q.setProgress(0);
            return true;
        }).orElse(false);
    }

    // ── Archivo por defecto ───────────────────────────────────────
    private void createDefaultQuestsFileIfNeeded() {
        if (Files.exists(questsFile)) return;
        try {
            Files.createDirectories(questsFile.getParent());
            String defaultJson = """
[
  {
    "id": "kill_zombie",
    "title": "Exterminador de Zombies",
    "description": "Mata 10 zombies",
    "type": "KILL",
    "target_id": "minecraft:zombie",
    "target_amount": 10,
    "locked": false,
    "rewards": [
      { "item": "minecraft:diamond", "count": 3 }
    ]
  },
  {
    "id": "kill_skeleton",
    "title": "Cazador de Esqueletos",
    "description": "Mata 5 esqueletos",
    "type": "KILL",
    "target_id": "minecraft:skeleton",
    "target_amount": 5,
    "locked": false,
    "rewards": [
      { "item": "minecraft:arrow", "count": 16 }
    ]
  },
  {
    "id": "craft_sword",
    "title": "Forjador",
    "description": "Craftea una espada de hierro",
    "type": "CRAFT",
    "target_id": "minecraft:iron_sword",
    "target_amount": 1,
    "locked": true,
    "rewards": [
      { "item": "minecraft:iron_ingot", "count": 5 }
    ]
  },
  {
    "id": "kill_infernal_bull",
    "title": "Torero Infernal",
    "description": "Mata 3 toros infernales",
    "type": "KILL",
    "target_id": "catsafio:infernal_bull",
    "target_amount": 3,
    "locked": true,
    "rewards": [
      { "item": "catsafio:infernit", "count": 5 }
    ]
  }
]
""";
            Files.writeString(questsFile, defaultJson);
        } catch (Exception e) {
            Catsafio.LOGGER.error("[Catsafio] Error creando quests.json por defecto: {}", e.getMessage());
        }
    }
}
