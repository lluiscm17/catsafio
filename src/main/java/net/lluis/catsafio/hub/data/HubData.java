package net.lluis.catsafio.hub.data;

import com.google.gson.*;
import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.hub.mission.Mission;
import net.lluis.catsafio.hub.mission.MissionRegistry;

import java.nio.file.*;
import java.util.*;

public class HubData {

    private static final HubData INSTANCE = new HubData();
    public static HubData get() { return INSTANCE; }

    // ── Datos ──────────────────────────────────────────────────────────
    private final Set<String>         unlockedMobs     = new HashSet<>();
    private final Set<String>         unlockedRecipes  = new HashSet<>();
    private final Map<String,Integer> missionProgress  = new HashMap<>();
    private final Set<String>         missionCompleted = new HashSet<>();
    private final Set<String>         missionDisabled  = new HashSet<>();
    private String       dificultadText = "Pasteles en la mesa\n\nAhora los creeper cakes spawnean encima de la mesa de crafteo.";
    private final List<String> notificaciones = new ArrayList<>();

    private Path dataFile;

    public void init(Path configDir) {
        dataFile = configDir.resolve(Catsafio.MOD_ID).resolve("hub_data.json");
        load();
    }

    // ── Catpedia ───────────────────────────────────────────────────────
    public boolean isMobUnlocked(String mobId)  { return unlockedMobs.contains(mobId); }
    public void unlockMob(String mobId)          { unlockedMobs.add(mobId);    save(); }
    public void lockMob(String mobId)            { unlockedMobs.remove(mobId); save(); }
    public Set<String> getUnlockedMobs()         { return Collections.unmodifiableSet(unlockedMobs); }

    // ── Árbol Tech ─────────────────────────────────────────────────────
    public boolean isRecipeUnlocked(String id)  { return unlockedRecipes.contains(id); }
    public void unlockRecipe(String id)          { unlockedRecipes.add(id);    save(); }
    public void lockRecipe(String id)            { unlockedRecipes.remove(id); save(); }
    public Set<String> getUnlockedRecipes()      { return Collections.unmodifiableSet(unlockedRecipes); }

    // ── Textos ─────────────────────────────────────────────────────────
    public String getDificultadText()            { return dificultadText; }
    public void setDificultadText(String text)   { this.dificultadText = text; save(); }
    public List<String> getNotificaciones()      { return Collections.unmodifiableList(notificaciones); }

    public void addNotificacion(String text) {
        notificaciones.add(0, text);
        if (notificaciones.size() > 10) notificaciones.remove(notificaciones.size() - 1);
        save();
    }

    public void clearNotificaciones() { notificaciones.clear(); save(); }

    // ── Misiones ───────────────────────────────────────────────────────
    public void saveMissionProgress(String id, int progress, boolean completed) {
        missionProgress.put(id, progress);
        if (completed) missionCompleted.add(id);
        else           missionCompleted.remove(id);
        save();
    }

    public int     getMissionProgress(String id)  { return missionProgress.getOrDefault(id, 0); }
    public boolean isMissionCompleted(String id)  { return missionCompleted.contains(id); }

    public void saveMissionEnabled(String id, boolean enabled) {
        if (enabled) missionDisabled.remove(id);
        else         missionDisabled.add(id);
        save();
    }

    public boolean isMissionEnabled(String id) { return !missionDisabled.contains(id); }

    /** Aplica el estado guardado a todas las misiones del registro */
    public void loadMissionStates() {
        for (Mission m : MissionRegistry.getAll()) {
            m.setProgress(getMissionProgress(m.getId()));
            m.setCompleted(isMissionCompleted(m.getId()));
            m.setEnabled(isMissionEnabled(m.getId()));
        }
    }

    // ── Persistencia ───────────────────────────────────────────────────
    public void save() {
        if (dataFile == null) return;
        try {
            Files.createDirectories(dataFile.getParent());
            JsonObject root = new JsonObject();

            // Catpedia
            JsonArray mobsArr = new JsonArray();
            unlockedMobs.forEach(mobsArr::add);
            root.add("unlocked_mobs", mobsArr);

            // Árbol Tech
            JsonArray recipesArr = new JsonArray();
            unlockedRecipes.forEach(recipesArr::add);
            root.add("unlocked_recipes", recipesArr);

            // Textos
            root.addProperty("dificultad_text", dificultadText);
            JsonArray notifsArr = new JsonArray();
            notificaciones.forEach(notifsArr::add);
            root.add("notificaciones", notifsArr);

            // Misiones - progreso
            JsonObject missionObj = new JsonObject();
            missionProgress.forEach((id, prog) -> {
                JsonObject entry = new JsonObject();
                entry.addProperty("progress", prog);
                entry.addProperty("completed", missionCompleted.contains(id));
                entry.addProperty("disabled", missionDisabled.contains(id));
                missionObj.add(id, entry);
            });
            // Guardar también las desactivadas aunque no tengan progreso
            for (String id : missionDisabled) {
                if (!missionObj.has(id)) {
                    JsonObject entry = new JsonObject();
                    entry.addProperty("progress", 0);
                    entry.addProperty("completed", false);
                    entry.addProperty("disabled", true);
                    missionObj.add(id, entry);
                }
            }
            root.add("missions", missionObj);

            Files.writeString(dataFile, new GsonBuilder().setPrettyPrinting().create().toJson(root));
        } catch (Exception e) {
            Catsafio.LOGGER.error("[Catsafio] Error guardando hub_data.json: {}", e.getMessage());
        }
    }

    public void load() {
        if (dataFile == null || !Files.exists(dataFile)) return;
        try {
            JsonObject root = JsonParser.parseString(Files.readString(dataFile)).getAsJsonObject();

            if (root.has("unlocked_mobs"))
                root.getAsJsonArray("unlocked_mobs").forEach(e -> unlockedMobs.add(e.getAsString()));

            if (root.has("unlocked_recipes"))
                root.getAsJsonArray("unlocked_recipes").forEach(e -> unlockedRecipes.add(e.getAsString()));

            if (root.has("dificultad_text"))
                dificultadText = root.get("dificultad_text").getAsString();

            if (root.has("notificaciones")) {
                notificaciones.clear();
                root.getAsJsonArray("notificaciones").forEach(e -> notificaciones.add(e.getAsString()));
            }

            if (root.has("missions")) {
                JsonObject mObj = root.getAsJsonObject("missions");
                for (Map.Entry<String, JsonElement> entry : mObj.entrySet()) {
                    String     id   = entry.getKey();
                    JsonObject data = entry.getValue().getAsJsonObject();
                    missionProgress.put(id, data.has("progress")  ? data.get("progress").getAsInt()     : 0);
                    if (data.has("completed") && data.get("completed").getAsBoolean()) missionCompleted.add(id);
                    if (data.has("disabled")  && data.get("disabled").getAsBoolean())  missionDisabled.add(id);
                }
            }
        } catch (Exception e) {
            Catsafio.LOGGER.error("[Catsafio] Error cargando hub_data.json: {}", e.getMessage());
        }
    }
}