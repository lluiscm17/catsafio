package net.lluis.catsafio.entity.client;

import net.lluis.catsafio.entity.custom.TaxiVoladorEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TaxiVoladorRenderer extends GeoEntityRenderer<TaxiVoladorEntity> {
    
    public TaxiVoladorRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TaxiVoladorModel());
    }
}
