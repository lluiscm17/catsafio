package net.lluis.catsafio.item.custom;

import net.lluis.catsafio.effect.ModEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class CremaSolarItem extends Item {

    // 30 minutos = 30 * 60 * 20 ticks
    private static final int DURATION_TICKS = 30 * 60 * 20;

    public CremaSolarItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player,
                                                   LivingEntity target, InteractionHand hand) {
        if (!player.level().isClientSide && target instanceof Player targetPlayer) {

            // No se puede aplicar si el objetivo ya tiene el efecto activo
            if (targetPlayer.hasEffect(ModEffects.PROTECCION_SOLAR.get())) {
                player.sendSystemMessage(Component.literal(
                        "§c" + targetPlayer.getDisplayName().getString()
                                + " ya tiene protección solar activa!"));
                return InteractionResult.FAIL;
            }

            // Aplicar protección solar al jugador objetivo
            targetPlayer.addEffect(new MobEffectInstance(
                    ModEffects.PROTECCION_SOLAR.get(),
                    DURATION_TICKS,
                    0,      // amplifier (nivel 0)
                    false,  // no es ambiental
                    true,   // mostrar partículas
                    true    // mostrar icono en HUD
            ));

            // Consumir el item (no en creativo)
            if (!player.isCreative()) {
                stack.shrink(1);
            }

            // Feedback
            player.sendSystemMessage(Component.literal(
                    "§a✔ Has aplicado crema solar a §e"
                            + targetPlayer.getDisplayName().getString()
                            + "§a. ¡Protegido 30 minutos!"));
            targetPlayer.sendSystemMessage(Component.literal(
                    "§a✔ §e" + player.getDisplayName().getString()
                            + " §ate ha aplicado crema solar. ¡Protegido del sol 30 minutos!"));

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                 List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§7Click derecho sobre un jugador para aplicar"));
        tooltip.add(Component.literal("§7Protege del sol durante §e30 minutos"));
        tooltip.add(Component.literal("§c✗ No acumulable: no se puede reaplicar hasta que expire"));
    }
}
