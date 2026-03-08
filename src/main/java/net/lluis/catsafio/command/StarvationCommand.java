package net.lluis.catsafio.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.lluis.catsafio.Catsafio;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class StarvationCommand {

    private static boolean enabled = false;
    private static int tickCounter = 0;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("starvation")
            .requires(source -> source.hasPermission(2))
            .then(Commands.argument("enabled", BoolArgumentType.bool())
                .executes(StarvationCommand::execute))
            .executes(ctx -> {
                ctx.getSource().sendSuccess(() -> 
                    Component.literal("§eHambruna: " + (enabled ? "§aEnabled" : "§cDisabled")), false);
                return Command.SINGLE_SUCCESS;
            })
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        enabled = BoolArgumentType.getBool(ctx, "enabled");
        ctx.getSource().sendSuccess(() -> 
            Component.literal(enabled ? 
                "§a✔ §6§lHAMBRUNA §aactivada! Hambre baja el doble!" :
                "§c✘ Hambruna desactivada."), true);
        return Command.SINGLE_SUCCESS;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!enabled || event.phase != TickEvent.Phase.END) return;
        
        Player player = event.player;
        tickCounter++;
        
        // Cada segundo (20 ticks), reducir hambre adicional
        if (tickCounter >= 20) {
            tickCounter = 0;
            if (player.getFoodData().getFoodLevel() > 0) {
                player.getFoodData().addExhaustion(4.0f); // Aumenta exhaustion para bajar hambre más rápido
            }
        }
    }

    public static boolean isEnabled() { return enabled; }
}
