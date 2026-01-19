package net.lluis.catsafio.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.entity.custom.InfernalBullEntity;
import net.lluis.catsafio.entity.custom.TortugaInfernalEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class TortugaInfernalRenderer extends MobRenderer<TortugaInfernalEntity, TortugaInfernalModel<TortugaInfernalEntity>> {
    public TortugaInfernalRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new TortugaInfernalModel<>(pContext.bakeLayer(ModModelLayers.TORTUGA_INFERNAL_LAYER)), 1.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(TortugaInfernalEntity pEntity) {
        return new ResourceLocation(Catsafio.MOD_ID, "textures/entity/tortuga_infernal.png");
    }

    public void render(TortugaInfernalEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()) {
            pMatrixStack.scale(0.5f, 0.5f, 0.5f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
