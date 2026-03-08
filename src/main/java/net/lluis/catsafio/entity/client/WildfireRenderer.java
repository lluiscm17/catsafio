package net.lluis.catsafio.entity.client;

import net.lluis.catsafio.entity.custom.WildfireEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WildfireRenderer extends GeoEntityRenderer<WildfireEntity> {

    public WildfireRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new WildfireModel());
    }
}
