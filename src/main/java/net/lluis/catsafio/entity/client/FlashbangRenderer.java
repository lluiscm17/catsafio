package net.lluis.catsafio.entity.client;

import net.lluis.catsafio.entity.custom.FlashbangEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FlashbangRenderer extends GeoEntityRenderer<FlashbangEntity> {

    public FlashbangRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new FlashbangModel());
    }
}
