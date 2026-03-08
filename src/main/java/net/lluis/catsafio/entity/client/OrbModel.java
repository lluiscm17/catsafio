package net.lluis.catsafio.entity.client;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.entity.custom.OrbEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class OrbModel extends GeoModel<OrbEntity> {

    @Override
    public ResourceLocation getModelResource(OrbEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "geo/orb.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(OrbEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "textures/entity/orb.png");
    }

    @Override
    public ResourceLocation getAnimationResource(OrbEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "animations/orb.animation.json");
    }
}
