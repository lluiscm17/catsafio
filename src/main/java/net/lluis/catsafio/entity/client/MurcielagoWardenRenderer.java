package net.lluis.catsafio.entity.client;

import net.lluis.catsafio.entity.custom.MurcielagoWardenEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MurcielagoWardenRenderer extends GeoEntityRenderer<MurcielagoWardenEntity> {

    public MurcielagoWardenRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new MurcielagoWardenModel());
    }
}
