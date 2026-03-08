package net.lluis.catsafio.entity.client;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.entity.custom.FlashbangEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FlashbangModel extends GeoModel<FlashbangEntity> {

    @Override
    public ResourceLocation getModelResource(FlashbangEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "geo/flashbang.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FlashbangEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "textures/entity/flashbang.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FlashbangEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "animations/flashbang.animation.json");
    }
}
