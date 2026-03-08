package net.lluis.catsafio.entity.client;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.entity.custom.GorgonaEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GorgonaModel extends GeoModel<GorgonaEntity> {
    
    @Override
    public ResourceLocation getModelResource(GorgonaEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "geo/gorgona.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GorgonaEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "textures/entity/gorgona.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GorgonaEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "animations/gorgona.animation.json");
    }
}
