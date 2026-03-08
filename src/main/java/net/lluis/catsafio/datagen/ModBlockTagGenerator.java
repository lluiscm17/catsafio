package net.lluis.catsafio.datagen;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.block.ModBlocks;
import net.lluis.catsafio.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Catsafio.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ModTags.Blocks.METAL_DETECTOR_VALUABLES)
                .add(ModBlocks.INFERNIT_ORE.get());

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.INFERNIT_ORE.get(),
                        ModBlocks.RAW_INFERNIT_BLOCK.get(),
                        ModBlocks.AURALITA_ORE.get(),
                        ModBlocks.AURALITA_BLOCK.get(),
                        ModBlocks.RAYOLITA_ORE.get(),
                        ModBlocks.DEEPSLATE_RAYOLITA_ORE.get(),
                        ModBlocks.RAW_AURALITA_BLOCK.get(),
                        ModBlocks.HORNO_BIG.get(),
                        ModBlocks.COOKED_IRON_BLOCK.get(),
                        ModBlocks.CRISTAL_REFORZADO.get(),
                        ModBlocks.INFERNIT_BLOCK.get()
                );

        this.tag(Tags.Blocks.NEEDS_NETHERITE_TOOL)
                .add(ModBlocks.RAYOLITA_ORE.get(),
                        ModBlocks.AURALITA_BLOCK.get(),
                        ModBlocks.INFERNIT_ORE.get(),
                        ModBlocks.INFERNIT_BLOCK.get(),
                        ModBlocks.RAW_INFERNIT_BLOCK.get(),
                        ModBlocks.DEEPSLATE_RAYOLITA_ORE.get(),
                        ModBlocks.RAW_AURALITA_BLOCK.get(),
                        ModBlocks.AURALITA_ORE.get(),
                        ModBlocks.COOKED_IRON_BLOCK.get(),
                        ModBlocks.CRISTAL_REFORZADO.get(),
                        ModBlocks.HORNO_BIG.get()
                );

    }
}
