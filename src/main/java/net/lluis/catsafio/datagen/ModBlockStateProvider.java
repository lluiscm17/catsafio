package net.lluis.catsafio.datagen;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.block.ModBlocks;
import net.lluis.catsafio.block.custom.MarihuanaCropBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Catsafio.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.INFERNIT_BLOCK);
        blockWithItem(ModBlocks.RAW_INFERNIT_BLOCK);
        blockWithItem(ModBlocks.AURALITA_BLOCK);
        blockWithItem(ModBlocks.RAW_AURALITA_BLOCK);

        blockWithItem(ModBlocks.INFERNIT_ORE);
        blockWithItem(ModBlocks.AURALITA_ORE);

        blockWithItem(ModBlocks.COOKED_IRON_BLOCK);

        blockWithItem(ModBlocks.RAYOLITA_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_RAYOLITA_ORE);

        makeMarihuanaCrop(((CropBlock) ModBlocks.MARIHUANA_CROP.get()), "marihuana_stage_", "marihuana_stage_");

        simpleBlock(ModBlocks.HORNO_BIG.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/horno_porno")));
    }


    public void makeMarihuanaCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> cornStates(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] cornStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((MarihuanaCropBlock) block).getAgeProperty()),
                new ResourceLocation(Catsafio.MOD_ID, "block/" + textureName + state.getValue(((MarihuanaCropBlock) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
