package net.lluis.catsafio.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lluis.catsafio.entity.custom.BombaAmarillaEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BombaAmarillaRenderer extends GeoEntityRenderer<BombaAmarillaEntity> {
    
    public BombaAmarillaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BombaAmarillaModel());
        this.shadowRadius = 0.4f;
    }

    @Override
    public void render(BombaAmarillaEntity entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        
        // Escala
        poseStack.scale(0.8f, 0.8f, 0.8f);
        
        // ROTAR 180 GRADOS en el eje X para que mire hacia arriba
        // El modelo mira hacia abajo por defecto, lo volteamos
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
