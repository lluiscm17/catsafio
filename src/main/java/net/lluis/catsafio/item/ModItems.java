package net.lluis.catsafio.item;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.block.ModBlocks;
import net.lluis.catsafio.entity.ModEntities;
import net.lluis.catsafio.item.armor.CascoInfernit;
import net.lluis.catsafio.item.armor.Hat;
import net.lluis.catsafio.item.armor.PartyHat;
import net.lluis.catsafio.item.armor.Suit;
import net.lluis.catsafio.item.custom.*;
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
    public static final RegistryObject<Item> OJO_DE_SAPO = ITEMS.register("ojo_de_sapo",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SANGRE_DE_GORGONA = ITEMS.register("sangre_de_gorgona",
            () -> new Item(new Item.Properties()));



    public static final RegistryObject<Item> RECIPE_BOOK = ITEMS.register("recipe_book",
            () -> new RecipeBookItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> INFERNIT_PICKAXE = ITEMS.register("infernit_pickaxe",
            () -> new PickaxeItem(ModToolTiers.INFERNIT, 4, 1, new Item.Properties()));
    public static final RegistryObject<Item> INFERNIT_STAFF = ITEMS.register("infernit_staff",
            () -> new SwordItem(ModToolTiers.INFERNIT, 10, 1f, new Item.Properties()));

    public static final RegistryObject<Item> AURALITA_SWORD = ITEMS.register("auralita_sword",
            () -> new SwordItem(ModToolTiers.AURALITA, 3, -2.4f, new Item.Properties()));
    public static final RegistryObject<Item> AURALITA_PICKAXE = ITEMS.register("auralita_pickaxe",
            () -> new PickaxeItem(ModToolTiers.AURALITA, 6, 1, new Item.Properties()));

    public static final RegistryObject<Item> BASTION_SWORD = ITEMS.register("bastion_sword",
            () -> new SwordItem(ModToolTiers.AURALITA, 3, -2.4f, new Item.Properties()));

    public static final RegistryObject<Item> RAYOLITA_SWORD = ITEMS.register("rayolita_sword",
            () -> new SwordItem(ModToolTiers.RAYOLITA, 3, -2.4f, new Item.Properties()));
    public static final RegistryObject<Item> RAYOLITA_PICKAXE = ITEMS.register("rayolita_pickaxe",
            () -> new PickaxeItem(ModToolTiers.RAYOLITA, 2, 1f, new Item.Properties()));

    public static final RegistryObject<Item> HEALING_SWORD = ITEMS.register("healing_sword",
            () -> new HealingSwordItem(ModToolTiers.HEALING_SWORD, 3, -2.4f, new Item.Properties()));

    public static final RegistryObject<Item> HEART = ITEMS.register("heart",
            () -> new HeartItem(new Item.Properties().stacksTo(8)));

    public static final RegistryObject<Item> PHANTOM_SWORD = ITEMS.register("phantom_sword",
            () -> new PhantomSwordItem(ModToolTiers.AURALITA, 3, -2.4f, new Item.Properties()));

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

    // SPAWN EGGS
    public static final RegistryObject<Item> INFERNAL_BULL_SPAWN_EGG = ITEMS.register("infernal_bull_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.INFERNAL_BULL, 0x7e9680, 0xc5d1c5,
                    new Item.Properties()));
    public static final RegistryObject<Item> TORTUGA_INFERNAL_SPAWN_EGG = ITEMS.register("tortuga_infernal_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.TORTUGA_INFERNAL, 0x7e9680, 0xc5d1c5,
                    new Item.Properties()));
    public static final RegistryObject<Item> CREEPER_CAKE_SPAWN_EGG = ITEMS.register("creepercake_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.CREEPER_CAKE, 0x88DD44, 0xFF4444,
                    new Item.Properties()));
    public static final RegistryObject<Item> SAPO_SPAWN_EGG = ITEMS.register("sapo_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.SAPO, 0x336622, 0x88FF44,
                    new Item.Properties()));
    public static final RegistryObject<Item> FANTASMA_SPAWN_EGG = ITEMS.register("fantasma_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.FANTASMA, 0xFF2222, 0xFF8888,
                    new Item.Properties()));
    public static final RegistryObject<Item> MURCIELAGO_WARDEN_SPAWN_EGG = ITEMS.register("murcielago_warden_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.MURCIELAGO_WARDEN, 0x1A0033, 0x6600AA,
                    new Item.Properties()));
    public static final RegistryObject<Item> ORB_SPAWN_EGG = ITEMS.register("orb_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.ORB, 0x6600FF, 0xAA44FF,
                    new Item.Properties()));
    public static final RegistryObject<Item> FLASHBANG_SPAWN_EGG = ITEMS.register("flashbang_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.FLASHBANG, 0xFFFF00, 0xFF8800,
                    new Item.Properties()));
    public static final RegistryObject<Item> WILDFIRE_SPAWN_EGG = ITEMS.register("wildfire_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.WILDFIRE, 0xFF6600, 0xFFDD00,
                    new Item.Properties()));
    public static final RegistryObject<Item> CREEPER_WARDEN_SPAWN_EGG = ITEMS.register("creeper_warden_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.CREEPER_WARDEN, 0x0F1419, 0x00FFAA,
                    new Item.Properties()));
    public static final RegistryObject<Item> GORGONA_SPAWN_EGG = ITEMS.register("gorgona_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.GORGONA,
                    0x4A4A4A,  // Color principal (gris oscuro)
                    0x8B008B,  // Color serpientes (morado)
                    new Item.Properties()));

    // Item destructor
    public static final RegistryObject<Item> TAXI_DESTRUCTOR = ITEMS.register("taxi_destructor",
            () -> new TaxiDestructorItem(new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.RARE)));

    // DEDITAS
    public static final RegistryObject<Item> ALA_DE_MURCIELAGO_WARDEN = ITEMS.register("ala_de_murcielago_warden",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DEDITA_NARANJA = ITEMS.register("dedita_naranja",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DEDITA_CELESTE = ITEMS.register("dedita_celeste",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DEDITA_AZUL = ITEMS.register("dedita_azul",
            () -> new Item(new Item.Properties()));

    // RUNA ITEMS (Item personalizado en lugar de spawn egg)
    public static final RegistryObject<Item> RUNA_SPAWN_ITEM = ITEMS.register("runa_spawn_item",
            () -> new RunaSpawnItem(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> RUNA_REMOVER = ITEMS.register("runa_remover",
            () -> new RunaRemoverItem(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> TAXI_VOLADOR_SPAWN = ITEMS.register("taxi_volador_spawn",
            () -> new TaxiVoladorSpawnItem(new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.EPIC)));

    // NEREIDA
    public static final RegistryObject<Item> LINGOTE_NEREIDA = ITEMS.register("lingote_nereida",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}