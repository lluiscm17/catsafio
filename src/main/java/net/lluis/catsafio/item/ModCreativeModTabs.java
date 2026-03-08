package net.lluis.catsafio.item;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Catsafio.MOD_ID);

    public static final RegistryObject<CreativeModeTab> CATSAFIO_TAB = CREATIVE_MODE_TABS.register("catsafio_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.INFERNIT.get()))
                    .title(Component.translatable("creativetab.catsasfio_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.INFERNIT.get());
                        pOutput.accept(ModItems.RAW_INFERNIT.get());
                        pOutput.accept(ModItems.RAW_AURALITA.get());
                        pOutput.accept(ModItems.AURALITA.get());
                        pOutput.accept(ModItems.RAYOLITA.get());
                        pOutput.accept(ModItems.RAW_RAYOLITA.get());
                        pOutput.accept(ModItems.COOKED_IRON.get());
                        pOutput.accept(ModItems.MOTOR_DE_INFERNIT.get());
                        pOutput.accept(ModItems.INFERNIT_UPGRADE_SMITHING_TEMPLATE.get());
                        pOutput.accept(ModItems.AURALITA_UPGRADE_SMITHING_TEMPLATE.get());
                        pOutput.accept(ModItems.RAYOLITA_UPGRADE_SMITHING_TEMPLATE.get());
                        pOutput.accept(ModItems.CASINO_COIN_10.get());
                        pOutput.accept(ModItems.CASINO_COIN_100.get());
                        pOutput.accept(ModItems.CASINO_COIN_20.get());
                        pOutput.accept(ModItems.CASINO_COIN_50.get());
                        pOutput.accept(ModItems.COGOLLO.get());
                        pOutput.accept(ModItems.PORRO.get());
                        pOutput.accept(ModItems.ALIVE_POTATO.get());
                        pOutput.accept(ModItems.MARIGUANA_LEAF.get());
                        pOutput.accept(ModItems.BRONZE_CATCOIN.get());
                        pOutput.accept(ModItems.SILVER_CATCOIN.get());
                        pOutput.accept(ModItems.GOLD_CATCOIN.get());
                        pOutput.accept(ModItems.PLATINIUM_CATCOIN.get());
                        pOutput.accept(ModItems.METAL_DETECTOR.get());
                        pOutput.accept(ModItems.POKELITO.get());
                        pOutput.accept(ModItems.INFERNIT_DETECTOR.get());
                        pOutput.accept(ModItems.INFERNIT_PICKAXE.get());
                        pOutput.accept(ModItems.AURALITA_PICKAXE.get());
                        pOutput.accept(ModItems.RAYOLITA_PICKAXE.get());
                        pOutput.accept(ModItems.AURALITA_SWORD.get());
                        pOutput.accept(ModItems.RAYOLITA_SWORD.get());
                        pOutput.accept(ModItems.BASTION_SWORD.get());
                        pOutput.accept(ModItems.HEALING_SWORD.get());
                        pOutput.accept(ModItems.PHANTOM_SWORD.get());
                        pOutput.accept(ModItems.INFERNIT_STAFF.get());
                        pOutput.accept(ModItems.INFERNAL_BULL_SPAWN_EGG.get());
                        pOutput.accept(ModItems.TORTUGA_INFERNAL_SPAWN_EGG.get());
                        pOutput.accept(ModItems.FANTASMA_SPAWN_EGG.get());
                        pOutput.accept(ModItems.CREEPER_CAKE_SPAWN_EGG.get());
                        pOutput.accept(ModItems.SAPO_SPAWN_EGG.get());
                        pOutput.accept(ModItems.HEART.get());
                        pOutput.accept(ModItems.HORN.get());
                        pOutput.accept(ModItems.STONE_WORM.get());
                        pOutput.accept(ModItems.SANGRE_DE_GORGONA.get());
                        pOutput.accept(ModItems.RECIPE_BOOK.get());
                        pOutput.accept(ModItems.OJO_DE_SAPO.get());
                        pOutput.accept(ModItems.DEDITA_CELESTE.get());
                        pOutput.accept(ModItems.DEDITA_NARANJA.get());
                        pOutput.accept(ModItems.DEDITA_AZUL.get());
                        pOutput.accept(ModItems.ALA_DE_MURCIELAGO_WARDEN.get());
                        pOutput.accept(ModItems.MURCIELAGO_WARDEN_SPAWN_EGG.get());
                        pOutput.accept(ModItems.ORB_SPAWN_EGG.get());
                        pOutput.accept(ModItems.FLASHBANG_SPAWN_EGG.get());
                        pOutput.accept(ModItems.RUNA_SPAWN_ITEM.get());
                        pOutput.accept(ModItems.RUNA_REMOVER.get());
                        pOutput.accept(ModItems.WILDFIRE_SPAWN_EGG.get());
                        pOutput.accept(ModItems.CREEPER_WARDEN_SPAWN_EGG.get());
                        pOutput.accept(ModItems.GORGONA_SPAWN_EGG.get());

                        pOutput.accept(ModItems.RAYOLITA_BOOTS.get());
                        pOutput.accept(ModItems.RAYOLITA_LEGGINS.get());
                        pOutput.accept(ModItems.RAYOLITA_CHESTPLATE.get());
                        pOutput.accept(ModItems.RAYOLITA_HELMET.get());

                        pOutput.accept(ModItems.CASCO_INFERNIT.get());
                        pOutput.accept(ModItems.INFERNIT_CHESTPLATE.get());
                        pOutput.accept(ModItems.INFERNIT_LEGGINS.get());
                        pOutput.accept(ModItems.INFERNIT_BOOTS.get());

                        pOutput.accept(ModItems.AURALITA_HELMET.get());
                        pOutput.accept(ModItems.AURALITA_CHESTPLATE.get());
                        pOutput.accept(ModItems.AURALITA_LEGGINS.get());
                        pOutput.accept(ModItems.AURALITA_BOOTS.get());

                        pOutput.accept(ModBlocks.INFERNIT_BLOCK.get());
                        pOutput.accept(ModBlocks.RAW_INFERNIT_BLOCK.get());
                        pOutput.accept(ModBlocks.INFERNIT_ORE.get());

                        pOutput.accept(ModBlocks.RAYOLITA_ORE.get());
                        pOutput.accept(ModBlocks.DEEPSLATE_RAYOLITA_ORE.get());

                        pOutput.accept(ModBlocks.AURALITA_ORE.get());
                        pOutput.accept(ModBlocks.AURALITA_BLOCK.get());
                        pOutput.accept(ModBlocks.RAW_AURALITA_BLOCK.get());
                        pOutput.accept(ModBlocks.COOKED_IRON_BLOCK.get());
                        pOutput.accept(ModBlocks.CRISTAL_REFORZADO.get());


                        pOutput.accept(ModBlocks.HORNO_BIG.get());
                        pOutput.accept(ModItems.HAT.get());
                        pOutput.accept(ModItems.SUIT.get());
                        pOutput.accept(ModItems.PARTY_HAT.get());



                    })
                    .build());
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
