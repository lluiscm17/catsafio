package net.lluis.catsafio.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.lluis.catsafio.Catsafio;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DeadlyLavaCommand {

    private static boolean enabled = false;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("deadlylava")
            .requires(source -> source.hasPermission(2))
            .then(Commands.argument("enabled", BoolArgumentType.bool())
                .executes(DeadlyLavaCommand::execute))
            .executes(ctx -> {
                ctx.getSource().sendSuccess(() -> 
                    Component.literal("§eLava Mortal: " + (enabled ? "§aEnabled" : "§cDisabled")), false);
                return Command.SINGLE_SUCCESS;
            })
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        enabled = BoolArgumentType.getBool(ctx, "enabled");
        ctx.getSource().sendSuccess(() -> 
            Component.literal(enabled ? 
                "§a✔ §6§lLAVA MORTAL §aactivada! Daño de lava x3!" :
                "§c✘ Lava Mortal desactivada."), true);
        return Command.SINGLE_SUCCESS;
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!enabled) return;
        
        if (event.getSource().getMsgId().equals("lava")) {
            event.setAmount(event.getAmount() * 3.0f);
        }
    }

    public static boolean isEnabled() { return enabled; }
}
