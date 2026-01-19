package net.lluis.catsafio.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.block.ModBlocks;
import net.lluis.catsafio.recipe.HornoBigRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class HornoBigCategory implements IRecipeCategory<HornoBigRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Catsafio.MOD_ID, "horno_big");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Catsafio.MOD_ID,
            "textures/gui/horno_big_gui.png");

    public static RecipeType<HornoBigRecipe> HORNO_BIG_TYPE =
            new RecipeType<>(UID, HornoBigRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public HornoBigCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.HORNO_BIG.get()));
    }

    @Override
    public RecipeType<HornoBigRecipe> getRecipeType() {
        return HORNO_BIG_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.catsafio.horno_big");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, HornoBigRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 11).addIngredients(recipe.getIngredients().get(0));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 59).addItemStack(recipe.getResultItem(null));
    }
}
