package net.lluis.catsafio.entity.client;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.entity.custom.CreeperWardenEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CreeperWardenModel extends GeoModel<CreeperWardenEntity> {
    
    @Override
    public ResourceLocation getModelResource(CreeperWardenEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "geo/creeper_warden.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CreeperWardenEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "textures/entity/creeper_warden.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CreeperWardenEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "animations/creeper_warden.animation.json");
    }
}
