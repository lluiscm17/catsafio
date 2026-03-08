package net.lluis.catsafio.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lluis.catsafio.entity.custom.CreeperWardenEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CreeperWardenRenderer extends GeoEntityRenderer<CreeperWardenEntity> {

    public CreeperWardenRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CreeperWardenModel());
        this.shadowRadius = 0.5F;
    }

    @Override
    public void render(CreeperWardenEntity entity, float entityYaw, float partialTick, 
                      PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        
        // Efecto de hinchazón antes de explotar
        float swelling = entity.getSwelling(partialTick);
        float scale = 1.0F + swelling * 0.4F; // Se hincha hasta 1.4x
        
        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        
        poseStack.popPose();
    }

    @Override
    protected float getDeathMaxRotation(CreeperWardenEntity entity) {
        return 0.0F; // No rotar al morir (explota antes)
    }
}
