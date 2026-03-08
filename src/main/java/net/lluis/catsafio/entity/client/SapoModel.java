package net.lluis.catsafio.entity.client;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.entity.custom.SapoEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SapoModel extends GeoModel<SapoEntity> {

    @Override
    public ResourceLocation getModelResource(SapoEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "geo/sapo.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SapoEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "textures/entity/sapo.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SapoEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "animations/sapo.animation.json");
    }
}
