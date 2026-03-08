package net.lluis.catsafio.entity.client;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.entity.custom.RunaEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RunaModel extends GeoModel<RunaEntity> {
    
    @Override
    public ResourceLocation getModelResource(RunaEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "geo/runa.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RunaEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "textures/entity/runa.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RunaEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "animations/runa.animation.json");
    }
}
