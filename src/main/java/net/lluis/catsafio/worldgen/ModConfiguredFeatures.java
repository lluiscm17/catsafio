package net.lluis.catsafio.worldgen;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> INFERNIT_ORE_KEY = registerKey("infernit_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> RAYOLITA_ORE_KEY = registerKey("rayolita_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> AURALITA_ORE_KEY = registerKey("auralita_ore");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest netherrackReplacables = new BlockMatchTest(Blocks.NETHERRACK);
        RuleTest endReplaceables = new BlockMatchTest(Blocks.END_STONE);

        List<OreConfiguration.TargetBlockState> rayolitaOres = List.of(OreConfiguration.target(stoneReplaceable,
                ModBlocks.RAYOLITA_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, ModBlocks.DEEPSLATE_RAYOLITA_ORE.get().defaultBlockState()));

        register(context, RAYOLITA_ORE_KEY, Feature.ORE, new OreConfiguration(rayolitaOres, 9));
        register(context, INFERNIT_ORE_KEY, Feature.ORE, new OreConfiguration(netherrackReplacables,
                ModBlocks.INFERNIT_ORE.get().defaultBlockState(), 9));
        register(context, AURALITA_ORE_KEY, Feature.ORE, new OreConfiguration(endReplaceables,
                ModBlocks.AURALITA_ORE.get().defaultBlockState(), 9));

    }


    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Catsafio.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
