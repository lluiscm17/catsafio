package net.lluis.catsafio.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.entity.custom.InfernalBullEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class InfernalBullRenderer extends MobRenderer<InfernalBullEntity, InfernalBullModel<InfernalBullEntity>> {
    public InfernalBullRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new InfernalBullModel<>(pContext.bakeLayer(ModModelLayers.INFERNAL_BULL_LAYER)), 2f);
    }

    @Override
    public ResourceLocation getTextureLocation(InfernalBullEntity infernalBullEntity) {
        return new ResourceLocation(Catsafio.MOD_ID, "textures/entity/infernal_bull.png");
    }

    @Override
    public void render(InfernalBullEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()) {
            pMatrixStack.scale(0.5f, 0.5f, 0.5f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
