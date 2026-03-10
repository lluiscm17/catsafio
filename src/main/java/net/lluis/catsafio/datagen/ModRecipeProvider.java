package net.lluis.catsafio.datagen;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.block.ModBlocks;
import net.lluis.catsafio.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    private static final List<ItemLike> IRON_SMELTABLES = List.of(Items.IRON_BLOCK);

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        // Wrapper que descarta el JSON de advancement para que las recetas
        // del mod NO aparezcan en el libro de recetas vanilla de Minecraft.
        Consumer<FinishedRecipe> w = sinAdvancement(pWriter);

        oreSmelting(w, IRON_SMELTABLES, RecipeCategory.MISC, ModItems.COOKED_IRON.get(), 0.25f, 1600, "iron_smeltables");
        oreBlasting(w, IRON_SMELTABLES, RecipeCategory.MISC, ModItems.COOKED_IRON.get(), 0.25f, 1250, "iron_smeltables");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.INFERNIT_BLOCK.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ModItems.INFERNIT.get())
                .unlockedBy(getHasName(ModItems.INFERNIT.get()), has(ModItems.INFERNIT.get()))
                .save(w);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.RAW_INFERNIT_BLOCK.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ModItems.RAW_INFERNIT.get())
                .unlockedBy(getHasName(ModItems.RAW_INFERNIT.get()), has(ModItems.RAW_INFERNIT.get()))
                .save(w);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.COOKED_IRON_BLOCK.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ModItems.COOKED_IRON.get())
                .unlockedBy(getHasName(ModItems.COOKED_IRON.get()), has(ModItems.COOKED_IRON.get()))
                .save(w);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.INFERNIT.get(), 9)
                .requires(ModBlocks.INFERNIT_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.INFERNIT_BLOCK.get()), has(ModBlocks.INFERNIT_BLOCK.get()))
                .save(w);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.RAW_INFERNIT.get(), 9)
                .requires(ModBlocks.RAW_INFERNIT_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.RAW_INFERNIT_BLOCK.get()), has(ModBlocks.RAW_INFERNIT_BLOCK.get()))
                .save(w);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.AURALITA_BLOCK.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ModItems.AURALITA.get())
                .unlockedBy(getHasName(ModItems.AURALITA.get()), has(ModItems.AURALITA.get()))
                .save(w);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.RAW_AURALITA_BLOCK.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ModItems.RAW_AURALITA.get())
                .unlockedBy(getHasName(ModItems.RAW_AURALITA.get()), has(ModItems.RAW_AURALITA.get()))
                .save(w);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.AURALITA.get(), 9)
                .requires(ModBlocks.AURALITA_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.AURALITA_BLOCK.get()), has(ModBlocks.AURALITA_BLOCK.get()))
                .save(w);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.RAW_AURALITA.get(), 9)
                .requires(ModBlocks.RAW_AURALITA_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.RAW_AURALITA_BLOCK.get()), has(ModBlocks.RAW_AURALITA_BLOCK.get()))
                .save(w);

        // NEREIDA
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.BLOQUE_NEREIDA.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ModItems.LINGOTE_NEREIDA.get())
                .unlockedBy(getHasName(ModItems.LINGOTE_NEREIDA.get()), has(ModItems.LINGOTE_NEREIDA.get()))
                .save(w);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.LINGOTE_NEREIDA.get(), 9)
                .requires(ModBlocks.BLOQUE_NEREIDA.get())
                .unlockedBy(getHasName(ModBlocks.BLOQUE_NEREIDA.get()), has(ModBlocks.BLOQUE_NEREIDA.get()))
                .save(w);
        oreSmelting(w, List.of(ModBlocks.MENA_NEREIDA.get()), RecipeCategory.MISC,
                ModItems.LINGOTE_NEREIDA.get(), 0.7f, 200, "nereida");
        oreBlasting(w, List.of(ModBlocks.MENA_NEREIDA.get()), RecipeCategory.MISC,
                ModItems.LINGOTE_NEREIDA.get(), 0.7f, 100, "nereida");

        // ESPADA INANIMADA
        // A A _
        // A A _
        // A B A   (A = cooked_iron, B = stick)
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ESPADA_INANIMADA.get())
                .pattern(" A ")
                .pattern(" A ")
                .pattern("ABA")
                .define('A', ModItems.COOKED_IRON.get())
                .define('B', Items.STICK)
                .unlockedBy(getHasName(ModItems.COOKED_IRON.get()), has(ModItems.COOKED_IRON.get()))
                .save(w);
    }

    /**
     * Envuelve el writer original para descartar los JSON de advancement.
     * Esto evita que las recetas aparezcan en el libro de recetas vanilla.
     * Las recetas siguen funcionando con normalidad en la mesa de crafteo / horno.
     */
    private static Consumer<FinishedRecipe> sinAdvancement(Consumer<FinishedRecipe> writer) {
        return recipe -> writer.accept(new FinishedRecipe() {
            @Override
            public void serializeRecipeData(com.google.gson.JsonObject json) {
                recipe.serializeRecipeData(json);
            }
            @Override
            public net.minecraft.resources.ResourceLocation getId() {
                return recipe.getId();
            }
            @Override
            public RecipeSerializer<?> getType() {
                return recipe.getType();
            }
            @Override
            public com.google.gson.JsonObject serializeAdvancement() {
                return null; // null = no se genera el fichero de advancement
            }
            @Override
            public net.minecraft.resources.ResourceLocation getAdvancementId() {
                return null;
            }
        });
    }

    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult,
                    pExperience, pCookingTime, pCookingSerializer)
                    .group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pFinishedRecipeConsumer, Catsafio.MOD_ID + ":" +getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }

    }
}
