package net.lluis.catsafio.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.lluis.catsafio.Catsafio;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EnderPearlLimitCommand {

    private static boolean enabled = false;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("enderpearllimit")
            .requires(source -> source.hasPermission(2))
            .then(Commands.argument("enabled", BoolArgumentType.bool())
                .executes(EnderPearlLimitCommand::execute))
            .executes(ctx -> {
                ctx.getSource().sendSuccess(() -> 
                    Component.literal("§eENDERPEEEEEARLS: " + (enabled ? "§aEnabled" : "§cDisabled")), false);
                return Command.SINGLE_SUCCESS;
            })
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        enabled = BoolArgumentType.getBool(ctx, "enabled");
        ctx.getSource().sendSuccess(() -> 
            Component.literal(enabled ? 
                "§a✔ §5§lENDERPEEEEEARLS §aactivado! Solo teletransportan en la misma dimensión!" :
                "§c✘ EnderPearl Limit desactivado."), true);
        return Command.SINGLE_SUCCESS;
    }

    @SubscribeEvent
    public static void onPearlImpact(ProjectileImpactEvent event) {
        if (!enabled) return;
        
        if (event.getProjectile() instanceof ThrownEnderpearl pearl) {
            var owner = pearl.getOwner();
            if (owner != null && !pearl.level().dimension().equals(owner.level().dimension())) {
                // Cancelar teletransporte si no está en la misma dimensión
                event.setCanceled(true);
                pearl.discard();
                if (owner instanceof net.minecraft.world.entity.player.Player player) {
                    player.displayClientMessage(
                        Component.literal("§c¡La perla no funciona entre dimensiones!"), true);
                }
            }
        }
    }

    public static boolean isEnabled() { return enabled; }
}
