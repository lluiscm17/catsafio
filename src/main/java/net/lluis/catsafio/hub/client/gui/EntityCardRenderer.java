package net.lluis.catsafio.hub.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class EntityCardRenderer {

    /**
     * Renderiza una entidad 3D rotatoria en una posición específica de la pantalla.
     * 
     * @param graphics GuiGraphics para renderizado
     * @param x Posición X en pantalla
     * @param y Posición Y en pantalla
     * @param size Tamaño de la entidad
     * @param entityType Tipo de entidad a renderizar
     * @param rotationTicks Tiempo de rotación (usa tickCount del screen)
     */
    public static void renderEntity(GuiGraphics graphics, int x, int y, int size,
                                     EntityType<?> entityType, float rotationTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        // Crear entidad temporal para renderizar
        Entity entity = entityType.create(mc.level);
        if (entity == null) return;

        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();

        // Posicionar en la carta
        poseStack.translate(x, y, 50);
        
        // Escalar la entidad
        poseStack.scale(size, size, size);
        
        // Rotar en Y (horizontal) para efecto rotatorio
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationTicks * 2f));
        
        // Inclinar ligeramente para mejor vista
        poseStack.mulPose(Axis.XP.rotationDegrees(15f));

        // Configurar iluminación
        poseStack.last().pose().rotate(Axis.XP.rotationDegrees(-15f));

        // Renderizar la entidad
        EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

        try {
            RenderSystem.runAsFancy(() -> {
                dispatcher.render(entity, 0, 0, 0, 0f, 1f, 
                    poseStack, bufferSource, 15728880);
            });
            bufferSource.endBatch();
        } catch (Exception e) {
            // Si falla el render, no hacer nada
        }

        poseStack.popPose();
        
        // Descartar entidad temporal
        entity.discard();
    }

    /**
     * Variante simplificada que calcula el tamaño automáticamente
     */
    public static void renderEntityInCard(GuiGraphics graphics, int cardX, int cardY,
                                          int cardWidth, int cardHeight,
                                          EntityType<?> entityType, float rotationTicks) {
        // Posición centrada en la carta
        int centerX = cardX + cardWidth / 2;
        int centerY = cardY + cardHeight / 2 - 10; // Ligeramente arriba del centro

        // Tamaño basado en el tamaño de la carta
        float size = Math.min(cardWidth, cardHeight) * 0.4f;

        renderEntity(graphics, centerX, centerY, (int)size, entityType, rotationTicks);
    }
}
