package net.lluis.catsafio.screen.orb;

import net.lluis.catsafio.network.OrbPackets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class OrbScreen extends Screen {

    private static final int W = 280;
    private static final int H = 220;

    private final int entityId;
    private String selectedMob;
    private int interval;

    // Lista de mobs disponibles (mod + vanilla)
    private final List<String> MOB_LIST = new ArrayList<>();
    private int scrollOffset = 0;
    private static final int VISIBLE_ROWS = 7;
    private static final int ROW_H = 18;

    private EditBox searchBox;
    private EditBox intervalBox;
    private List<String> filteredList = new ArrayList<>();

    private OrbScreen(int entityId, String selectedMob, int interval) {
        super(Component.literal("Configurar Orb"));
        this.entityId    = entityId;
        this.selectedMob = selectedMob;
        this.interval    = interval;
        buildMobList();
    }

    public static void open(int entityId, String selectedMob, int interval) {
        Minecraft.getInstance().setScreen(
            new OrbScreen(entityId, selectedMob, interval));
    }

    private void buildMobList() {
        MOB_LIST.clear();
        // Todos los entity types registrados que sean mobs
        ForgeRegistries.ENTITY_TYPES.getValues().stream()
            .filter(t -> {
                try {
                    var e = t.create(Minecraft.getInstance().level);
                    boolean isMob = e instanceof net.minecraft.world.entity.Mob;
                    if (e != null) e.discard();
                    return isMob;
                } catch (Exception ex) { return false; }
            })
            .map(t -> ForgeRegistries.ENTITY_TYPES.getKey(t).toString())
            .sorted()
            .forEach(MOB_LIST::add);
        filteredList = new ArrayList<>(MOB_LIST);
    }

    @Override
    protected void init() {
        int x = (this.width  - W) / 2;
        int y = (this.height - H) / 2;

        // Buscador
        searchBox = new EditBox(this.font, x + 8, y + 24, W - 16, 16,
            Component.literal("Buscar mob..."));
        searchBox.setHint(Component.literal("Buscar..."));
        searchBox.setResponder(text -> {
            filteredList = MOB_LIST.stream()
                .filter(m -> m.toLowerCase().contains(text.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());
            scrollOffset = 0;
        });
        this.addRenderableWidget(searchBox);

        // Intervalo
        intervalBox = new EditBox(this.font, x + 100, y + H - 36, 60, 16,
            Component.literal("Intervalo"));
        intervalBox.setValue(String.valueOf(interval));
        intervalBox.setFilter(s -> s.matches("\\d*"));
        this.addRenderableWidget(intervalBox);

        // Botón Guardar
        this.addRenderableWidget(Button.builder(
            Component.literal("§aGuardar"),
            btn -> save())
            .pos(x + W - 68, y + H - 38).size(60, 20).build());

        // Scroll arriba/abajo
        this.addRenderableWidget(Button.builder(
            Component.literal("▲"),
            btn -> { if (scrollOffset > 0) scrollOffset--; })
            .pos(x + W - 18, y + 44).size(14, 14).build());

        this.addRenderableWidget(Button.builder(
            Component.literal("▼"),
            btn -> { if (scrollOffset < filteredList.size() - VISIBLE_ROWS) scrollOffset++; })
            .pos(x + W - 18, y + 44 + VISIBLE_ROWS * ROW_H - 14).size(14, 14).build());
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float delta) {
        this.renderBackground(g);
        int x = (this.width  - W) / 2;
        int y = (this.height - H) / 2;

        // Fondo
        g.fill(x, y, x + W, y + H, 0xCC1A1A2E);
        g.fill(x, y, x + W, y + 1, 0xFF6644FF);
        g.fill(x, y + H - 1, x + W, y + H, 0xFF6644FF);
        g.fill(x, y, x + 1, y + H, 0xFF6644FF);
        g.fill(x + W - 1, y, x + W, y + H, 0xFF6644FF);

        // Título
        g.drawCenteredString(this.font, "§5§lConfigurar Orb",
            x + W / 2, y + 8, 0xFFFFFF);

        // Label intervalo
        g.drawString(this.font, "§7Intervalo (ticks):", x + 8, y + H - 32, 0xAAAAAA);

        // Mob seleccionado actual
        g.drawString(this.font, "§7Seleccionado: §e" + selectedMob,
            x + 8, y + H - 52, 0xFFFFFF);

        // Lista de mobs
        int listX = x + 8;
        int listY = y + 44;
        g.fill(listX, listY, listX + W - 28, listY + VISIBLE_ROWS * ROW_H, 0xAA000020);

        for (int i = 0; i < VISIBLE_ROWS && (scrollOffset + i) < filteredList.size(); i++) {
            String mob = filteredList.get(scrollOffset + i);
            boolean sel = mob.equals(selectedMob);
            int rowY = listY + i * ROW_H;

            if (sel) g.fill(listX, rowY, listX + W - 28, rowY + ROW_H, 0x886644FF);

            // Hover
            if (mx >= listX && mx <= listX + W - 28 && my >= rowY && my < rowY + ROW_H) {
                g.fill(listX, rowY, listX + W - 28, rowY + ROW_H, 0x44FFFFFF);
            }

            // Nombre corto
            String display = mob.contains(":") ? mob.split(":")[1] : mob;
            g.drawString(this.font, (sel ? "§a> " : "§7") + display,
                listX + 4, rowY + 5, 0xFFFFFF);
        }

        super.render(g, mx, my, delta);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        int x = (this.width  - W) / 2;
        int y = (this.height - H) / 2;
        int listX = x + 8;
        int listY = y + 44;

        // Click en la lista
        if (mx >= listX && mx <= listX + W - 28
                && my >= listY && my < listY + VISIBLE_ROWS * ROW_H) {
            int idx = scrollOffset + (int)((my - listY) / ROW_H);
            if (idx >= 0 && idx < filteredList.size()) {
                selectedMob = filteredList.get(idx);
                return true;
            }
        }
        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double delta) {
        if (delta < 0 && scrollOffset < filteredList.size() - VISIBLE_ROWS) scrollOffset++;
        if (delta > 0 && scrollOffset > 0) scrollOffset--;
        return true;
    }

    private void save() {
        int ticks = 100;
        try { ticks = Integer.parseInt(intervalBox.getValue()); }
        catch (NumberFormatException ignored) {}
        ticks = Math.max(20, ticks);
        OrbPackets.sendSaveConfig(entityId, selectedMob, ticks);
        this.onClose();
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
