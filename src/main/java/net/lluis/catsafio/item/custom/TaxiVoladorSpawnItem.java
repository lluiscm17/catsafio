package net.lluis.catsafio.item.custom;

import net.lluis.catsafio.entity.ModEntities;
import net.lluis.catsafio.entity.custom.TaxiVoladorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class TaxiVoladorSpawnItem extends Item {
    
    public TaxiVoladorSpawnItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        
        if (!level.isClientSide) {
            BlockPos pos = context.getClickedPos();
            Direction direction = context.getClickedFace();
            BlockPos spawnPos = pos.relative(direction);
            
            // Crear el taxi volador
            TaxiVoladorEntity taxi = ModEntities.TAXI_VOLADOR.get().create((ServerLevel) level);
            
            if (taxi != null) {
                // Posicionar el taxi
                taxi.moveTo(
                    spawnPos.getX() + 0.5,
                    spawnPos.getY(),
                    spawnPos.getZ() + 0.5,
                    context.getRotation(),
                    0
                );
                
                // Añadir al mundo
                level.addFreshEntity(taxi);
                
                // Consumir el item
                if (!context.getPlayer().isCreative()) {
                    context.getItemInHand().shrink(1);
                }
                
                return InteractionResult.SUCCESS;
            }
        }
        
        return InteractionResult.CONSUME;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, 
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§eSpawnea un Taxi Volador"));
        tooltip.add(Component.literal("§7Click derecho en el suelo"));
    }
}
