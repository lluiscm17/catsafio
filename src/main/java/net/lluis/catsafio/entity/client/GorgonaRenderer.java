package net.lluis.catsafio.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lluis.catsafio.entity.custom.GorgonaEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GorgonaRenderer extends GeoEntityRenderer<GorgonaEntity> {
    
    public GorgonaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GorgonaModel());
        this.shadowRadius = 0.7f;
    }

    @Override
    public void render(GorgonaEntity entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        
        // Escala según tamaño del modelo
        poseStack.scale(1.0f, 1.0f, 1.0f);
        
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
