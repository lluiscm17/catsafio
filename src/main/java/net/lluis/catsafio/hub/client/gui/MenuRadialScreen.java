package net.lluis.catsafio.hub.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.lluis.catsafio.Catsafio;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class MenuRadialScreen extends Screen {

    private static final ResourceLocation[] ICONS = {
            new ResourceLocation(Catsafio.MOD_ID, "textures/gui/hub/misiones_icon.png"),
            new ResourceLocation(Catsafio.MOD_ID, "textures/gui/hub/catpedia_icon.png"),
            new ResourceLocation(Catsafio.MOD_ID, "textures/gui/hub/dificultad_icon.png"),
            new ResourceLocation(Catsafio.MOD_ID, "textures/gui/hub/arbol_tech_icon.png"),
            new ResourceLocation(Catsafio.MOD_ID, "textures/gui/hub/notificaciones_icon.png"),
    };
    private static final ResourceLocation VOLVER_ICON =
            new ResourceLocation(Catsafio.MOD_ID, "textures/gui/hub/volver_icon.png");
    private static final String[] LABELS = {
            "Misiones", "Catpedia", "Dificultad", "Arbol Tec.", "Notificaciones",
    };

    private static final int ICON_RENDER = 32;  // nativo, sin escalar
    private static final int ICON_TEX    = 32;  // los PNG son 32x32 reales
    private static final int CENTER_BTN  = 38;

    private int hoveredIndex = -1;
    private boolean hoverCenter = false;

    public MenuRadialScreen() {
        super(Component.literal("Menu Principal"));
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float tick) {
        // Fondo oscuro
        g.fill(0, 0, this.width, this.height, 0xAA000000);


        // ── Coordenadas fijas absolutas basadas en porcentaje ──────────
        // El centro del circulo esta al 50% del ancho y al 42% del alto
        // (un poco arriba del centro para dejar espacio a la hotbar)
        int cx = this.width  * 50 / 100;
        int cy = this.height * 42 / 100;

        // Radio: el icono mas alto ocupa cy - radius - 20 desde arriba
        // Necesitamos: cy - radius - 20 >= 5  →  radius <= cy - 25
        // El icono mas bajo ocupa cy + radius*0.809 + 20 desde arriba
        // Necesitamos: cy + radius*0.809 + 20 <= height - 50  →  radius <= (height-50-cy-20)/0.809
        int maxR1 = cy - 40;                                          // limite superior (40px margen arriba)
        int maxR2 = (int)((this.height - 50 - cy - 20) / 0.809);     // limite inferior
        int maxR3 = (int)((this.width / 2.0 - 25) / 0.951);          // limite lateral
        int radius = Math.min(maxR1, Math.min(maxR2, maxR3));
        radius = Math.max(radius, 50); // minimo absoluto


        hoveredIndex = -1;
        hoverCenter  = false;

        // ── Boton central ─────────────────────────────────────────────
        int halfC = CENTER_BTN / 2;
        hoverCenter = Math.abs(mx - cx) < halfC && Math.abs(my - cy) < halfC;
        // Solo dibujar el icono, sin círculo de fondo
        int off = (CENTER_BTN - ICON_RENDER) / 2;
        g.blit(VOLVER_ICON,
                cx - halfC + off, cy - halfC + off,
                0, 0, ICON_RENDER, ICON_RENDER, ICON_TEX, ICON_TEX);
        if (hoverCenter) {
            int lw = font.width("Cerrar");
            g.drawString(font, "Cerrar", cx - lw / 2, cy + halfC + 5, 0xFFFFFFFF, true);
        }

        // ── 5 iconos radiales ──────────────────────────────────────────
        for (int i = 0; i < ICONS.length; i++) {
            double angle = Math.toRadians(i * 72 - 90);
            int bx = cx + (int)(Math.cos(angle) * radius);
            int by = cy + (int)(Math.sin(angle) * radius);

            // Coordenadas del blit (esquina superior izquierda del icono)
            int ix = bx - ICON_RENDER / 2;
            int iy = by - ICON_RENDER / 2;

            int half = ICON_RENDER / 2 + 4;
            boolean hovered = (mx >= bx - half && mx < bx + half
                    && my >= by - half && my < by + half);
            if (hovered) hoveredIndex = i;

            // sin círculo de hover

            // Dibujar icono
            g.blit(ICONS[i], ix, iy, 0, 0, ICON_RENDER, ICON_RENDER, ICON_TEX, ICON_TEX);


            if (hovered) {
                int lw = font.width(LABELS[i]);
                g.drawString(font, LABELS[i], bx - lw / 2, by + half + 4, 0xFFFFFFFF, true);
            }
        }

        super.render(g, mx, my, tick);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (button != 0) return false;
        int cx = this.width  * 50 / 100;
        int cy = this.height * 42 / 100;
        int maxR1 = cy - 25;
        int maxR2 = (int)((this.height - 50 - cy - 20) / 0.809);
        int maxR3 = (int)((this.width / 2.0 - 25) / 0.951);
        int radius = Math.max(Math.min(maxR1, Math.min(maxR2, maxR3)), 50);

        int halfC = CENTER_BTN / 2;
        if (Math.abs(mx - cx) < halfC && Math.abs(my - cy) < halfC) {
            this.onClose(); return true;
        }
        for (int i = 0; i < ICONS.length; i++) {
            double angle = Math.toRadians(i * 72 - 90);
            int bx = cx + (int)(Math.cos(angle) * radius);
            int by = cy + (int)(Math.sin(angle) * radius);
            int half = ICON_RENDER / 2 + 4;
            if (mx >= bx - half && mx < bx + half && my >= by - half && my < by + half) {
                Screen next = switch (i) {
                    case 0 -> new net.lluis.catsafio.hub.client.gui.MisionesScreen();
                    case 1 -> new CatpediaScreen();
                    case 2 -> new DificultadScreen();
                    case 3 -> new ArbolTechScreen();
                    case 4 -> new NotificacionesScreen();
                    default -> null;
                };
                if (next != null) minecraft.setScreen(next);
                return true;
            }
        }
        return super.mouseClicked(mx, my, button);
    }

    private void drawCircleFill(GuiGraphics g, int cx, int cy, int r, int color) {
        for (int dy = -r; dy <= r; dy++)
            for (int dx = -r; dx <= r; dx++)
                if (dx*dx + dy*dy <= r*r)
                    g.fill(cx+dx, cy+dy, cx+dx+1, cy+dy+1, color);
    }
    private void drawCircleOutline(GuiGraphics g, int cx, int cy, int r, int color, int t) {
        for (int i = 0; i < t; i++) {
            int ri = r - i;
            for (int a = 0; a < 360; a++) {
                double rad = Math.toRadians(a);
                int x = cx + (int)(Math.cos(rad) * ri);
                int y = cy + (int)(Math.sin(rad) * ri);
                g.fill(x, y, x+1, y+1, color);
            }
        }
    }

    @Override public boolean isPauseScreen() { return false; }
}