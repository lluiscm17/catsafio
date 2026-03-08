package net.lluis.catsafio.item.custom;

import net.lluis.catsafio.entity.custom.TaxiVoladorEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class TaxiDestructorItem extends Item {
    
    public TaxiDestructorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, 
                                                   net.minecraft.world.entity.LivingEntity target, 
                                                   InteractionHand hand) {
        // Este método no se usa para entities que no son LivingEntity
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, 
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§cDestruye el Taxi Volador"));
        tooltip.add(Component.literal("§7Click derecho en el taxi"));
    }
}
