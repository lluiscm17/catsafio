package net.lluis.catsafio.hub.data;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.hub.mission.Mission;
import net.lluis.catsafio.hub.mission.MissionRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

/**
 * Sistema de guardado de misiones POR MUNDO.
 * Cada mundo (save) tiene su propio progreso de misiones independiente.
 */
public class WorldMissionData extends SavedData {
    
    private static final String DATA_NAME = "catsafio_world_missions";
    
    public WorldMissionData() {
        super();
    }

    public static WorldMissionData get(Level level) {
        if (level.isClientSide()) {
            throw new RuntimeException("Don't access this on the client!");
        }

        DimensionDataStorage storage = level.getServer().overworld().getDataStorage();
        return storage.computeIfAbsent(WorldMissionData::load, WorldMissionData::new, DATA_NAME);
    }

    public static WorldMissionData load(CompoundTag tag) {
        WorldMissionData data = new WorldMissionData();
        data.loadMissionStates(tag);
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        saveMissionStates(tag);
        return tag;
    }

    private void saveMissionStates(CompoundTag tag) {
        for (Mission mission : MissionRegistry.getAll()) {
            String id = mission.getId();
            tag.putInt(id + "_progress", mission.getProgress());
            tag.putBoolean(id + "_completed", mission.isCompleted());
            
            // Guardar progreso multi-item
            if (mission.isMultiItem()) {
                CompoundTag multiTag = new CompoundTag();
                for (int i = 0; i < mission.getMultiProgress().size(); i++) {
                    multiTag.putInt("item_" + i, mission.getMultiProgress().get(i));
                }
                tag.put(id + "_multi", multiTag);
            }
        }
        Catsafio.LOGGER.info("Saved mission states for this world");
    }

    private void loadMissionStates(CompoundTag tag) {
        for (Mission mission : MissionRegistry.getAll()) {
            String id = mission.getId();
            
            if (tag.contains(id + "_progress")) {
                int progress = tag.getInt(id + "_progress");
                boolean completed = tag.getBoolean(id + "_completed");
                
                // Restaurar progreso
                mission.setProgress(progress);
                mission.setCompleted(completed);
                
                // Restaurar progreso multi-item
                if (mission.isMultiItem() && tag.contains(id + "_multi")) {
                    CompoundTag multiTag = tag.getCompound(id + "_multi");
                    for (int i = 0; i < mission.getMultiProgress().size(); i++) {
                        if (multiTag.contains("item_" + i)) {
                            mission.getMultiProgress().set(i, multiTag.getInt("item_" + i));
                        }
                    }
                }
            }
        }
        Catsafio.LOGGER.info("Loaded mission states for this world");
    }

    public void saveMissionProgress(String missionId, int progress, boolean completed) {
        Mission mission = MissionRegistry.get(missionId);
        if (mission != null) {
            mission.setProgress(progress);
            mission.setCompleted(completed);
            setDirty(); // Marca que hay cambios para guardar
        }
    }
}
