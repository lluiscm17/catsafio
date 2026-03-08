package net.lluis.catsafio.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lluis.catsafio.Catsafio;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class HealthNametagRenderer {

    @SubscribeEvent
    public static void onRenderNameTag(RenderNameTagEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        // Obtener vida del jugador
        float health = player.getHealth();

        // Formatear texto: "20 HP" o "15.5 HP"
        String healthText;
        if (health == (int)health) {
            healthText = String.format("%d HP", (int)health);
        } else {
            healthText = String.format("%.1f HP", health);
        }

        // Preparar renderizado
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource buffer = event.getMultiBufferSource();
        Font font = mc.font;

        poseStack.pushPose();

        // Posicionar encima del nametag
        poseStack.translate(0.0D, 2.15D, 0.0D);

        // Rotar hacia la cámara (SOLO en Y - horizontal)
        // Obtener yaw de la cámara
        float cameraYaw = mc.gameRenderer.getMainCamera().getYRot();
        poseStack.mulPose(Axis.YP.rotationDegrees(-cameraYaw));

        // Escalar
        float scale = 0.025F;
        poseStack.scale(-scale, -scale, scale);

        Matrix4f matrix = poseStack.last().pose();

        // Centrar el texto
        float textWidth = font.width(healthText);
        float x = -textWidth / 2.0F;

        // Dibujar sombra negra (fondo)
        font.drawInBatch(healthText, x, 0, 0x20000000, false, matrix, buffer,
                Font.DisplayMode.NORMAL, 0, event.getPackedLight());

        // Dibujar texto blanco
        font.drawInBatch(healthText, x, 0, 0xFFFFFFFF, false, matrix, buffer,
                Font.DisplayMode.SEE_THROUGH, 0, event.getPackedLight());

        poseStack.popPose();
    }
}