package net.lluis.catsafio.hub.client.gui;
import net.lluis.catsafio.hub.data.HubData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import java.util.List;
public class NotificacionesScreen extends Screen {
    public NotificacionesScreen() { super(Component.literal("Notificaciones")); }
    @Override
    public void render(GuiGraphics g, int mx, int my, float tick) {
        this.renderBackground(g);
        g.drawCenteredString(font, "§a§lNOTIFICACIONES", this.width/2, 40, 0xFFFFFFFF);
        List<String> notifs = HubData.get().getNotificaciones();
        int y = 80;
        for (int i = 0; i < Math.min(notifs.size(), 10); i++) {
            g.fill(this.width/2 - 300, y - 2, this.width/2 + 300, y + 20, 0x88000000);
            g.drawString(font, "§e● §f" + notifs.get(i), this.width/2 - 290, y + 4, 0xFFFFFFFF, false);
            y += 26;
        }
        if (notifs.isEmpty()) {
            g.drawCenteredString(font, "§7No hay notificaciones", this.width/2, 150, 0xFFFFFFFF);
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
