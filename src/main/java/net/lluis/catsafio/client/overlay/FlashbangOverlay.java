package net.lluis.catsafio.client.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class FlashbangOverlay {

    private static final float FLASH_DURATION_MS = 4000f; // 4 segundos en ms
    private static long flashStartTime = -1;

    public static void triggerFlash() {
        flashStartTime = System.currentTimeMillis();
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.playSound(ModSounds.FLASHBANG_SOUND.get(), 1.0f, 1.0f);
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        if (flashStartTime < 0) return;

        long elapsed = System.currentTimeMillis() - flashStartTime;
        if (elapsed >= FLASH_DURATION_MS) {
            flashStartTime = -1;
            return;
        }

        // Alpha: 1.0 al inicio → 0.0 al final, basado en tiempo real
        float alpha = 1.0f - (elapsed / FLASH_DURATION_MS);

        Minecraft mc = Minecraft.getInstance();
        int sw = mc.getWindow().getGuiScaledWidth();
        int sh = mc.getWindow().getGuiScaledHeight();

        GuiGraphics g = event.getGuiGraphics();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        int a = (int)(alpha * 255);
        int color = (a << 24) | 0x00FFFFFF; // ARGB blanco
        g.fill(0, 0, sw, sh, color);

        RenderSystem.disableBlend();
    }
}