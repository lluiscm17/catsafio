package net.lluis.catsafio.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.lluis.catsafio.Catsafio;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;

public class RecipeBookScreen extends Screen {
    private static final ResourceLocation BACKGROUND = 
            new ResourceLocation("minecraft", "textures/gui/demo_background.png");
    
    private final List<Recipe<?>> recipes = new ArrayList<>();
    private int scrollOffset = 0;
    private static final int RECIPES_PER_PAGE = 7;

    public RecipeBookScreen() {
        super(Component.literal("Libro de Recetas"));
        loadRecipes();
    }

    private void loadRecipes() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            // Obtener todas las recetas de crafting
            recipes.addAll(mc.level.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING));
        }
    }

    @Override
    protected void init() {
        super.init();
        
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Botón para cerrar
        this.addRenderableWidget(Button.builder(
                Component.literal("Cerrar"),
                button -> this.onClose()
        ).bounds(centerX - 50, centerY + 80, 100, 20).build());

        // Botón para scroll arriba
        this.addRenderableWidget(Button.builder(
                Component.literal("↑"),
                button -> {
                    if (scrollOffset > 0) {
                        scrollOffset--;
                    }
                }
        ).bounds(centerX + 90, centerY - 70, 20, 20).build());

        // Botón para scroll abajo
        this.addRenderableWidget(Button.builder(
                Component.literal("↓"),
                button -> {
                    if (scrollOffset < Math.max(0, recipes.size() - RECIPES_PER_PAGE)) {
                        scrollOffset++;
                    }
                }
        ).bounds(centerX + 90, centerY + 50, 20, 20).build());
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Dibujar fondo
        pGuiGraphics.fill(centerX - 120, centerY - 90, centerX + 120, centerY + 90, 0xC0101010);
        pGuiGraphics.fill(centerX - 118, centerY - 88, centerX + 118, centerY + 88, 0xFF383838);

        // Título
        pGuiGraphics.drawCenteredString(this.font, this.title, centerX, centerY - 80, 0xFFFFFF);

        // Dibujar recetas
        int startY = centerY - 60;
        int displayCount = Math.min(RECIPES_PER_PAGE, recipes.size() - scrollOffset);
        
        for (int i = 0; i < displayCount; i++) {
            int index = scrollOffset + i;
            if (index < recipes.size()) {
                Recipe<?> recipe = recipes.get(index);
                int y = startY + (i * 18);
                
                // Dibujar el resultado de la receta
                ItemStack result = recipe.getResultItem(Minecraft.getInstance().level.registryAccess());
                pGuiGraphics.renderItem(result, centerX - 100, y);
                
                // Nombre del item
                String itemName = result.getHoverName().getString();
                if (itemName.length() > 25) {
                    itemName = itemName.substring(0, 22) + "...";
                }
                pGuiGraphics.drawString(this.font, itemName, centerX - 75, y + 4, 0xFFFFFF);
            }
        }

        // Info de scroll
        if (recipes.size() > RECIPES_PER_PAGE) {
            String scrollInfo = (scrollOffset + 1) + "-" + 
                    Math.min(scrollOffset + RECIPES_PER_PAGE, recipes.size()) + 
                    " / " + recipes.size();
            pGuiGraphics.drawCenteredString(this.font, scrollInfo, centerX + 100, centerY + 60, 0xAAAAAA);
        }

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
