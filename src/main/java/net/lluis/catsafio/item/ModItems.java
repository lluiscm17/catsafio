package net.lluis.catsafio.item;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.block.ModBlocks;
import net.lluis.catsafio.entity.ModEntities;
import net.lluis.catsafio.item.armor.CascoInfernit;
import net.lluis.catsafio.item.armor.Hat;
import net.lluis.catsafio.item.armor.PartyHat;
import net.lluis.catsafio.item.armor.Suit;
import net.lluis.catsafio.item.custom.InfernitDetectorItem;
import net.lluis.catsafio.item.custom.MetalDetectorItem;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Catsafio.MOD_ID);

    public static final RegistryObject<Item> COOKED_IRON = ITEMS.register("cooked_iron",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> INFERNIT = ITEMS.register("infernit",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_INFERNIT = ITEMS.register("raw_infernit",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_AURALITA = ITEMS.register("raw_auralita",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> AURALITA = ITEMS.register("auralita",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MOTOR_DE_INFERNIT = ITEMS.register("motor_de_infernit",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CASINO_COIN_10 = ITEMS.register("casino_coin_10",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CASINO_COIN_20 = ITEMS.register("casino_coin_20",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CASINO_COIN_50 = ITEMS.register("casino_coin_50",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CASINO_COIN_100 = ITEMS.register("casino_coin_100",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORRO = ITEMS.register("porro",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MARIGUANA_LEAF = ITEMS.register("mariguana_leaf",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HORN = ITEMS.register("horn",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> AURALITA_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("auralita_upgrade_smithing_template",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> INFERNIT_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("infernit_upgrade_smithing_template",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAYOLITA_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("rayolita_upgrade_smithing_template",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> METAL_DETECTOR = ITEMS.register("metal_detector",
            () -> new MetalDetectorItem(new Item.Properties().durability(100)));

    public static final RegistryObject<Item> RAYOLITA = ITEMS.register("rayolita",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_RAYOLITA = ITEMS.register("raw_rayolita",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BRONZE_CATCOIN = ITEMS.register("bronze_catcoin",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILVER_CATCOIN = ITEMS.register("silver_catcoin",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_CATCOIN = ITEMS.register("gold_catcoin",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PLATINIUM_CATCOIN = ITEMS.register("platinium_catcoin",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ALIVE_POTATO = ITEMS.register("alive_potato",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COGOLLO = ITEMS.register("cogollo",
            () -> new ItemNameBlockItem(ModBlocks.MARIHUANA_CROP.get(), new Item.Properties()));

    public static final RegistryObject<Item> STONE_WORM = ITEMS.register("stone_worm",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> INFERNIT_PICKAXE = ITEMS.register("infernit_pickaxe",
            () -> new PickaxeItem(ModToolTiers.INFERNIT, 4, 1, new Item.Properties()));
    public static final RegistryObject<Item> INFERNIT_STAFF = ITEMS.register("infernit_staff",
            () -> new SwordItem(ModToolTiers.INFERNIT, 10, 1f, new Item.Properties()));

    public static final RegistryObject<Item> AURALITA_SWORD = ITEMS.register("auralita_sword",
            () -> new SwordItem(ModToolTiers.AURALITA, 12, 1f, new Item.Properties()));
    public static final RegistryObject<Item> AURALITA_PICKAXE = ITEMS.register("auralita_pickaxe",
            () -> new PickaxeItem(ModToolTiers.AURALITA, 6, 1, new Item.Properties()));

    public static final RegistryObject<Item> BASTION_SWORD = ITEMS.register("bastion_sword",
            () -> new SwordItem(ModToolTiers.AURALITA, 7, 1f, new Item.Properties()));

    public static final RegistryObject<Item> RAYOLITA_SWORD = ITEMS.register("rayolita_sword",
            () -> new SwordItem(ModToolTiers.RAYOLITA, 8, 1f, new Item.Properties()));
    public static final RegistryObject<Item> RAYOLITA_PICKAXE = ITEMS.register("rayolita_pickaxe",
            () -> new PickaxeItem(ModToolTiers.RAYOLITA, 2, 1f, new Item.Properties()));

    public static final RegistryObject<Item> HEALING_SWORD = ITEMS.register("healing_sword",
            () -> new SwordItem(ModToolTiers.AURALITA, 6, 0.00001f, new Item.Properties()));

    public static final RegistryObject<Item> INFERNIT_DETECTOR = ITEMS.register("infernit_detector",
            () -> new InfernitDetectorItem(new Item.Properties().durability(100)));
    public static final RegistryObject<Item> POKELITO = ITEMS.register("pokelito",
            () -> new Item(new Item.Properties().food(ModFoods.POKELITO)));

    public static final RegistryObject<Item> INFERNIT_CHESTPLATE = ITEMS.register("infernit_chestplate",
            () -> new ArmorItem(ModArmorMaterials.INFERNIT, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> INFERNIT_LEGGINS = ITEMS.register("infernit_leggins",
            () -> new ArmorItem(ModArmorMaterials.INFERNIT, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> INFERNIT_BOOTS = ITEMS.register("infernit_boots",
            () -> new ArmorItem(ModArmorMaterials.INFERNIT, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> AURALITA_HELMET = ITEMS.register("auralita_helmet",
            () -> new ArmorItem(ModArmorMaterials.AURALITA, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> AURALITA_CHESTPLATE = ITEMS.register("auralita_chestplate",
            () -> new ArmorItem(ModArmorMaterials.AURALITA, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> AURALITA_LEGGINS = ITEMS.register("auralita_leggins",
            () -> new ArmorItem(ModArmorMaterials.AURALITA, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> AURALITA_BOOTS = ITEMS.register("auralita_boots",
            () -> new ArmorItem(ModArmorMaterials.AURALITA, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> RAYOLITA_HELMET = ITEMS.register("rayolita_helmet",
            () -> new ArmorItem(ModArmorMaterials.RAYOLITA, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> RAYOLITA_CHESTPLATE = ITEMS.register("rayolita_chestplate",
            () -> new ArmorItem(ModArmorMaterials.RAYOLITA, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> RAYOLITA_LEGGINS = ITEMS.register("rayolita_leggins",
            () -> new ArmorItem(ModArmorMaterials.RAYOLITA, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> RAYOLITA_BOOTS = ITEMS.register("rayolita_boots",
            () -> new ArmorItem(ModArmorMaterials.RAYOLITA, ArmorItem.Type.BOOTS, new Item.Properties()));


    public static final RegistryObject<Item> CASCO_INFERNIT = ITEMS.register("casco_infernit",
            () -> new CascoInfernit(ModArmorMaterials.INFERNIT, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<Item> HAT = ITEMS.register("hat",
            () -> new Hat(ModArmorMaterials.HAT, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> SUIT = ITEMS.register("suit",
            () -> new Suit(ModArmorMaterials.HAT, ArmorItem.Type.CHESTPLATE, new Item.Properties()));


    public static final RegistryObject<Item> PARTY_HAT = ITEMS.register("party_hat",
            () -> new PartyHat(ModArmorMaterials.HAT, ArmorItem.Type.HELMET, new Item.Properties()));



    public static final RegistryObject<Item> INFERNAL_BULL_SPAWN_EGG = ITEMS.register("infernal_bull_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.INFERNAL_BULL, 0x7e9680, 0xc5d1c5,
                    new Item.Properties()));
    public static final RegistryObject<Item> TORTUGA_INFERNAL_SPAWN_EGG = ITEMS.register("tortuga_infernal_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.TORTUGA_INFERNAL, 0x7e9680, 0xc5d1c5,
                    new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
