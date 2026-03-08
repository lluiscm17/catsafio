package net.lluis.catsafio.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lluis.catsafio.entity.custom.RunaEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RunaRenderer extends GeoEntityRenderer<RunaEntity> {

    public RunaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RunaModel());
    }

    @Override
    public void render(RunaEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                      MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(1.0f, 1.0f, 1.0f);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}
