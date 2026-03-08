package net.lluis.catsafio.entity.client;

import net.lluis.catsafio.entity.custom.CreeperCakeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CreeperCakeRenderer extends GeoEntityRenderer<CreeperCakeEntity> {
    public CreeperCakeRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new CreeperCakeModel());
    }
}
