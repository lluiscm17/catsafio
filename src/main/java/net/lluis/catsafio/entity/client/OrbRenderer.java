package net.lluis.catsafio.entity.client;

import net.lluis.catsafio.entity.custom.OrbEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class OrbRenderer extends GeoEntityRenderer<OrbEntity> {

    public OrbRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new OrbModel());
    }
}
