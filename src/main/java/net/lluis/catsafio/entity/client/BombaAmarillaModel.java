package net.lluis.catsafio.entity.client;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.entity.custom.BombaAmarillaEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BombaAmarillaModel extends GeoModel<BombaAmarillaEntity> {
    
    @Override
    public ResourceLocation getModelResource(BombaAmarillaEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "geo/bomba_amarilla.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BombaAmarillaEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "textures/entity/bomba_amarilla.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BombaAmarillaEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "animations/bomba_amarilla.animation.json");
    }
}
