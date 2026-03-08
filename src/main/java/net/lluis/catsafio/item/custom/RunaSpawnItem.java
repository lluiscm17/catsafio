package net.lluis.catsafio.item.custom;

import net.lluis.catsafio.entity.ModEntities;
import net.lluis.catsafio.entity.custom.RunaEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class RunaSpawnItem extends Item {

    public RunaSpawnItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            BlockPos pos = context.getClickedPos();
            Direction face = context.getClickedFace();
            BlockPos spawnPos = pos.relative(face);
            
            // Crear y spawnear la Runa
            RunaEntity runa = new RunaEntity(ModEntities.RUNA.get(), serverLevel);
            runa.moveTo(
                spawnPos.getX() + 0.5,
                spawnPos.getY(),
                spawnPos.getZ() + 0.5,
                0, 0
            );
            
            serverLevel.addFreshEntity(runa);
            
            // Consumir el item si no está en creativo
            if (!context.getPlayer().isCreative()) {
                context.getItemInHand().shrink(1);
            }
            
            return InteractionResult.SUCCESS;
        }
        
        return InteractionResult.CONSUME;
    }
}
