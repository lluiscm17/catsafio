package net.lluis.catsafio.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.lluis.catsafio.Catsafio;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RouletteAnimationRenderer {

    private static boolean isPlaying = false;
    private static String currentColor = "";
    private static long startTime = 0;
    private static int totalFrames = 0;
    private static float videoDuration = 0;
    private static boolean soundStarted = false;

    private static final int VIDEO_SIZE = 512;

    private static final RouletteConfig[] CONFIGS = {
            new RouletteConfig("azul", 292, 15.229729f, "azul-%d.png", 0, 0),
            new RouletteConfig("amarilla", 288, 15.229729f, "amarilla-%d.png", 0, 0),
            new RouletteConfig("verde", 290, 15.229729f, "verde-%d.png", 0, 0),
            new RouletteConfig("naranja", 287, 15.229729f, "naranja-%d.png", 0, 0),
            new RouletteConfig("turquesa", 287, 15.229729f, "turqueza-%d.png", 0, 0),
            new RouletteConfig("morada", 294, 15.229729f, "morada-%d.png", 0, 0),
            new RouletteConfig("rosada", 392, 22.5f, null, 294, 98)
    };

    public static void startAnimation(String color) {
        RouletteConfig config = getConfig(color);
        if (config == null) {
            Catsafio.LOGGER.error("Unknown roulette color: {}", color);
            return;
        }

        isPlaying = true;
        currentColor = color;
        startTime = 0;
        totalFrames = config.frames;
        videoDuration = config.videoDuration;
        soundStarted = false;
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        if (!isPlaying) return;

        Minecraft mc = Minecraft.getInstance();
        GuiGraphics graphics = event.getGuiGraphics();

        if (startTime == 0) {
            startTime = System.currentTimeMillis();

            if (!soundStarted && mc.player != null && mc.level != null) {
                String soundName = currentColor.equals("rosada") ? "ruleta_rosada" : "ruleta";
                ResourceLocation sound = new ResourceLocation(Catsafio.MOD_ID, soundName);

                mc.level.playSound(
                        mc.player,
                        mc.player.blockPosition(),
                        net.minecraftforge.registries.ForgeRegistries.SOUND_EVENTS.getValue(sound),
                        SoundSource.MASTER,
                        1.0f,
                        1.0f
                );
                soundStarted = true;
            }
        }

        float elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000.0f;

        float progress = elapsedSeconds / videoDuration;
        int currentFrame = Math.min((int)(progress * totalFrames) + 1, totalFrames);

        if (currentFrame < 1) currentFrame = 1;

        // Terminar la animación cuando se acabe el video
        // NO enviar packet al servidor - el servidor ya programó los comandos
        if (elapsedSeconds >= videoDuration) {
            isPlaying = false;
            return;
        }

        RouletteConfig config = getConfig(currentColor);
        if (config == null) return;

        String frameName = getFrameName(config, currentFrame);
        ResourceLocation frameTexture = new ResourceLocation(
                Catsafio.MOD_ID,
                "textures/roulettes/" + currentColor + "/" + frameName
        );

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        int renderWidth = VIDEO_SIZE;
        int renderHeight = VIDEO_SIZE;

        int offsetX = (screenWidth - renderWidth) / 2;
        int offsetY = (screenHeight - renderHeight) / 2;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, frameTexture);

        Matrix4f matrix = graphics.pose().last().pose();
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(matrix, offsetX, offsetY + renderHeight, 0).uv(0, 1).endVertex();
        buffer.vertex(matrix, offsetX + renderWidth, offsetY + renderHeight, 0).uv(1, 1).endVertex();
        buffer.vertex(matrix, offsetX + renderWidth, offsetY, 0).uv(1, 0).endVertex();
        buffer.vertex(matrix, offsetX, offsetY, 0).uv(0, 0).endVertex();
        BufferUploader.drawWithShader(buffer.end());

        RenderSystem.disableBlend();
    }

    private static String getFrameName(RouletteConfig config, int frameNumber) {
        if (config.rosadaFrames > 0 && config.momentonutriaFrames > 0) {
            if (frameNumber <= config.rosadaFrames) {
                return String.format("rosada-%d.png", frameNumber);
            } else {
                int momentoFrame = frameNumber - config.rosadaFrames;
                return String.format("momentonutria-%d.png", momentoFrame);
            }
        }
        return String.format(config.framePattern, frameNumber);
    }

    private static RouletteConfig getConfig(String color) {
        for (RouletteConfig config : CONFIGS) {
            if (config.color.equals(color)) {
                return config;
            }
        }
        return null;
    }

    public static boolean isAnimationPlaying() {
        return isPlaying;
    }

    static class RouletteConfig {
        String color;
        int frames;
        float videoDuration;
        String framePattern;
        int rosadaFrames;
        int momentonutriaFrames;

        RouletteConfig(String color, int frames, float videoDuration,
                       String framePattern, int rosadaFrames, int momentonutriaFrames) {
            this.color = color;
            this.frames = frames;
            this.videoDuration = videoDuration;
            this.framePattern = framePattern;
            this.rosadaFrames = rosadaFrames;
            this.momentonutriaFrames = momentonutriaFrames;
        }
    }
}