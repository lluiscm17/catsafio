package net.lluis.catsafio.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.lluis.catsafio.Catsafio;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DeathAnimationRenderer {

    private static boolean isPlaying = false;
    private static int currentFrame = 0;
    private static long animationStartTime = 0;
    private static String deathMessage = "";

    // 263 frames en 8 segundos = 32.875 FPS
    private static final int TOTAL_FRAMES = 263;
    private static final float FRAMES_PER_SECOND = 32.875f;
    private static final float FRAME_DURATION_MS = 1000f / FRAMES_PER_SECOND;

    // Tamaño del cuadrado en píxeles
    // IMPORTANTE: Usa múltiplos de 256 para mejor calidad (256, 512, 768)
    // O valores exactos para evitar blur (256, 300 se verá nítido)
    private static final int VIDEO_SIZE = 256; // Doble del original = más nítido

    public static void startAnimation(String deathCause) {
        isPlaying = true;
        currentFrame = 0;
        animationStartTime = System.currentTimeMillis();
        deathMessage = deathCause;

        // Reproducir sonido
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.playSound(
                    net.lluis.catsafio.sound.ModSounds.DEATH_ANIMATION.get(),
                    1.0f,
                    1.0f
            );
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        if (!isPlaying) return;

        Minecraft mc = Minecraft.getInstance();
        GuiGraphics graphics = event.getGuiGraphics();

        // Calcular frame actual basado en tiempo
        long elapsed = System.currentTimeMillis() - animationStartTime;
        currentFrame = (int)(elapsed / FRAME_DURATION_MS);

        // Terminar animación si llegamos al final
        if (currentFrame >= TOTAL_FRAMES) {
            isPlaying = false;
            return;
        }

        // Frame actual (con padding: 00001, 00002, etc.)
        String frameNumber = String.format("%05d", currentFrame + 1);
        ResourceLocation frameTexture = new ResourceLocation(
                Catsafio.MOD_ID,
                "textures/death_animation/" + frameNumber + ".png"
        );

        // Dimensiones de la pantalla
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        // Video CUADRADO (1:1)
        int renderWidth = VIDEO_SIZE;
        int renderHeight = VIDEO_SIZE;

        // Centrar
        int offsetX = (screenWidth - renderWidth) / 2;
        int offsetY = (screenHeight - renderHeight) / 2;

        // Renderizar con configuración optimizada para nitidez
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, frameTexture);

        // Dibujar textura (sin filtrado extra - usa el default de Minecraft que es más nítido)
        Matrix4f matrix = graphics.pose().last().pose();
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(matrix, offsetX, offsetY + renderHeight, 0).uv(0, 1).endVertex();
        buffer.vertex(matrix, offsetX + renderWidth, offsetY + renderHeight, 0).uv(1, 1).endVertex();
        buffer.vertex(matrix, offsetX + renderWidth, offsetY, 0).uv(1, 0).endVertex();
        buffer.vertex(matrix, offsetX, offsetY, 0).uv(0, 0).endVertex();
        BufferUploader.drawWithShader(buffer.end());

        RenderSystem.disableBlend();

        // Renderizar mensaje de muerte en rojo debajo
        if (!deathMessage.isEmpty()) {
            Component message = Component.literal("§c" + deathMessage);
            int textWidth = mc.font.width(message);
            int textX = (screenWidth - textWidth) / 2;
            int textY = offsetY + renderHeight + 10;

            // Sombra para mejor legibilidad
            graphics.drawString(mc.font, message, textX, textY, 0xFF0000, true);
        }
    }

    public static boolean isAnimationPlaying() {
        return isPlaying;
    }
}