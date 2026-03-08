package net.lluis.catsafio.hub.mission;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una misión individual.
 * Soporta misiones multi-item para casos como "craftear set completo de diamante"
 */
public class Mission {

    private final String  id;
    private final String  displayName;
    private final String  description;
    private final MissionType type;
    private final ResourceLocation target; // Item o mob principal
    private final int     goal;
    private final ItemStack rewardItem;
    private final int     rewardXp;

    // Para misiones multi-item (ej: armadura completa)
    private final List<ResourceLocation> multiTargets;
    private final List<Integer> multiProgress;

    private boolean enabled  = true;
    private int     progress = 0;
    private boolean completed = false;

    // Constructor normal (un solo target)
    public Mission(String id, String displayName, String description,
                   MissionType type, ResourceLocation target, int goal,
                   ItemStack rewardItem, int rewardXp) {
        this.id          = id;
        this.displayName = displayName;
        this.description = description;
        this.type        = type;
        this.target      = target;
        this.goal        = goal;
        this.rewardItem  = rewardItem;
        this.rewardXp    = rewardXp;
        this.multiTargets = new ArrayList<>();
        this.multiProgress = new ArrayList<>();
    }

    // Constructor para misiones multi-item
    public Mission(String id, String displayName, String description,
                   MissionType type, List<ResourceLocation> targets,
                   ItemStack rewardItem, int rewardXp) {
        this.id          = id;
        this.displayName = displayName;
        this.description = description;
        this.type        = type;
        this.target      = targets.isEmpty() ? null : targets.get(0); // primer item como icono
        this.goal        = targets.size(); // completar todos los items
        this.rewardItem  = rewardItem;
        this.rewardXp    = rewardXp;
        this.multiTargets = new ArrayList<>(targets);
        this.multiProgress = new ArrayList<>();
        for (int i = 0; i < targets.size(); i++) {
            this.multiProgress.add(0);
        }
    }

    // Getters
    public String          getId()          { return id; }
    public String          getDisplayName() { return displayName; }
    public String          getDescription() { return description; }
    public MissionType     getType()        { return type; }
    public ResourceLocation getTarget()     { return target; }
    public int             getGoal()        { return goal; }
    public ItemStack       getRewardItem()  { return rewardItem; }
    public int             getRewardXp()    { return rewardXp; }
    public boolean         isEnabled()      { return enabled; }
    public int             getProgress()    { return progress; }
    public boolean         isCompleted()    { return completed; }
    public boolean         isMultiItem()    { return !multiTargets.isEmpty(); }
    public List<ResourceLocation> getMultiTargets() { return multiTargets; }
    public List<Integer>   getMultiProgress()       { return multiProgress; }

    public void setEnabled(boolean v)    { this.enabled   = v; }
    public void setProgress(int v)       { this.progress  = Math.min(v, goal); }
    public void addProgress(int v)       { setProgress(this.progress + v); }
    public void setCompleted(boolean v)  { this.completed = v; }

    // Para misiones multi-item: marcar un subitem como completado
    public void completeSubItem(int index) {
        if (index >= 0 && index < multiProgress.size()) {
            multiProgress.set(index, 1);
            // Recalcular progreso total
            int completed = 0;
            for (int p : multiProgress) {
                if (p > 0) completed++;
            }
            this.progress = completed;
        }
    }

    // Verificar si un target específico ya está completo
    public boolean isSubItemComplete(ResourceLocation target) {
        int index = multiTargets.indexOf(target);
        if (index >= 0 && index < multiProgress.size()) {
            return multiProgress.get(index) > 0;
        }
        return false;
    }

    public float getPercent() {
        return goal <= 0 ? 0f : Math.min(1f, (float) progress / goal);
    }

    public boolean isFinished() {
        return progress >= goal;
    }
}