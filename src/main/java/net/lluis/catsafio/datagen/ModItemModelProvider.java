package net.lluis.catsafio.datagen;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;

public class ModItemModelProvider extends ItemModelProvider {
    private static LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();
    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
    }

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Catsafio.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.INFERNIT);
        simpleItem(ModItems.RAW_INFERNIT);
        simpleItem(ModItems.METAL_DETECTOR);
        simpleItem(ModItems.INFERNIT_DETECTOR);
        simpleItem(ModItems.POKELITO);
        simpleItem(ModItems.INFERNIT_UPGRADE_SMITHING_TEMPLATE);
        simpleItem(ModItems.AURALITA);
        simpleItem(ModItems.RAW_AURALITA);
        simpleItem(ModItems.HORN);
        simpleItem(ModItems.AURALITA_UPGRADE_SMITHING_TEMPLATE);
        simpleItem(ModItems.RAYOLITA);
        simpleItem(ModItems.RAW_RAYOLITA);
        simpleItem(ModItems.CASINO_COIN_10);
        simpleItem(ModItems.CASINO_COIN_20);
        simpleItem(ModItems.CASINO_COIN_50);
        simpleItem(ModItems.CASINO_COIN_100);
        simpleItem(ModItems.PORRO);
        simpleItem(ModItems.OJO_DE_SAPO);
        simpleItem(ModItems.MARIGUANA_LEAF);
        simpleItem(ModItems.BRONZE_CATCOIN);
        simpleItem(ModItems.SILVER_CATCOIN);
        simpleItem(ModItems.GOLD_CATCOIN);
        simpleItem(ModItems.PLATINIUM_CATCOIN);
        simpleItem(ModItems.STONE_WORM);
        simpleItem(ModItems.COGOLLO);
        simpleItem(ModItems.ALIVE_POTATO);
        simpleItem(ModItems.RAYOLITA_UPGRADE_SMITHING_TEMPLATE);
        simpleItem(ModItems.COOKED_IRON);
        simpleItem(ModItems.MOTOR_DE_INFERNIT);
        simpleItem(ModItems.HEART);
        simpleItem(ModItems.ALA_DE_MURCIELAGO_WARDEN);
        simpleItem(ModItems.RUNA_SPAWN_ITEM);
        simpleItem(ModItems.RUNA_REMOVER);
        simpleItem(ModItems.SANGRE_DE_GORGONA);
        simpleItem(ModItems.LINGOTE_NEREIDA);

        handheldItem(ModItems.BASTION_SWORD);
        handheldItem(ModItems.HEALING_SWORD);
        handheldItem(ModItems.PHANTOM_SWORD);


        trimmedArmorItem(ModItems.INFERNIT_CHESTPLATE);
        trimmedArmorItem(ModItems.INFERNIT_LEGGINS);
        trimmedArmorItem(ModItems.INFERNIT_BOOTS);
        trimmedArmorItem(ModItems.HAT);
        trimmedArmorItem(ModItems.PARTY_HAT);
        trimmedArmorItem(ModItems.CASCO_INFERNIT);

        trimmedArmorItem(ModItems.AURALITA_HELMET);
        trimmedArmorItem(ModItems.AURALITA_CHESTPLATE);
        trimmedArmorItem(ModItems.AURALITA_LEGGINS);
        trimmedArmorItem(ModItems.AURALITA_BOOTS);

        trimmedArmorItem(ModItems.RAYOLITA_BOOTS);
        trimmedArmorItem(ModItems.RAYOLITA_LEGGINS);
        trimmedArmorItem(ModItems.RAYOLITA_CHESTPLATE);
        trimmedArmorItem(ModItems.RAYOLITA_HELMET);

        withExistingParent(ModItems.INFERNAL_BULL_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.TORTUGA_INFERNAL_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.FANTASMA_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.SAPO_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.CREEPER_CAKE_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.FLASHBANG_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.WILDFIRE_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.MURCIELAGO_WARDEN_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.ORB_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.CREEPER_WARDEN_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.GORGONA_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Catsafio.MOD_ID,"item/" + item.getId().getPath()));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(Catsafio.MOD_ID,"item/" + item.getId().getPath()));
    }
    private void trimmedArmorItem(RegistryObject<Item> itemRegistryObject) {
        final String MOD_ID = Catsafio.MOD_ID; // Change this to your mod id

        if(itemRegistryObject.get() instanceof ArmorItem armorItem) {
            trimMaterials.entrySet().forEach(entry -> {

                ResourceKey<TrimMaterial> trimMaterial = entry.getKey();
                float trimValue = entry.getValue();

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                String armorItemPath = "item/" + armorItem;
                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation armorItemResLoc = new ResourceLocation(MOD_ID, armorItemPath);
                ResourceLocation trimResLoc = new ResourceLocation(trimPath); // minecraft namespace
                ResourceLocation trimNameResLoc = new ResourceLocation(MOD_ID, currentTrimName);

                // This is used for making the ExistingFileHelper acknowledge that this texture exist, so this will
                // avoid an IllegalArgumentException
                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                // Trimmed armorItem files
                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc)
                        .texture("layer1", trimResLoc);

                // Non-trimmed armorItem file (normal variant)
                this.withExistingParent(itemRegistryObject.getId().getPath(),
                                mcLoc("item/generated"))
                        .override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResLoc))
                        .predicate(mcLoc("trim_type"), trimValue).end()
                        .texture("layer0",
                                new ResourceLocation(MOD_ID,
                                        "item/" + itemRegistryObject.getId().getPath()));
            });
        }
    }
}
