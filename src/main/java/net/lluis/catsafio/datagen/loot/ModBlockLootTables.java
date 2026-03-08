package net.lluis.catsafio.datagen.loot;

import net.lluis.catsafio.block.ModBlocks;
import net.lluis.catsafio.block.custom.MarihuanaCropBlock;
import net.lluis.catsafio.item.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.INFERNIT_BLOCK.get());
        this.dropSelf(ModBlocks.RAW_INFERNIT_BLOCK.get());
        this.dropSelf(ModBlocks.AURALITA_BLOCK.get());
        this.dropSelf(ModBlocks.RAW_AURALITA_BLOCK.get());
        this.dropSelf(ModBlocks.HORNO_BIG.get());
        this.dropSelf(ModBlocks.COOKED_IRON_BLOCK.get());
        this.dropSelf(ModBlocks.CRISTAL_REFORZADO.get());

        this.add(ModBlocks.INFERNIT_ORE.get(),
                block -> createCopperLikeOreDrops(ModBlocks.INFERNIT_ORE.get(), ModItems.RAW_INFERNIT.get()));
        this.add(ModBlocks.AURALITA_ORE.get(),
                block -> createCopperLikeOreDrops(ModBlocks.AURALITA_ORE.get(), ModItems.RAW_AURALITA.get()));
        this.add(ModBlocks.RAYOLITA_ORE.get(),
                block -> createCopperLikeOreDrops(ModBlocks.RAYOLITA_ORE.get(), ModItems.RAW_RAYOLITA.get()));
        this.add(ModBlocks.DEEPSLATE_RAYOLITA_ORE.get(),
                block -> createCopperLikeOreDrops(ModBlocks.DEEPSLATE_RAYOLITA_ORE.get(), ModItems.RAW_RAYOLITA.get()));

        LootItemCondition.Builder lootitemcondition$builder2 = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(ModBlocks.MARIHUANA_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(MarihuanaCropBlock.AGE, 8));

        // LootItemCondition.Builder lootitemcondition$builder2 = LootItemBlockStatePropertyCondition
        //         .hasBlockStateProperties(ModBlocks.CORN_CROP.get())
        //         .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CornCropBlock.AGE, 8));

        this.add(ModBlocks.MARIHUANA_CROP.get(), createCropDrops(ModBlocks.MARIHUANA_CROP.get(), ModItems.MARIGUANA_LEAF.get(),
                ModItems.COGOLLO.get(), lootitemcondition$builder2));

        // NEREIDA
        this.dropSelf(ModBlocks.BLOQUE_NEREIDA.get());
        this.add(ModBlocks.MENA_NEREIDA.get(),
                block -> createNereidaOreDrops(ModBlocks.MENA_NEREIDA.get(), ModItems.LINGOTE_NEREIDA.get()));
    }

    // Drops 1-3 lingotes con fortune, bloque entero con silk touch
    protected LootTable.Builder createNereidaOreDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock,
                        LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    protected LootTable.Builder createCopperLikeOreDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock,
                        LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 1.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
