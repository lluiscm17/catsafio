package net.lluis.catsafio.entity.client;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.entity.custom.FantasmaEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FantasmaModel extends GeoModel<FantasmaEntity> {

    @Override
    public ResourceLocation getModelResource(FantasmaEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "geo/fantasma.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FantasmaEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "textures/entity/fantasma.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FantasmaEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "animations/fantasma.animation.json");
    }
}
