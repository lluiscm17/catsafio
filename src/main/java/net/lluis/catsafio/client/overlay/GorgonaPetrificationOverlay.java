package net.lluis.catsafio.client.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.network.GorgonaPetrificationPacket;
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
public class GorgonaPetrificationOverlay {

    private static boolean isPetrified = false;
    private static int spacePresses = 0;
    private static final int REQUIRED_PRESSES = 150;
    
    // Recursos
    private static final ResourceLocation STONE_OVERLAY = 
        new ResourceLocation(Catsafio.MOD_ID, "textures/gui/fondo_gorgona.png");
    private static final ResourceLocation SPACE_BAR = 
        new ResourceLocation(Catsafio.MOD_ID, "textures/gui/espaciouno.png");

    public static void startPetrification() {
        isPetrified = true;
        spacePresses = 0;
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.playSound(SoundEvents.GLASS_BREAK, 1.0f, 0.5f);
        }
    }

    public static void endPetrification() {
        isPetrified = false;
        spacePresses = 0;
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.playSound(SoundEvents.GLASS_BREAK, 1.0f, 1.5f);
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (!isPetrified) return;
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        
        // Detectar espacio (keycode 32 o 341 según configuración)
        if (event.getAction() == 1 && event.getKey() == 32) { // Press
            spacePresses++;
            
            // Sonido de progreso
            float pitch = 0.5f + (spacePresses / (float)REQUIRED_PRESSES);
            mc.player.playSound(SoundEvents.STONE_BREAK, 0.3f, pitch);
            
            // Liberarse al alcanzar 150 pulsos
            if (spacePresses >= REQUIRED_PRESSES) {
                // Enviar al servidor que se liberó
                GorgonaPetrificationPacket packet = new GorgonaPetrificationPacket(false);
                GorgonaPetrificationPacket.CHANNEL.sendToServer(packet);
                
                // Notificar servidor para quitar de lista
                if (mc.player != null) {
                    GorgonaPetrificationPacket.setPetrified(mc.player.getUUID(), false);
                }
                
                endPetrification();
            }
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!isPetrified) return;
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        
        // Bloquear movimiento mientras está petrificado
        mc.player.setDeltaMovement(0, mc.player.getDeltaMovement().y, 0);
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        if (!isPetrified) return;
        
        Minecraft mc = Minecraft.getInstance();
        GuiGraphics graphics = event.getGuiGraphics();
        
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        
        // 1. Renderizar fondo de piedra semitransparente (2100x1039)
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 0.7f); // 70% opacidad
        RenderSystem.setShaderTexture(0, STONE_OVERLAY);
        
        Matrix4f matrix = graphics.pose().last().pose();
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(matrix, 0, screenHeight, 0).uv(0, 1).endVertex();
        buffer.vertex(matrix, screenWidth, screenHeight, 0).uv(1, 1).endVertex();
        buffer.vertex(matrix, screenWidth, 0, 0).uv(1, 0).endVertex();
        buffer.vertex(matrix, 0, 0, 0).uv(0, 0).endVertex();
        BufferUploader.drawWithShader(buffer.end());
        
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        
        // 2. Renderizar UI de espacio (misma que el sapo)
        int spaceBarWidth = 200;
        int spaceBarHeight = 50;
        int spaceX = (screenWidth - spaceBarWidth) / 2;
        int spaceY = screenHeight - 120;
        
        RenderSystem.setShaderTexture(0, SPACE_BAR);
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(matrix, spaceX, spaceY + spaceBarHeight, 0).uv(0, 1).endVertex();
        buffer.vertex(matrix, spaceX + spaceBarWidth, spaceY + spaceBarHeight, 0).uv(1, 1).endVertex();
        buffer.vertex(matrix, spaceX + spaceBarWidth, spaceY, 0).uv(1, 0).endVertex();
        buffer.vertex(matrix, spaceX, spaceY, 0).uv(0, 0).endVertex();
        BufferUploader.drawWithShader(buffer.end());
        
        RenderSystem.disableBlend();
        
        // 3. Texto de progreso
        String progressText = spacePresses + " / " + REQUIRED_PRESSES;
        int textWidth = mc.font.width(progressText);
        graphics.drawString(mc.font, progressText, 
            (screenWidth - textWidth) / 2, spaceY - 20, 0xFFFFFF, true);
        
        // 4. Mensaje
        String message = "§c¡ESTÁS PETRIFICADO! Pulsa ESPACIO para liberarte";
        int msgWidth = mc.font.width(message);
        graphics.drawString(mc.font, message, 
            (screenWidth - msgWidth) / 2, spaceY - 40, 0xFFFFFF, true);
        
        // 5. Barra de progreso
        int barWidth = 300;
        int barHeight = 20;
        int barX = (screenWidth - barWidth) / 2;
        int barY = spaceY + spaceBarHeight + 20;
        
        // Fondo de la barra
        graphics.fill(barX, barY, barX + barWidth, barY + barHeight, 0xFF000000);
        
        // Progreso
        int progress = (int)((spacePresses / (float)REQUIRED_PRESSES) * barWidth);
        graphics.fill(barX, barY, barX + progress, barY + barHeight, 0xFF00FF00);
        
        // Borde
        graphics.fill(barX - 1, barY - 1, barX + barWidth + 1, barY, 0xFFFFFFFF);
        graphics.fill(barX - 1, barY + barHeight, barX + barWidth + 1, barY + barHeight + 1, 0xFFFFFFFF);
        graphics.fill(barX - 1, barY, barX, barY + barHeight, 0xFFFFFFFF);
        graphics.fill(barX + barWidth, barY, barX + barWidth + 1, barY + barHeight, 0xFFFFFFFF);
    }
    
    public static boolean isPetrified() {
        return isPetrified;
    }
}
