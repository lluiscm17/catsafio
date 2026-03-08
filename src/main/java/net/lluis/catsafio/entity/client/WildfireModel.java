package net.lluis.catsafio.entity.client;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.entity.custom.WildfireEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WildfireModel extends GeoModel<WildfireEntity> {

    @Override
    public ResourceLocation getModelResource(WildfireEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "geo/wildfire.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WildfireEntity entity) {
        // Cambia textura según escudos restantes (0=4 escudos, 4=sin escudos)
        int shields = entity.getShieldCount();
        return switch (shields) {
            case 0 -> new ResourceLocation(Catsafio.MOD_ID, "textures/entity/wildfire_4.png");
            case 1 -> new ResourceLocation(Catsafio.MOD_ID, "textures/entity/wildfire_3.png");
            case 2 -> new ResourceLocation(Catsafio.MOD_ID, "textures/entity/wildfire_2.png");
            case 3 -> new ResourceLocation(Catsafio.MOD_ID, "textures/entity/wildfire_1.png");
            default -> new ResourceLocation(Catsafio.MOD_ID, "textures/entity/wildfire_0.png");
        };
    }

    @Override
    public ResourceLocation getAnimationResource(WildfireEntity entity) {
        return new ResourceLocation(Catsafio.MOD_ID, "animations/wildfire.animation.json");
    }
}
