package net.lluis.catsafio.entity.client;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.entity.custom.TaxiVoladorEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TaxiVoladorModel extends GeoModel<TaxiVoladorEntity> {
    
    @Override
    public ResourceLocation getModelResource(TaxiVoladorEntity animatable) {
        return new ResourceLocation(Catsafio.MOD_ID, "geo/taxi_volador.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TaxiVoladorEntity animatable) {
        return new ResourceLocation(Catsafio.MOD_ID, "textures/entity/taxi_volador.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TaxiVoladorEntity animatable) {
        return new ResourceLocation(Catsafio.MOD_ID, "animations/taxi_volador.animation.json");
    }
}
