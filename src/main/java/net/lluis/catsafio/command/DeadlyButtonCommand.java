package net.lluis.catsafio.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.lluis.catsafio.Catsafio;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DeadlyButtonCommand {

    private static boolean deadlyButtonsEnabled = false;
    private static final float DEADLY_DAMAGE = 23000000f; // 23 millones de daño

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("deadlybuttons")
            .requires(source -> source.hasPermission(2))
            .then(Commands.argument("enabled", BoolArgumentType.bool())
                .executes(DeadlyButtonCommand::execute))
            .executes(ctx -> {
                ctx.getSource().sendSuccess(() -> 
                    Component.literal("§eDeadly Buttons: " + 
                        (deadlyButtonsEnabled ? "§aEnabled (23M damage)" : "§cDisabled")), false);
                return Command.SINGLE_SUCCESS;
            })
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        boolean enable = BoolArgumentType.getBool(ctx, "enabled");
        deadlyButtonsEnabled = enable;

        if (enable) {
            ctx.getSource().sendSuccess(() -> 
                Component.literal("§a✔ Deadly Buttons enabled! All buttons now deal §c23 MILLION §adamage!"), 
                true);
        } else {
            ctx.getSource().sendSuccess(() -> 
                Component.literal("§c✘ Deadly Buttons disabled. Buttons work normally."), 
                true);
        }

        return Command.SINGLE_SUCCESS;
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!deadlyButtonsEnabled) return;
        
        var blockState = event.getLevel().getBlockState(event.getPos());
        if (blockState.getBlock() instanceof ButtonBlock) {
            Player player = event.getEntity();
            
            // Aplicar 23 millones de daño (muerte instantánea)
            player.hurt(player.damageSources().generic(), DEADLY_DAMAGE);
            
            // El jugador morirá antes de ver este mensaje, pero por si acaso...
            player.displayClientMessage(
                Component.literal("§c§l💀 DEADLY BUTTON! §7(-23M HP)"), 
                true
            );
        }
    }

    public static boolean isEnabled() {
        return deadlyButtonsEnabled;
    }
}
