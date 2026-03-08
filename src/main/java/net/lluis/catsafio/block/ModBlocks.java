package net.lluis.catsafio.block;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.block.custom.HornoBigBlock;
import net.lluis.catsafio.block.custom.MarihuanaCropBlock;
import net.lluis.catsafio.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Catsafio.MOD_ID);

    public static final RegistryObject<Block> INFERNIT_BLOCK = registerBlock("infernit_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .sound(SoundType.NETHER_BRICKS).requiresCorrectToolForDrops()));

    // CRISTAL_REFORZADO con transparencia correcta
    public static final RegistryObject<Block> CRISTAL_REFORZADO = registerBlock("cristal_reforzado",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.GLASS)
                    .sound(SoundType.GLASS)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()) // Permite transparencia
    );

    public static final RegistryObject<Block> COOKED_IRON_BLOCK = registerBlock("cooked_iron_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> RAW_INFERNIT_BLOCK = registerBlock("raw_infernit_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .sound(SoundType.NETHER_BRICKS).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> AURALITA_BLOCK = registerBlock("auralita_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .sound(SoundType.AMETHYST).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> RAW_AURALITA_BLOCK = registerBlock("raw_auralita_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .sound(SoundType.AMETHYST).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> HORNO_BIG = registerBlock("horno_big",
            () -> new HornoBigBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));

    public static final RegistryObject<Block> AURALITA_ORE = registerBlock("auralita_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK)
                    .strength(4f).requiresCorrectToolForDrops(), UniformInt.of(3,7)));
    public static final RegistryObject<Block> RAYOLITA_ORE = registerBlock("rayolita_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .strength(5f).requiresCorrectToolForDrops(), UniformInt.of(3,8)));
    public static final RegistryObject<Block> DEEPSLATE_RAYOLITA_ORE = registerBlock("deepslate_rayolita_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .strength(5f).requiresCorrectToolForDrops(), UniformInt.of(3,8)));
    public static final RegistryObject<Block> INFERNIT_ORE = registerBlock("infernit_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.NETHERRACK)
                    .strength(3f).requiresCorrectToolForDrops(), UniformInt.of(3,6)));

    public static final RegistryObject<Block> MARIHUANA_CROP = registerBlock("marihuana_crop",
            () -> new MarihuanaCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noCollission()));

    // NEREIDA
    public static final RegistryObject<Block> MENA_NEREIDA = registerBlock("mena_nereida",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .strength(4f).requiresCorrectToolForDrops(), UniformInt.of(2, 5)));
    public static final RegistryObject<Block> BLOQUE_NEREIDA = registerBlock("bloque_nereida",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .requiresCorrectToolForDrops()));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {

        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}