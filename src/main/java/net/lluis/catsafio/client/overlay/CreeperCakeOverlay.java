package net.lluis.catsafio.client.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.lluis.catsafio.Catsafio;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class CreeperCakeOverlay {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Catsafio.MOD_ID, "textures/gui/creepercake_overlay.png");

    private static final int DURATION   = 160; // 8 segundos
    private static final int FADE_TICKS = 40;  // 2 segundos de fade

    private static int ticksRemaining = 0;

    /** Llamado desde el packet cuando el creeper explota */
    public static void show() {
        ticksRemaining = DURATION;
    }

    /** Decrementar en el tick del cliente, no en el render */
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (ticksRemaining > 0) ticksRemaining--;
    }

    /** Renderizar encima de todo el HUD */
    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        // Renderizar tras el crosshair para quedar encima de todo
        if (event.getOverlay() != VanillaGuiOverlay.CROSSHAIR.type()) return;
        if (ticksRemaining <= 0) return;

        Minecraft mc = Minecraft.getInstance();
        int w = mc.getWindow().getGuiScaledWidth();
        int h = mc.getWindow().getGuiScaledHeight();

        float alpha = ticksRemaining < FADE_TICKS
                ? (float) ticksRemaining / FADE_TICKS
                : 1.0f;

        GuiGraphics g = event.getGuiGraphics();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1f, 1f, 1f, alpha);

        // Textura 500x500 centrada en pantalla — 80% del lado más pequeño
        int size = (int)(Math.min(w, h) * 0.8f);
        int ox   = (w - size) / 2;
        int oy   = (h - size) / 2;
        g.blit(TEXTURE, ox, oy, size, size, 0, 0, 500, 500, 500, 500);

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
    }
}