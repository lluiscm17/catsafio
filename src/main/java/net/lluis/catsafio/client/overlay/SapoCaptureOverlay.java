package net.lluis.catsafio.client.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.network.SapoPackets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class SapoCaptureOverlay {

    // espaciouno.png = jugador NO pulsa | espaciodos.png = jugador SÍ pulsa
    private static final ResourceLocation TEX_IDLE  =
            new ResourceLocation(Catsafio.MOD_ID, "textures/gui/espaciouno.png");
    private static final ResourceLocation TEX_PRESS =
            new ResourceLocation(Catsafio.MOD_ID, "textures/gui/espaciodos.png");

    // Tamaño real de las texturas (las imágenes son rectangulares)
    private static final int TEX_W = 182;
    private static final int TEX_H = 22;

    private static final int MAX_PRESSES   = 50;
    private static final int PRESS_CD      = 3;   // ticks mínimos entre pulsaciones

    // Estado cliente
    private static boolean captured  = false; // cogido por lengua (muestra UI)
    private static boolean swallowed = false; // dentro del sapo

    private static int  pressCount   = 0;
    private static int  pressCooldown = 0;
    private static int  flashTicks   = 0;    // cuántos ticks mostrar TEX_PRESS
    private static boolean pressing  = false;

    // ── Callbacks desde packets ────────────────────────────────────────
    public static void onCaptureStart() {
        captured   = true;
        swallowed  = false;
        pressCount = 0;
        pressing   = false;
        flashTicks = 0;
    }

    public static void onCaptureEnd() {
        captured  = false;
        pressing  = false;
        flashTicks = 0;
    }

    public static void onSwallowed(boolean inside) {
        swallowed = inside;
        if (inside) {
            captured = false; // ya no mostrar UI de lengua
        }
    }

    // ── Input ESPACIO ──────────────────────────────────────────────────
    @SubscribeEvent
    public static void onKey(InputEvent.Key event) {
        if (!captured) return;
        if (event.getKey() != GLFW.GLFW_KEY_SPACE) return;
        if (event.getAction() != GLFW.GLFW_PRESS)  return;
        if (pressCooldown > 0) return;

        pressCount++;
        pressCooldown = PRESS_CD;
        pressing      = true;
        flashTicks    = 6;

        // Notificar servidor
        SapoPackets.sendSpacePress();

        // Al llegar a 50 pulsaciones → escapar (si está en fase de lengua)
        if (pressCount >= MAX_PRESSES) {
            onCaptureEnd();
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (pressCooldown > 0) pressCooldown--;
        if (flashTicks > 0) {
            flashTicks--;
            if (flashTicks == 0) pressing = false;
        }
    }

    // ── Render HUD ─────────────────────────────────────────────────────
    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() != VanillaGuiOverlay.CROSSHAIR.type()) return;
        if (!captured && !swallowed) return;

        Minecraft mc = Minecraft.getInstance();
        int sw = mc.getWindow().getGuiScaledWidth();
        int sh = mc.getWindow().getGuiScaledHeight();
        GuiGraphics g = event.getGuiGraphics();

        // ── Textura de la barra ESPACIO ────────────────────────────────
        // Escalar x2 — más pequeño
        int dispW = TEX_W * 2;
        int dispH = TEX_H * 2;
        int dispX = (sw - dispW) / 2;
        int dispY = sh - 70;

        // Solo mostrar la UI de espacio cuando está siendo arrastrado (NO cuando está tragado)
        if (captured) {
            ResourceLocation tex = pressing ? TEX_PRESS : TEX_IDLE;
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            g.blit(tex, dispX, dispY, dispW, dispH, 0, 0, TEX_W, TEX_H, TEX_W, TEX_H);
            RenderSystem.disableBlend();
        }

        // ── Barra de progreso (pulsaciones) ────────────────────────────
        if (captured) {
            int barMaxW = dispW;
            int barFill = (int)((pressCount / (float) MAX_PRESSES) * barMaxW);
            int barX    = dispX;
            int barY    = dispY + dispH + 4;

            // Fondo
            g.fill(barX,    barY, barX + barMaxW, barY + 6, 0xAA333333);
            // Relleno verde
            if (barFill > 0)
                g.fill(barX, barY, barX + barFill, barY + 6, 0xFF55DD55);

            // Texto
            String msg = "§e¡Pulsa ESPACIO para escapar! §f" + pressCount + "/" + MAX_PRESSES;
            int tw = mc.font.width(msg);
            g.drawString(mc.font, msg, (sw - tw) / 2, barY + 10, 0xFFFFFF, true);
        }

        if (swallowed) {
            String msg = "§c¡Mata al Sapo para escapar!";
            int tw = mc.font.width(msg);
            g.fill((sw - tw) / 2 - 4, sh / 2 - 14, (sw + tw) / 2 + 4, sh / 2 - 2, 0xAA000000);
            g.drawString(mc.font, msg, (sw - tw) / 2, sh / 2 - 12, 0xFF4444, true);
        }
    }
}
