package net.lluis.catsafio.hub.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lluis.catsafio.hub.mission.Mission;
import net.lluis.catsafio.hub.mission.MissionRegistry;
import net.lluis.catsafio.hub.mission.MissionType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class MisionesScreen extends Screen {

    private static final int SLOT_SIZE = 48;
    private static final int COLS      = 8;
    private static final int GAP       = 8;
    private static final int ITEM_SIZE = 20;

    private int guiLeft, guiTop;
    private int hoveredSlot = -1;
    private MissionType activeTab = MissionType.DAILY;

    public MisionesScreen() {
        super(Component.literal("Misiones"));
    }

    @Override
    protected void init() {
        if (MissionRegistry.getAll().isEmpty()) MissionRegistry.init();
        int totalW = COLS * SLOT_SIZE + (COLS - 1) * GAP;
        guiLeft = (this.width - totalW) / 2;
        guiTop  = 80;
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float tick) {
        g.fill(0, 0, this.width, this.height, 0xCC000000);

        g.drawCenteredString(font, "§b§lMISIONES", this.width / 2, 12, 0xFFFFFFFF);

        int tabY = 35;
        int tabW = 90;
        int tabH = 22;
        int tabSpacing = 10;
        int startX = this.width / 2 - (tabW + tabSpacing / 2);

        drawTab(g, startX, tabY, tabW, tabH, "§d★ Diarias", MissionType.DAILY, mx, my);
        drawTab(g, startX + tabW + tabSpacing, tabY, tabW, tabH, "§e⚡ Entrenamiento", MissionType.TRAINING, mx, my);

        hoveredSlot = -1;
        int slotIndex = 0;

        for (Mission m : MissionRegistry.getByType(activeTab)) {
            if (!m.isEnabled()) continue;

            if (m.isMultiItem()) {
                for (int subIdx = 0; subIdx < m.getMultiTargets().size(); subIdx++) {
                    int col = slotIndex % COLS;
                    int row = slotIndex / COLS;
                    int x = guiLeft + col * (SLOT_SIZE + GAP);
                    int y = guiTop + row * (SLOT_SIZE + GAP);

                    boolean hovered = mx >= x && mx < x + SLOT_SIZE && my >= y && my < y + SLOT_SIZE;
                    if (hovered) hoveredSlot = slotIndex;

                    drawMultiItemSlot(g, x, y, m, subIdx, hovered);
                    slotIndex++;
                }
            } else {
                int col = slotIndex % COLS;
                int row = slotIndex / COLS;
                int x = guiLeft + col * (SLOT_SIZE + GAP);
                int y = guiTop + row * (SLOT_SIZE + GAP);

                boolean hovered = mx >= x && mx < x + SLOT_SIZE && my >= y && my < y + SLOT_SIZE;
                if (hovered) hoveredSlot = slotIndex;

                drawMissionSlot(g, x, y, m, hovered);
                slotIndex++;
            }
        }

        // Tooltip - CORREGIDO para no renderizar múltiples veces
        if (hoveredSlot >= 0) {
            slotIndex = 0;
            boolean tooltipRendered = false;

            for (Mission m : MissionRegistry.getByType(activeTab)) {
                if (!m.isEnabled() || tooltipRendered) continue;

                if (m.isMultiItem()) {
                    for (int subIdx = 0; subIdx < m.getMultiTargets().size(); subIdx++) {
                        if (slotIndex == hoveredSlot) {
                            renderMultiItemTooltip(g, m, subIdx, mx, my);
                            tooltipRendered = true;
                            break;
                        }
                        slotIndex++;
                    }
                } else {
                    if (slotIndex == hoveredSlot) {
                        renderTooltip(g, m, mx, my);
                        tooltipRendered = true;
                    }
                    slotIndex++;
                }
            }
        }

        BackButtonHelper.drawAndCheckHover(g, font, this.width, this.height, mx, my);
        super.render(g, mx, my, tick);
    }

    private void drawTab(GuiGraphics g, int x, int y, int w, int h, String label, MissionType type, int mx, int my) {
        boolean active = activeTab == type;
        boolean hovered = mx >= x && mx < x + w && my >= y && my < y + h;

        int bg = active ? 0xFF0F3460 : (hovered ? 0xFF2A2A3E : 0xFF1A1A2E);
        g.fill(x, y, x + w, y + h, bg);

        int borderColor = active ? 0xFF4FC3F7 : 0xFF333344;
        g.fill(x, y + h - 2, x + w, y + h, borderColor);

        int textX = x + (w - font.width(label)) / 2;
        int textY = y + (h - font.lineHeight) / 2;
        g.drawString(font, label, textX, textY, 0xFFFFFFFF, false);
    }

    private void drawMissionSlot(GuiGraphics g, int x, int y, Mission m, boolean hovered) {
        int cx = x + SLOT_SIZE / 2;
        int cy = y + SLOT_SIZE / 2;
        int r  = SLOT_SIZE / 2 - 1;

        int bg     = m.isCompleted() ? 0xFF0D2E0D : (hovered ? 0xFF4A4A4A : 0xFF333333);
        int border = m.isCompleted() ? 0xFF88EE55 : (hovered ? 0xFFCCCCCC : 0xFF666666);

        drawCircle(g, cx, cy, r, bg, border, 2);

        ItemStack stack = iconFor(m);
        if (!stack.isEmpty()) {
            renderItemScaled(g, stack, cx, cy, ITEM_SIZE);
        }

        if (m.isCompleted()) {
            g.drawString(font, "§a✔", x + SLOT_SIZE - 10, y + 2, 0xFF88EE55, true);
        }

        if (!m.isCompleted() && m.getProgress() > 0) {
            int bw = SLOT_SIZE - 8;
            int bx = x + 4;
            int by = y + SLOT_SIZE - 6;
            g.fill(bx, by, bx + bw, by + 3, 0xFF555555);
            g.fill(bx, by, bx + (int)(m.getPercent() * bw), by + 3, 0xFF55DD55);
        }
    }

    private void drawMultiItemSlot(GuiGraphics g, int x, int y, Mission m, int subIdx, boolean hovered) {
        int cx = x + SLOT_SIZE / 2;
        int cy = y + SLOT_SIZE / 2;
        int r  = SLOT_SIZE / 2 - 1;

        boolean subComplete = m.getMultiProgress().get(subIdx) > 0;
        boolean allComplete = m.isCompleted();

        int bg     = subComplete ? 0xFF0D2E0D : (hovered ? 0xFF4A4A4A : 0xFF333333);
        int border = allComplete ? 0xFF88EE55 : (subComplete ? 0xFF55AA55 : (hovered ? 0xFFCCCCCC : 0xFF666666));

        drawCircle(g, cx, cy, r, bg, border, 2);

        try {
            var item = ForgeRegistries.ITEMS.getValue(m.getMultiTargets().get(subIdx));
            if (item != null) {
                ItemStack stack = new ItemStack(item);
                renderItemScaled(g, stack, cx, cy, ITEM_SIZE);
            }
        } catch (Exception ignored) {}

        if (subComplete) {
            g.drawString(font, "§a✔", x + SLOT_SIZE - 10, y + 2, 0xFF88EE55, true);
        }
    }

    private void renderTooltip(GuiGraphics g, Mission m, int mx, int my) {
        List<Component> lines = new ArrayList<>();
        lines.add(Component.literal("§6§l" + m.getDisplayName()));
        lines.add(Component.literal(getTypeLabel(m.getType())));
        lines.add(Component.literal("§7" + m.getDescription()));
        lines.add(Component.literal(""));
        lines.add(Component.literal("§eProgreso: §f" + m.getProgress() + "/" + m.getGoal()));
        int bf = (int)(m.getPercent() * 20);
        lines.add(Component.literal("§a" + "█".repeat(bf) + "§8" + "█".repeat(20 - bf)));
        if (m.isCompleted()) lines.add(Component.literal("§a§l✔ ¡COMPLETADA!"));
        lines.add(Component.literal(""));
        lines.add(Component.literal("§bRecompensa: §f" + rewardText(m)));
        lines.add(Component.literal("§bXP: §f+" + m.getRewardXp()));
        g.renderComponentTooltip(font, lines, mx, my);
    }

    private void renderMultiItemTooltip(GuiGraphics g, Mission m, int subIdx, int mx, int my) {
        List<Component> lines = new ArrayList<>();
        lines.add(Component.literal("§6§l" + m.getDisplayName()));
        lines.add(Component.literal(""));

        // Nombre de la pieza específica
        String itemName = m.getMultiTargets().get(subIdx).getPath().replace("_", " ");
        itemName = itemName.substring(0, 1).toUpperCase() + itemName.substring(1);
        lines.add(Component.literal("§ePieza: §f" + itemName));

        boolean subComplete = m.getMultiProgress().get(subIdx) > 0;
        lines.add(Component.literal(subComplete ? "§a✔ Completada" : "§7✗ Pendiente"));
        lines.add(Component.literal(""));

        // Progreso total
        lines.add(Component.literal("§7Progreso total: §f" + m.getProgress() + "/" + m.getGoal()));

        // CORREGIDO: Mostrar recompensa siempre que la misión esté completada
        if (m.isCompleted()) {
            lines.add(Component.literal(""));
            lines.add(Component.literal("§a§l✔ ¡MISIÓN COMPLETADA!"));
            lines.add(Component.literal(""));
            lines.add(Component.literal("§bRecompensa: §f" + rewardText(m)));
            lines.add(Component.literal("§bXP: §f+" + m.getRewardXp()));
        }

        g.renderComponentTooltip(font, lines, mx, my);
    }

    private void renderItemScaled(GuiGraphics g, ItemStack stack, int cx, int cy, int targetSize) {
        float scale = targetSize / 16.0f;
        PoseStack ps = g.pose();
        ps.pushPose();
        ps.translate(cx - 8 * scale, cy - 8 * scale, 0);
        ps.scale(scale, scale, 1.0f);
        g.renderItem(stack, 0, 0);
        ps.popPose();
    }

    private void drawCircle(GuiGraphics g, int cx, int cy, int r, int fillColor, int borderColor, int borderT) {
        float innerFill  = (r - borderT - 0.5f) * (r - borderT - 0.5f);
        float outerFill  = (r + 0.5f) * (r + 0.5f);
        float innerBorder= (r - borderT + 0.5f) * (r - borderT + 0.5f);

        for (int dy = -r; dy <= r; dy++) {
            for (int dx = -r; dx <= r; dx++) {
                float d = dx * dx + dy * dy;
                if (d > outerFill) continue;
                int color = (d >= innerBorder) ? borderColor : fillColor;
                g.fill(cx + dx, cy + dy, cx + dx + 1, cy + dy + 1, color);
            }
        }
    }

    private ItemStack iconFor(Mission m) {
        // Casos especiales primero
        if (m.getId().equals("daily_kill_raid")) {
            // CORREGIDO: Usar Totem of Undying en lugar de poción
            return new ItemStack(Items.TOTEM_OF_UNDYING);
        }

        // Caso normal
        try {
            if (m.getType() == MissionType.CRAFT || m.getType() == MissionType.DAILY || m.getType() == MissionType.TRAINING) {
                if (m.getTarget() != null) {
                    var item = ForgeRegistries.ITEMS.getValue(m.getTarget());
                    if (item != null) return new ItemStack(item);
                }
            }
            if (!m.getRewardItem().isEmpty()) return m.getRewardItem().copy();
        } catch (Exception ignored) {}
        return ItemStack.EMPTY;
    }

    private String getTypeLabel(MissionType type) {
        return switch (type) {
            case CRAFT -> "§a⚒ Craftear";
            case KILL -> "§c☠ Matar";
            case TRAINING -> "§e⚡ Entrenamiento";
            case DAILY -> "§d★ Diaria";
        };
    }

    private String rewardText(Mission m) {
        if (m.getRewardItem().isEmpty()) return "Nada";
        return m.getRewardItem().getCount() + "x " + m.getRewardItem().getHoverName().getString();
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (button == 0) {
            int tabY = 35;
            int tabW = 90;
            int tabH = 22;
            int tabSpacing = 10;
            int startX = this.width / 2 - (tabW + tabSpacing / 2);

            if (my >= tabY && my < tabY + tabH) {
                if (mx >= startX && mx < startX + tabW) {
                    activeTab = MissionType.DAILY;
                    return true;
                }
                if (mx >= startX + tabW + tabSpacing && mx < startX + tabW * 2 + tabSpacing) {
                    activeTab = MissionType.TRAINING;
                    return true;
                }
            }

            if (BackButtonHelper.isClicked(this.width, this.height, mx, my)) {
                minecraft.setScreen(new MenuRadialScreen());
                return true;
            }
        }
        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}