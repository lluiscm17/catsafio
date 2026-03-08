package net.lluis.catsafio.hub.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lluis.catsafio.hub.data.HubData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.*;

public class ArbolTechScreen extends Screen {

    private static final String[] RECIPE_IDS = {
            "catsafio:motor_de_infernit_craft",
            "catsafio:infernit_smithing_template_duplicate",
            "catsafio:auralita_smithing_template_duplicate",
            "catsafio:rayolita_smithing_template_duplicate",
            "catsafio:auralita_boots_from_infernit_boots",
            "catsafio:auralita_chestplate_from_infernit_chestplate",
            "catsafio:auralita_helmet_from_infernit_helmet",
            "catsafio:auralita_leggins_from_infernit_leggins",
            "catsafio:auralita_raw_auralita",

            "catsafio:infernit_boots_from_rayolita_boots",
            "catsafio:infernit_chestplate_from_rayolita_chestplate",
            "catsafio:infernit_helmet_from_rayolita_helmet",
            "catsafio:infernit_leggins_from_rayolita_leggins",
            "catsafio:infernit_raw_infernit",
    };

    private static final int ICON_SIZE      = 36;
    private static final int ICONS_PER_ROW  = 9;
    private static final int SPACING        = 6;
    private static final int ITEM_ICON_SIZE = 12;
    private static final int SLOT_SIZE      = 18;

    // Colores tooltip
    private static final int COL_BG_OUTER  = 0xFF1A1A2E;
    private static final int COL_BG_INNER  = 0xFF16213E;
    private static final int COL_BORDER    = 0xFFFFAA00;
    private static final int COL_SLOT_BG   = 0xFF2D2D2D;
    private static final int COL_SLOT_EDGE = 0xFF555555;
    private static final int COL_RES_GLOW  = 0xFF3A3A1E;

    private int guiLeft, guiTop;
    private int hoveredIndex = -1;

    public ArbolTechScreen() {
        super(Component.literal("Árbol Tecnológico"));
    }

    @Override
    protected void init() {
        guiLeft = (this.width - (ICON_SIZE * ICONS_PER_ROW + SPACING * (ICONS_PER_ROW - 1))) / 2;
        guiTop  = 60;
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float tick) {
        this.renderBackground(g);
        g.drawCenteredString(font, "§9§lÁRBOL TECNOLÓGICO", this.width / 2, 20, 0xFFFFFFFF);

        hoveredIndex = -1;

        for (int i = 0; i < RECIPE_IDS.length; i++) {
            String recipeId = RECIPE_IDS[i];
            boolean unlocked = HubData.get().isRecipeUnlocked(recipeId);
            int row = i / ICONS_PER_ROW;
            int col = i % ICONS_PER_ROW;
            int x = guiLeft + col * (ICON_SIZE + SPACING);
            int y = guiTop  + row * (ICON_SIZE + SPACING);

            if (mx >= x && mx < x + ICON_SIZE && my >= y && my < y + ICON_SIZE) {
                hoveredIndex = i;
            }
            drawRecipeIcon(g, x, y, recipeId, unlocked, hoveredIndex == i);
        }

        BackButtonHelper.drawAndCheckHover(g, font, this.width, this.height, mx, my);
        super.render(g, mx, my, tick);

        // Tooltip AL FINAL para que quede encima de todo
        if (hoveredIndex >= 0) {
            String recipeId = RECIPE_IDS[hoveredIndex];
            boolean unlocked = HubData.get().isRecipeUnlocked(recipeId);
            renderRecipeTooltip(g, recipeId, unlocked, mx, my);
        }
    }

    private void drawRecipeIcon(GuiGraphics g, int x, int y, String recipeId,
                                boolean unlocked, boolean hovered) {
        int cx = x + ICON_SIZE / 2;
        int cy = y + ICON_SIZE / 2;
        int r  = ICON_SIZE / 2 - 1;
        int bg     = unlocked ? 0xFF0D2E0D : (hovered ? 0xFF4A4A4A : 0xFF333333);
        int border = unlocked ? 0xFF88EE55 : (hovered ? 0xFFCCCCCC : 0xFF666666);

        drawCircle(g, cx, cy, r, bg, border, 2);

        ItemStack result = getRecipeResult(recipeId);
        if (result != null && !result.isEmpty()) {
            renderItemScaled(g, result, cx, cy, ITEM_ICON_SIZE);
        } else {
            g.drawString(font, "?", cx - 2, cy - 4, 0xFF888888, false);
        }
        if (unlocked) {
            g.drawString(font, "§a✔", x + ICON_SIZE - 8, y + 1, 0xFF88EE55, true);
        }
    }

