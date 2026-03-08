package net.lluis.catsafio.hub.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.hub.client.gui.MenuRadialScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class HubButtonInjector {

    private static final ResourceLocation ICON = new ResourceLocation(
            Catsafio.MOD_ID, "textures/gui/hub/volver_icon.png"
    );

    // Icono a tamaño NATIVO 32x32 — sin escalar, sin recorte
    private static final int SIZE = 23;

    // Posición relativa al panel inventario (176x166)
    // Slot escudo está en relX=56, relY=87 (16x16)
    // Ponemos el icono centrado justo a su derecha: relX=76, relY=79
    private static final int REL_X = 130;
    private static final int REL_Y = 55;

    @SubscribeEvent
    public static void onInventoryInit(ScreenEvent.Init.Post event) {
        if (!(event.getScreen() instanceof InventoryScreen screen)) return;
        int left = (screen.width  - 176) / 2;
        int top  = (screen.height - 166) / 2;
        // Botón invisible del tamaño exacto del icono para capturar clicks
        event.addListener(new HubIconButton(left + REL_X, top + REL_Y, SIZE));
    }

    @SubscribeEvent
    public static void onRenderPost(ScreenEvent.Render.Post event) {
        if (!(event.getScreen() instanceof InventoryScreen screen)) return;

        int left = (screen.width  - 176) / 2;
        int top  = (screen.height - 166) / 2;
        int x = left + REL_X;
        int y = top  + REL_Y;

        GuiGraphics g = event.getGuiGraphics();

        // Desactivar scissor para no recortar
        RenderSystem.disableScissor();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // Calcular hover con coordenadas de ratón escaladas a GUI units
        Minecraft mc = Minecraft.getInstance();
        double scaleX = (double) screen.width  / mc.getWindow().getScreenWidth();
        double scaleY = (double) screen.height / mc.getWindow().getScreenHeight();
        double mx = mc.mouseHandler.xpos() * scaleX;
        double my = mc.mouseHandler.ypos() * scaleY;
        boolean hovered = mx >= x && mx < x + SIZE && my >= y && my < y + SIZE;

        // Dibujar icono 32x32 → 32x32 (sin escala = sin recorte)
        g.blit(ICON, x, y, 0, 0, SIZE, SIZE, SIZE, SIZE);

        RenderSystem.disableBlend();

        if (hovered) {
            g.renderTooltip(mc.font,
                    Component.literal("Abrir Menu Hub"), (int)mx, (int)my);
        }
    }

    static class HubIconButton extends AbstractButton {
        HubIconButton(int x, int y, int size) {
            super(x, y, size, size, Component.empty());
        }
        @Override
        public void renderWidget(GuiGraphics g, int mx, int my, float tick) {
            // vacío — render lo hace onRenderPost
        }
        @Override
        public void onPress() {
            Minecraft.getInstance().setScreen(new MenuRadialScreen());
        }
        @Override
        protected void updateWidgetNarration(NarrationElementOutput out) {
            this.defaultButtonNarrationText(out);
        }
    }
}