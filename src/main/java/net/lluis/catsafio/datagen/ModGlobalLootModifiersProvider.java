package net.lluis.catsafio.datagen;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.item.ModItems;
import net.lluis.catsafio.loot.AddItemModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, Catsafio.MOD_ID);
    }

    @Override
    protected void start(){
        add("stone_worm_from_stone", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.STONE).build(),
                LootItemRandomChanceCondition.randomChance(0.05f).build()}, ModItems.STONE_WORM.get()));

        add("alive_potato_from_potato", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.POTATOES).build(),
                LootItemRandomChanceCondition.randomChance(0.05f).build()}, ModItems.ALIVE_POTATO.get()));

        add("infernit_upgrade_smithing_template_from_nether_bridge", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/nether_bridge")).build(), LootItemRandomChanceCondition.randomChance(0.01f).build()}, ModItems.INFERNIT_UPGRADE_SMITHING_TEMPLATE.get()));

        add("rayolita_upgrade_smithing_template_from_nether_bridge", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/nether_bridge")).build(), LootItemRandomChanceCondition.randomChance(0.01f).build()}, ModItems.RAYOLITA_UPGRADE_SMITHING_TEMPLATE.get()));

        add("infernit_upgrade_smithing_template_from_nether_bridge", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/nether_bridge")).build(), LootItemRandomChanceCondition.randomChance(0.01f).build()}, ModItems.AURALITA_UPGRADE_SMITHING_TEMPLATE.get()));

        add("bastion_sword_from_ancient_city", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/ancient_city")).build(), LootItemRandomChanceCondition.randomChance(0.3f).build()}, ModItems.BASTION_SWORD.get()));
    }
}
