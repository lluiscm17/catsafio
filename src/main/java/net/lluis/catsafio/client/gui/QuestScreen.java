package net.lluis.catsafio.client.gui;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.quest.Quest;
import net.lluis.catsafio.quest.QuestManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class QuestScreen extends Screen {

    // Textura principal del fondo
    private static final ResourceLocation BG_TEXTURE =
            new ResourceLocation(Catsafio.MOD_ID, "textures/gui/quest_menu.png");

    // Dimensiones del panel
    private static final int GUI_W = 320;
    private static final int GUI_H = 210;

    // Panel de misiones: lista izquierda + detalle derecho
    private static final int LIST_W   = 120;
    private static final int DETAIL_X = LIST_W + 10;

    private final List<Quest> quests;
    private int selectedIndex = 0;
    private int scrollOffset  = 0;
    private static final int VISIBLE_QUESTS = 7;

    // Posición top-left del panel (se calcula en init)
    private int guiLeft, guiTop;

    public QuestScreen() {
        super(Component.translatable("gui.catsafio.quest_menu"));
        this.quests = QuestManager.get().getAllQuests();
    }

    @Override
    protected void init() {
        guiLeft = (this.width  - GUI_W) / 2;
        guiTop  = (this.height - GUI_H) / 2;

        // Botón cerrar (X)
        this.addRenderableWidget(Button.builder(
                Component.literal("✕"),
                btn -> this.onClose()
        ).bounds(guiLeft + GUI_W - 18, guiTop + 4, 14, 14).build());

        // Scroll arriba
        this.addRenderableWidget(Button.builder(
                Component.literal("▲"),
                btn -> { if (scrollOffset > 0) scrollOffset--; }
        ).bounds(guiLeft + LIST_W - 14, guiTop + 24, 12, 12).build());

        // Scroll abajo
        this.addRenderableWidget(Button.builder(
                Component.literal("▼"),
                btn -> { if (scrollOffset < Math.max(0, quests.size() - VISIBLE_QUESTS)) scrollOffset++; }
        ).bounds(guiLeft + LIST_W - 14, guiTop + GUI_H - 24, 12, 12).build());

        // Botón RECLAMAR — siempre presente, se activa/desactiva según estado
        this.addRenderableWidget(Button.builder(
                Component.literal("✦ RECLAMAR ✦"),
                btn -> claimSelectedQuest()
        )
        .bounds(guiLeft + DETAIL_X, guiTop + GUI_H - 28, GUI_W - DETAIL_X - 6, 20)
        .build());
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float tick) {
        this.renderBackground(g);
        drawBackground(g);
        drawQuestList(g, mx, my);
        drawQuestDetail(g);

        // Activar/desactivar botón reclamar según el estado de la misión seleccionada
        if (!quests.isEmpty() && selectedIndex < quests.size()) {
            Quest selected = quests.get(selectedIndex);
            // El botón reclamar es el 4º widget añadido (índice 3)
            this.renderables.stream()
                .filter(r -> r instanceof Button)
                .map(r -> (Button) r)
                .filter(b -> b.getMessage().getString().contains("RECLAMAR"))
                .findFirst()
                .ifPresent(b -> b.active = selected.canClaim());
        }

        super.render(g, mx, my, tick);
        drawTitle(g);
    }

    // ── Fondo ─────────────────────────────────────────────────────
    private void drawBackground(GuiGraphics g) {
        // Fondo oscuro con bordes decorativos
        // Sombra exterior
        g.fill(guiLeft - 2, guiTop - 2, guiLeft + GUI_W + 2, guiTop + GUI_H + 2, 0xFF000000);
        // Fondo principal
        g.fill(guiLeft, guiTop, guiLeft + GUI_W, guiTop + GUI_H, 0xFF1A1A2E);
        // Barra de título
        g.fill(guiLeft, guiTop, guiLeft + GUI_W, guiTop + 20, 0xFF16213E);
        // Separador lista / detalle
        g.fill(guiLeft + LIST_W + 2, guiTop + 22, guiLeft + LIST_W + 3, guiTop + GUI_H - 4, 0xFF0F3460);
        // Borde exterior dorado
        drawBorder(g, guiLeft, guiTop, GUI_W, GUI_H, 0xFFE94560, 1);
        drawBorder(g, guiLeft + 2, guiTop + 2, GUI_W - 4, GUI_H - 4, 0xFF533483, 1);
    }

    private void drawBorder(GuiGraphics g, int x, int y, int w, int h, int color, int thickness) {
        g.fill(x, y, x + w, y + thickness, color);              // top
        g.fill(x, y + h - thickness, x + w, y + h, color);     // bottom
        g.fill(x, y, x + thickness, y + h, color);              // left
        g.fill(x + w - thickness, y, x + w, y + h, color);     // right
    }

    private void drawTitle(GuiGraphics g) {
        g.drawCenteredString(font, "§e✦ §fMISIONES §e✦", guiLeft + GUI_W / 2, guiTop + 6, 0xFFFFFF);
    }

    // ── Lista de misiones ─────────────────────────────────────────
    private void drawQuestList(GuiGraphics g, int mx, int my) {
        int startY = guiTop + 24;
        int itemH  = 26;

        for (int i = 0; i < VISIBLE_QUESTS; i++) {
            int index = scrollOffset + i;
            if (index >= quests.size()) break;

            Quest q = quests.get(index);
            int x = guiLeft + 4;
            int y = startY + i * itemH;
            int w = LIST_W - 8;

            // Detectar hover
            boolean hovered  = mx >= x && mx < x + w && my >= y && my < y + itemH - 2;
            boolean selected = index == selectedIndex;

            // Clic en elemento
            if (hovered && !selected) {
                // Efecto hover: fondo semitransparente
                g.fill(x, y, x + w, y + itemH - 2, 0x330F3460);
            }

            // Fondo del elemento seleccionado
            if (selected) {
                g.fill(x, y, x + w, y + itemH - 2, 0x880F3460);
                drawBorder(g, x, y, w, itemH - 2, 0xFFE94560, 1);
            }

            // Icono de estado
            String icon  = getStateIcon(q);
            int iconColor = getStateColor(q);
            g.drawString(font, icon, x + 3, y + 4, iconColor, false);

            // Nombre de la misión (truncado)
            String title = q.getTitle();
            if (font.width(title) > w - 18) {
                while (font.width(title + "…") > w - 18) title = title.substring(0, title.length() - 1);
                title += "…";
            }
            g.drawString(font, title, x + 14, y + 4, q.isLocked() ? 0xFF888888 : 0xFFEEEEEE, false);

            // Mini barra de progreso (solo si está en progreso)
            if (q.isInProgress()) {
                int barW  = w - 18;
                int barX  = x + 14;
                int barY  = y + 15;
                g.fill(barX, barY, barX + barW, barY + 3, 0xFF333355);
                g.fill(barX, barY, barX + (int)(barW * q.getProgressPercent()), barY + 3, 0xFF4CAF50);
            }
        }
    }

    // ── Detalle de la misión seleccionada ─────────────────────────
    private void drawQuestDetail(GuiGraphics g) {
        if (quests.isEmpty() || selectedIndex >= quests.size()) return;
        Quest q = quests.get(selectedIndex);

        int x = guiLeft + DETAIL_X;
        int y = guiTop + 24;
        int w = GUI_W - DETAIL_X - 6;

        // Título grande
        String stateLabel = switch (q.getState()) {
            case CLAIMED     -> "§6[★ RECLAMADA]";
            case COMPLETED   -> "§a[✔ COMPLETADA — ¡Reclama!]";
            case IN_PROGRESS -> "§e[◉ EN PROGRESO]";
            case LOCKED      -> "§7[🔒 BLOQUEADA]";
        };
        g.drawString(font, stateLabel, x, y, 0xFFFFFFFF, false);
        y += 12;

        // Nombre
        g.drawString(font, "§f§l" + q.getTitle(), x, y, 0xFFFFFFFF, false);
        y += 14;

        // Separador
        g.fill(x, y, x + w, y + 1, 0xFF533483);
        y += 5;

        // Descripción (con wrap)
        String desc = q.getDescription();
        for (String line : wrapText(desc, w)) {
            g.drawString(font, "§7" + line, x, y, 0xFFFFFFFF, false);
            y += 10;
        }
        y += 4;

        // Tipo de misión
        String typeLabel = q.getType() == Quest.Type.KILL
                ? "§c⚔ Objetivo: Matar " + q.getTargetAmount() + "x"
                : "§b⚒ Objetivo: Craftear " + q.getTargetAmount() + "x";
        g.drawString(font, typeLabel, x, y, 0xFFFFFFFF, false);
        y += 14;

        // Barra de progreso grande
        if (!q.isLocked()) {
            String progText = q.getProgress() + " / " + q.getTargetAmount();
            g.drawString(font, "§fProgreso: " + progText, x, y, 0xFFFFFFFF, false);
            y += 10;
            int barW = w;
            g.fill(x, y, x + barW, y + 6, 0xFF222240);
            int filled = (int)(barW * q.getProgressPercent());
            int barColor = q.isClaimed()    ? 0xFFFFD700 :  // Dorado = reclamada
                           q.isCompleted()  ? 0xFF4CAF50 :  // Verde  = completada
                                              0xFF2196F3;   // Azul   = en progreso
            g.fill(x, y, x + filled, y + 6, barColor);
            drawBorder(g, x, y, barW, 6, 0xFF533483, 1);
            y += 12;
        }

        // Separador recompensas
        y += 4;
        g.fill(x, y, x + w, y + 1, 0xFF533483);
        y += 5;
        g.drawString(font, "§eRecompensas:", x, y, 0xFFFFFFFF, false);
        y += 12;

        // Iconos de recompensa
        int rewardX = x;
        for (ItemStack reward : q.getRewards()) {
            g.renderItem(reward, rewardX, y);
            // Cantidad
            if (reward.getCount() > 1) {
                g.renderItemDecorations(font, reward, rewardX, y);
            }
            rewardX += 18;
        }
    }

    // ── Reclamar recompensa ───────────────────────────────────────
    private void claimSelectedQuest() {
        if (quests.isEmpty() || selectedIndex >= quests.size()) return;
        Quest q = quests.get(selectedIndex);
        if (!q.canClaim()) return;

        Player player = minecraft.player;
        if (player == null) return;

        // Dar cada item de recompensa al jugador
        for (ItemStack reward : q.getRewards()) {
            ItemStack copy = reward.copy();
            // Intentar añadir al inventario; si no cabe, tirar al suelo
            if (!player.getInventory().add(copy)) {
                player.drop(copy, false);
            }
        }

        // Marcar como reclamada
        q.setState(Quest.State.CLAIMED);
        QuestManager.get().saveProgress(player.getUUID());

        // Mensaje en el chat
        player.sendSystemMessage(Component.literal(
            "§a✦ ¡Misión '" + q.getTitle() + "' reclamada! Recompensas añadidas al inventario."
        ));
    }

    // ── Utilidades ────────────────────────────────────────────────
    private String getStateIcon(Quest q) {
        return switch (q.getState()) {
            case CLAIMED     -> "★";
            case COMPLETED   -> "✔";
            case IN_PROGRESS -> "◉";
            case LOCKED      -> "🔒";
        };
    }

    private int getStateColor(Quest q) {
        return switch (q.getState()) {
            case CLAIMED     -> 0xFFFFD700;  // Dorado
            case COMPLETED   -> 0xFF4CAF50;  // Verde
            case IN_PROGRESS -> 0xFFFFEB3B;  // Amarillo
            case LOCKED      -> 0xFF888888;  // Gris
        };
    }

    private List<String> wrapText(String text, int maxWidth) {
        List<String> lines = new java.util.ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder current = new StringBuilder();
        for (String word : words) {
            String test = current.isEmpty() ? word : current + " " + word;
            if (font.width(test) > maxWidth - 4) {
                if (!current.isEmpty()) lines.add(current.toString());
                current = new StringBuilder(word);
            } else {
                current = new StringBuilder(test);
            }
        }
        if (!current.isEmpty()) lines.add(current.toString());
        return lines;
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (button == 0) {
            int startY = guiTop + 24;
            int itemH  = 26;
            for (int i = 0; i < VISIBLE_QUESTS; i++) {
                int index = scrollOffset + i;
                if (index >= quests.size()) break;
                int y = startY + i * itemH;
                if (mx >= guiLeft + 4 && mx < guiLeft + LIST_W - 4
                        && my >= y && my < y + itemH - 2) {
                    selectedIndex = index;
                    return true;
                }
            }
        }
        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double delta) {
        if (delta < 0 && scrollOffset < Math.max(0, quests.size() - VISIBLE_QUESTS)) scrollOffset++;
        if (delta > 0 && scrollOffset > 0) scrollOffset--;
        return true;
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
