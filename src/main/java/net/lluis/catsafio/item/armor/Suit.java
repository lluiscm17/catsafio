package net.lluis.catsafio.item.armor;

import net.lluis.catsafio.client.model.CustomSuit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

public class Suit extends ArmorItem {
    public Suit(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel<LivingEntity> getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
                HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
                        Map.of("head", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                "body", new CustomSuit(Minecraft.getInstance().getEntityModels().bakeLayer(CustomSuit.LAYER_LOCATION)).body,
                                "right_arm", new CustomSuit(Minecraft.getInstance().getEntityModels().bakeLayer(CustomSuit.LAYER_LOCATION)).right_arm,
                                "left_arm", new CustomSuit(Minecraft.getInstance().getEntityModels().bakeLayer(CustomSuit.LAYER_LOCATION)).left_arm,
                                "right_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                "left_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
                        return armorModel;
            }
        });
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "catsafio:textures/entity/suit.png";
    }
}
