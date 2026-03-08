package net.lluis.catsafio.quest;

import net.minecraft.world.item.ItemStack;
import java.util.List;

public class Quest {

    public enum Type { KILL, CRAFT }
    public enum State { LOCKED, IN_PROGRESS, COMPLETED, CLAIMED }

    private final String id;
    private final String title;
    private final String description;
    private final Type type;
    private final String targetId;   // mob ResourceLocation o item ResourceLocation
    private final int targetAmount;
    private final List<ItemStack> rewards;
    private State state;
    private int progress;

    public Quest(String id, String title, String description,
                 Type type, String targetId, int targetAmount,
                 List<ItemStack> rewards, State initialState) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.targetId = targetId;
        this.targetAmount = targetAmount;
        this.rewards = rewards;
        this.state = initialState;
        this.progress = 0;
    }

    // ── Getters ──────────────────────────────────────────────────
    public String getId()           { return id; }
    public String getTitle()        { return title; }
    public String getDescription()  { return description; }
    public Type   getType()         { return type; }
    public String getTargetId()     { return targetId; }
    public int    getTargetAmount() { return targetAmount; }
    public List<ItemStack> getRewards() { return rewards; }
    public State  getState()        { return state; }
    public int    getProgress()     { return progress; }

    // ── Setters ──────────────────────────────────────────────────
    public void setState(State state)   { this.state = state; }
    public void setProgress(int amount) { this.progress = Math.min(amount, targetAmount); }

    public void incrementProgress(int amount) {
        if (state != State.IN_PROGRESS) return;
        progress = Math.min(progress + amount, targetAmount);
        if (progress >= targetAmount) state = State.COMPLETED;
    }

    public float getProgressPercent() {
        return targetAmount > 0 ? (float) progress / targetAmount : 0f;
    }

    public boolean isCompleted()   { return state == State.COMPLETED; }
    public boolean isLocked()      { return state == State.LOCKED; }
    public boolean isInProgress()  { return state == State.IN_PROGRESS; }
    public boolean isClaimed()     { return state == State.CLAIMED; }
    public boolean canClaim()      { return state == State.COMPLETED; }
}
