package net.lluis.catsafio.hub.client.gui;
import net.lluis.catsafio.hub.data.HubData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
public class DificultadScreen extends Screen {
    public DificultadScreen() { super(Component.literal("Cambios de Dificultad")); }
    @Override
    public void render(GuiGraphics g, int mx, int my, float tick) {
        this.renderBackground(g);
        g.drawCenteredString(font, "§c§lCAMBIOS DE DIFICULTAD", this.width/2, 40, 0xFFFFFFFF);
        String[] lines = HubData.get().getDificultadText().split("\n");
        int y = 100;
        for (String line : lines) {
            g.drawCenteredString(font, line, this.width/2, y, 0xFFFFFFFF);
            y += 12;
        }
        drawBackButton(g, mx, my);
        super.render(g, mx, my, tick);
    }
    private void drawBackButton(GuiGraphics g, int mx, int my) {
        int btnX = this.width - 110, btnY = this.height - 40, btnW = 100, btnH = 30;
        boolean hovered = mx >= btnX && mx < btnX + btnW && my >= btnY && my < btnY + btnH;
        g.fill(btnX, btnY, btnX + btnW, btnY + btnH, hovered ? 0xFFE94560 : 0xFF0F3460);
        g.drawCenteredString(font, "§l← VOLVER", btnX + btnW/2, btnY + 10, 0xFFFFFFFF);
    }
    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (button == 0 && mx >= this.width - 110 && mx < this.width - 10 && my >= this.height - 40 && my < this.height - 10) {
            minecraft.setScreen(new MenuRadialScreen());
            return true;
        }
        return super.mouseClicked(mx, my, button);
    }
    @Override
    public boolean isPauseScreen() { return false; }
}
