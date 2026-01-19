package net.lluis.catsafio.datagen;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, Catsafio.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ItemTags.TRIMMABLE_ARMOR)
                .add(ModItems.INFERNIT_CHESTPLATE.get(),
                        ModItems.INFERNIT_LEGGINS.get(),
                        ModItems.INFERNIT_BOOTS.get(),
                        ModItems.AURALITA_BOOTS.get(),
                        ModItems.AURALITA_LEGGINS.get(),
                        ModItems.AURALITA_HELMET.get(),
                        ModItems.AURALITA_CHESTPLATE.get(),
                        ModItems.RAYOLITA_BOOTS.get(),
                        ModItems.RAYOLITA_LEGGINS.get(),
                        ModItems.RAYOLITA_CHESTPLATE.get(),
                        ModItems.RAYOLITA_HELMET.get());
    }
}
