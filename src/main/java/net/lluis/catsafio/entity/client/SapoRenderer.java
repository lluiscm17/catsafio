package net.lluis.catsafio.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lluis.catsafio.entity.custom.SapoEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SapoRenderer extends GeoEntityRenderer<SapoEntity> {

    public SapoRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new SapoModel());
    }

    @Override
    public void render(SapoEntity entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        int swallowedId = entity.getSwallowedEntityId();
        if (swallowedId == -1) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        Entity found = mc.level.getEntity(swallowedId);
        // AbstractClientPlayer es el tipo correcto en cliente para jugadores
        if (!(found instanceof AbstractClientPlayer clientPlayer)) return;

        // getRenderer devuelve EntityRenderer<? super AbstractClientPlayer>
        // hay que obtenerlo del skin type para tener el PlayerRenderer correcto
        PlayerRenderer playerRenderer = (PlayerRenderer) mc.getEntityRenderDispatcher()
                .getSkinMap().get(clientPlayer.getModelName());
        if (playerRenderer == null) return;

        poseStack.pushPose();
        poseStack.translate(0.0D, 0.8D, 0.0D);
        poseStack.scale(0.55F, 0.55F, 0.55F);
        playerRenderer.render(clientPlayer, 0.0F, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}