    private void renderRecipeTooltip(GuiGraphics g, String recipeId,
                                     boolean unlocked, int mx, int my) {
        if (!unlocked) {
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(Component.literal("§c[Bloqueada]"));
            tooltip.add(Component.literal("§7Desbloquea esta receta para verla."));
            g.renderComponentTooltip(font, tooltip, mx, my);
            return;
        }

        Recipe<?> recipe = getRecipe(recipeId);
        if (recipe == null) {
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(Component.literal("§7Receta no encontrada"));
            g.renderComponentTooltip(font, tooltip, mx, my);
            return;
        }

        int tooltipW = 3 * SLOT_SIZE + 8 + 20 + SLOT_SIZE + 12;
        int tooltipH = 3 * SLOT_SIZE + 22;

        int tx = mx + 14;
        int ty = my - 10;
        if (tx + tooltipW > this.width  - 4) tx = mx - tooltipW - 8;
        if (ty + tooltipH > this.height - 4) ty = this.height - tooltipH - 4;
        if (ty < 2) ty = 2;

        drawTooltipPanel(g, tx, ty, tooltipW, tooltipH);

        // Título
        ItemStack result = recipe.getResultItem(minecraft.level.registryAccess());
        String title = result.getHoverName().getString();
        if (title.length() > 16) title = title.substring(0, 13) + "...";
        g.drawString(font, "§6" + title, tx + 4, ty + 4, 0xFFFFFFFF, true);
        g.fill(tx + 3, ty + 14, tx + tooltipW - 3, ty + 15, COL_BORDER);

        // Grid 3x3
        NonNullList<Ingredient> grid = NonNullList.withSize(9, Ingredient.EMPTY);
        if (recipe instanceof ShapedRecipe shaped) {
            int rw = shaped.getWidth();
            int rh = shaped.getHeight();
            List<Ingredient> ing = shaped.getIngredients();
            for (int row = 0; row < rh; row++)
                for (int col = 0; col < rw; col++)
                    grid.set(row * 3 + col, ing.get(row * rw + col));
        } else {
            List<Ingredient> ing = recipe.getIngredients();
            for (int i = 0; i < Math.min(ing.size(), 9); i++)
                grid.set(i, ing.get(i));
        }

        int gridX = tx + 4;
        int gridY = ty + 17;
        int animTick = (int) (System.currentTimeMillis() / 1000);

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int sx = gridX + col * SLOT_SIZE;
                int sy = gridY + row * SLOT_SIZE;
                g.fill(sx,     sy,     sx + 16, sy + 16, COL_SLOT_BG);
                g.fill(sx,     sy,     sx + 16, sy + 1,  COL_SLOT_EDGE);
                g.fill(sx,     sy,     sx + 1,  sy + 16, COL_SLOT_EDGE);

                ItemStack[] stacks = grid.get(row * 3 + col).getItems();
                if (stacks.length > 0) {
                    ItemStack display = stacks[animTick % stacks.length];
                    g.renderItem(display, sx, sy);
                    g.renderItemDecorations(font, display, sx, sy);
                }
            }
        }

        // Flecha y resultado
        int arrowX = gridX + 3 * SLOT_SIZE + 2;
        int arrowY = gridY + SLOT_SIZE + 3;
        g.drawString(font, "→", arrowX, arrowY, COL_BORDER, false);

        int resX = arrowX + 14;
        int resY = gridY + SLOT_SIZE;
        g.fill(resX,      resY,      resX + 16, resY + 16, COL_RES_GLOW);
        g.fill(resX,      resY,      resX + 16, resY + 1,  COL_BORDER);
        g.fill(resX,      resY,      resX + 1,  resY + 16, COL_BORDER);
        g.fill(resX + 15, resY,      resX + 16, resY + 16, COL_BORDER);
        g.fill(resX,      resY + 15, resX + 16, resY + 16, COL_BORDER);
        g.renderItem(result, resX, resY);
        g.renderItemDecorations(font, result, resX, resY);
    }

    private void drawTooltipPanel(GuiGraphics g, int x, int y, int w, int h) {
        g.fill(x + 3, y + 3, x + w + 3, y + h + 3, 0x55000000);
        g.fill(x, y, x + w, y + h, COL_BG_OUTER);
        g.fill(x + 1, y + 1, x + w - 1, y + h - 1, COL_BG_INNER);
        g.fill(x,         y,         x + w,     y + 1,     COL_BORDER);
        g.fill(x,         y + h - 1, x + w,     y + h,     COL_BORDER);
        g.fill(x,         y,         x + 1,     y + h,     COL_BORDER);
        g.fill(x + w - 1, y,         x + w,     y + h,     COL_BORDER);
    }

    private void drawCircle(GuiGraphics g, int cx, int cy, int r,
                            int fillColor, int borderColor, int borderT) {
        float outerFill   = (r + 0.5f) * (r + 0.5f);
        float innerBorder = (r - borderT + 0.5f) * (r - borderT + 0.5f);
        for (int dy = -r; dy <= r; dy++)
            for (int dx = -r; dx <= r; dx++) {
                float d = dx * dx + dy * dy;
                if (d > outerFill) continue;
                g.fill(cx + dx, cy + dy, cx + dx + 1, cy + dy + 1,
                        d >= innerBorder ? borderColor : fillColor);
            }
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

    private ItemStack getRecipeResult(String recipeId) {
        Recipe<?> r = getRecipe(recipeId);
        return (r != null && minecraft.level != null)
                ? r.getResultItem(minecraft.level.registryAccess())
                : ItemStack.EMPTY;
    }

    private Recipe<?> getRecipe(String recipeId) {
        if (minecraft.level == null) return null;
        return minecraft.level.getRecipeManager()
                .byKey(new ResourceLocation(recipeId))
                .orElse(null);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (button == 0 && BackButtonHelper.isClicked(this.width, this.height, mx, my)) {
            minecraft.setScreen(new MenuRadialScreen());
            return true;
        }
        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}