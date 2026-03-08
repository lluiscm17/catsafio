package net.lluis.catsafio.entity.client;

import net.lluis.catsafio.entity.custom.FantasmaEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FantasmaRenderer extends GeoEntityRenderer<FantasmaEntity> {

    public FantasmaRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new FantasmaModel());
    }

    @Override
    public RenderType getRenderType(FantasmaEntity entity, ResourceLocation texture,
                                    MultiBufferSource bufferSource, float partialTick) {
        // Render translúcido para efecto fantasmal
        return RenderType.entityTranslucent(texture);
    }
}
