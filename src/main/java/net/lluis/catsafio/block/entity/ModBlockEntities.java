package net.lluis.catsafio.block.entity;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Catsafio.MOD_ID);

    public static final RegistryObject<BlockEntityType<HornoBigBlockEntity>> HORNO_BIG_BE =
            BLOCK_ENTITIES.register("horno_big_be", () ->
                    BlockEntityType.Builder.of(HornoBigBlockEntity::new,
                            ModBlocks.HORNO_BIG.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
