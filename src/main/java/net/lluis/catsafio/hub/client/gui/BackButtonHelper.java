package net.lluis.catsafio.hub.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.lluis.catsafio.Catsafio;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class BackButtonHelper {

    private static final ResourceLocation VOLVER_ICON =
            new ResourceLocation(Catsafio.MOD_ID, "textures/gui/hub/volver_icon.png");

    private static final int ICON_SIZE = 32;

    // Posición: centro inferior de pantalla
    private static int btnX(int screenWidth)  { return screenWidth  / 2 - ICON_SIZE / 2; }
    private static int btnY(int screenHeight) { return screenHeight - ICON_SIZE - 20; }

    public static boolean drawAndCheckHover(GuiGraphics g, Font font, int screenWidth, int screenHeight, int mx, int my) {
        int x = btnX(screenWidth);
        int y = btnY(screenHeight);
        boolean hovered = mx >= x && mx < x + ICON_SIZE && my >= y && my < y + ICON_SIZE;

        // Sin círculo — solo icono transparente, leve brillo blanco al hover
        if (hovered) {
            g.fill(x - 2, y - 2, x + ICON_SIZE + 2, y + ICON_SIZE + 2, 0x33FFFFFF);
        }

        RenderSystem.enableBlend();
        g.blit(VOLVER_ICON, x, y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
        RenderSystem.disableBlend();

        return hovered;
    }

    public static boolean isClicked(int screenWidth, int screenHeight, double mx, double my) {
        int x = btnX(screenWidth);
        int y = btnY(screenHeight);
        return mx >= x && mx < x + ICON_SIZE && my >= y && my < y + ICON_SIZE;
    }
}