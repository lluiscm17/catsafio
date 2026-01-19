package net.lluis.catsafio.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.recipe.HornoBigRecipe;
import net.lluis.catsafio.screen.HornoBigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class JEICatsafioModPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Catsafio.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new HornoBigCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<HornoBigRecipe> hornoBigRecipes = recipeManager.getAllRecipesFor(HornoBigRecipe.Type.INSTANCE);
        registration.addRecipes(HornoBigCategory.HORNO_BIG_TYPE, hornoBigRecipes);

    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(HornoBigScreen.class, 60, 30, 20, 30,
                HornoBigCategory.HORNO_BIG_TYPE);
    }
}
