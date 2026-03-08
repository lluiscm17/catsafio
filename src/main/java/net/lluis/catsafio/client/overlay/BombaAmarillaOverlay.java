package net.lluis.catsafio.client.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.network.BombaAmarillaAttachPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, value = Dist.CLIENT)
public class BombaAmarillaOverlay {

    private static boolean isAttached = false;
    private static int spacePresses = 0;
    private static final int REQUIRED_PRESSES = 80;
    private static int ticksRemaining = 0;
    private static int maxTicks = 0;
    
    private static final ResourceLocation SPACE_BAR = 
        new ResourceLocation(Catsafio.MOD_ID, "textures/gui/space.png");

    public static void startAttached(int explosionTime) {
        isAttached = true;
        spacePresses = 0;
        ticksRemaining = explosionTime;
        maxTicks = explosionTime;
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.playSound(SoundEvents.CREEPER_PRIMED, 1.0f, 1.5f);
        }
    }

    public static void endAttached() {
        isAttached = false;
        spacePresses = 0;
        ticksRemaining = 0;
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.playSound(SoundEvents.ITEM_BREAK, 1.0f, 1.0f);
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (!isAttached) return;
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        
        // Detectar espacio (keycode 32)
        if (event.getAction() == 1 && event.getKey() == 32) {
            spacePresses++;
            
            // Sonido de progreso
            float pitch = 0.8f + (spacePresses / (float)REQUIRED_PRESSES) * 0.7f;
            mc.player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.3f, pitch);
            
            // Liberarse al alcanzar 80 pulsos
            if (spacePresses >= REQUIRED_PRESSES) {
                // Enviar al servidor que se liberó
                BombaAmarillaAttachPacket packet = new BombaAmarillaAttachPacket(false, 0);
                BombaAmarillaAttachPacket.CHANNEL.sendToServer(packet);
                
                // Notificar servidor
                if (mc.player != null) {
                    BombaAmarillaAttachPacket.setAttached(mc.player.getUUID(), false);
                }
                
                endAttached();
            }
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!isAttached) return;
        
        ticksRemaining--;
        
        // Sonido de tick cada segundo
        if (ticksRemaining % 20 == 0 && ticksRemaining > 0) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                float pitch = 0.8f + ((maxTicks - ticksRemaining) / (float)maxTicks) * 0.7f;
                mc.player.playSound(SoundEvents.NOTE_BLOCK_PLING.value(), 0.5f, pitch);
            }
        }
        
        // Si se acaba el tiempo, falló (la explosión la maneja el servidor)
        if (ticksRemaining <= 0) {
            endAttached();
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        if (!isAttached) return;
        
        Minecraft mc = Minecraft.getInstance();
        GuiGraphics graphics = event.getGuiGraphics();
        
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        
        // Renderizar UI de espacio (misma que Sapo)
        int spaceBarWidth = 200;
        int spaceBarHeight = 50;
        int spaceX = (screenWidth - spaceBarWidth) / 2;
        int spaceY = screenHeight - 120;
        
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, SPACE_BAR);
        
        Matrix4f matrix = graphics.pose().last().pose();
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(matrix, spaceX, spaceY + spaceBarHeight, 0).uv(0, 1).endVertex();
        buffer.vertex(matrix, spaceX + spaceBarWidth, spaceY + spaceBarHeight, 0).uv(1, 1).endVertex();
        buffer.vertex(matrix, spaceX + spaceBarWidth, spaceY, 0).uv(1, 0).endVertex();
        buffer.vertex(matrix, spaceX, spaceY, 0).uv(0, 0).endVertex();
        BufferUploader.drawWithShader(buffer.end());
        
        RenderSystem.disableBlend();
        
        // Texto de progreso (80 pulsaciones)
        String progressText = spacePresses + " / " + REQUIRED_PRESSES;
        int textWidth = mc.font.width(progressText);
        graphics.drawString(mc.font, progressText, 
            (screenWidth - textWidth) / 2, spaceY - 20, 0xFFFFFF, true);
        
        // Mensaje de advertencia
        String message = "§c¡BOMBA ENGANCHADA! Pulsa ESPACIO para quitarla";
        int msgWidth = mc.font.width(message);
        graphics.drawString(mc.font, message, 
            (screenWidth - msgWidth) / 2, spaceY - 40, 0xFFFFFF, true);
        
        // Timer de explosión
        int secondsLeft = ticksRemaining / 20;
        String timerText = "§4⚠ Tiempo: " + secondsLeft + "s";
        int timerWidth = mc.font.width(timerText);
        graphics.drawString(mc.font, timerText, 
            (screenWidth - timerWidth) / 2, spaceY - 60, 0xFFFFFF, true);
        
        // Barra de progreso
        int barWidth = 300;
        int barHeight = 20;
        int barX = (screenWidth - barWidth) / 2;
        int barY = spaceY + spaceBarHeight + 20;
        
        // Fondo de la barra
        graphics.fill(barX, barY, barX + barWidth, barY + barHeight, 0xFF000000);
        
        // Progreso de pulsaciones
        int progress = (int)((spacePresses / (float)REQUIRED_PRESSES) * barWidth);
        graphics.fill(barX, barY, barX + progress, barY + barHeight, 0xFF00FF00);
        
        // Barra de tiempo restante (rojo)
        int timeProgress = (int)((ticksRemaining / (float)maxTicks) * barWidth);
        graphics.fill(barX, barY + barHeight + 5, barX + timeProgress, barY + barHeight + 10, 0xFFFF0000);
        
        // Borde
        graphics.fill(barX - 1, barY - 1, barX + barWidth + 1, barY, 0xFFFFFFFF);
        graphics.fill(barX - 1, barY + barHeight, barX + barWidth + 1, barY + barHeight + 1, 0xFFFFFFFF);
        graphics.fill(barX - 1, barY, barX, barY + barHeight, 0xFFFFFFFF);
        graphics.fill(barX + barWidth, barY, barX + barWidth + 1, barY + barHeight, 0xFFFFFFFF);
    }
    
    public static boolean isAttached() {
        return isAttached;
    }
}
