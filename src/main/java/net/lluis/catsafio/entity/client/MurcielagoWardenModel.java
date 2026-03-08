package net.lluis.catsafio.entity.client;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.entity.custom.MurcielagoWardenEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MurcielagoWardenModel extends GeoModel<MurcielagoWardenEntity> {

    @Override
    public ResourceLocation getModelResource(MurcielagoWardenEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "geo/murcielago_warden.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MurcielagoWardenEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "textures/entity/murcielago_warden.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MurcielagoWardenEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "animations/murcielago_warden.animation.json");
    }
}
