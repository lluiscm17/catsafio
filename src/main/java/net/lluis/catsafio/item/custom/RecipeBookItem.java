package net.lluis.catsafio.item.custom;

import net.lluis.catsafio.client.gui.RecipeBookScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RecipeBookItem extends Item {
    
    public RecipeBookItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pLevel.isClientSide()) {
            // Abrir la GUI del libro de recetas
            Minecraft.getInstance().setScreen(new RecipeBookScreen());
        }
        
        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }
}
