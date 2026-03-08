package net.lluis.catsafio.item.custom;

import net.lluis.catsafio.entity.custom.RunaEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class RunaRemoverItem extends Item {

    public RunaRemoverItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        
        if (!level.isClientSide()) {
            // Buscar Runas en un radio de 10 bloques
            AABB searchBox = player.getBoundingBox().inflate(10);
            List<RunaEntity> runas = level.getEntitiesOfClass(RunaEntity.class, searchBox);
            
            if (!runas.isEmpty()) {
                // Eliminar la Runa más cercana
                RunaEntity closest = null;
                double minDist = Double.MAX_VALUE;
                
                for (RunaEntity runa : runas) {
                    double dist = player.distanceToSqr(runa);
                    if (dist < minDist) {
                        minDist = dist;
                        closest = runa;
                    }
                }
                
                if (closest != null) {
                    closest.discard();
                    player.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0f, 1.0f);
                    stack.shrink(1);
                    return InteractionResultHolder.success(stack);
                }
            }
        }
        
        return InteractionResultHolder.fail(stack);
    }
}
