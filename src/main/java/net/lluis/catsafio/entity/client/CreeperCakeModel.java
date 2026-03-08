package net.lluis.catsafio.entity.client;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.entity.custom.CreeperCakeEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CreeperCakeModel extends GeoModel<CreeperCakeEntity> {

    @Override
    public ResourceLocation getModelResource(CreeperCakeEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "geo/creepercake.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CreeperCakeEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "textures/entity/creepercake.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CreeperCakeEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "animations/creepercake.animation.json");
    }
}
