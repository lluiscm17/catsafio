package net.lluis.catsafio.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.lluis.catsafio.Catsafio;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DeadlyCactusCommand {

    private static boolean deadlyCactusEnabled = false;
    private static final float DEADLY_DAMAGE = 7777000000f; // 7777 millones

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("deadlycactus")
            .requires(source -> source.hasPermission(2))
            .then(Commands.argument("enabled", BoolArgumentType.bool())
                .executes(DeadlyCactusCommand::execute))
            .executes(ctx -> {
                ctx.getSource().sendSuccess(() -> 
                    Component.literal("§eDeadly Cactus: " + 
                        (deadlyCactusEnabled ? "§aEnabled (7777M damage)" : "§cDisabled")), false);
                return Command.SINGLE_SUCCESS;
            })
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        boolean enable = BoolArgumentType.getBool(ctx, "enabled");
        deadlyCactusEnabled = enable;

        if (enable) {
            ctx.getSource().sendSuccess(() -> 
                Component.literal("§a✔ Deadly Cactus enabled! All cacti now deal §c7777 MILLION §adamage!"), 
                true);
        } else {
            ctx.getSource().sendSuccess(() -> 
                Component.literal("§c✘ Deadly Cactus disabled. Cacti deal normal damage."), 
                true);
        }

        return Command.SINGLE_SUCCESS;
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!deadlyCactusEnabled) return;

        DamageSource source = event.getSource();
        
        // Verificar si el daño es causado por un cactus
        // En Minecraft, el daño de cactus usa el tipo "cactus"
        if (source.getMsgId().equals("cactus")) {
            // Multiplicar el daño a 7777 millones
            event.setAmount(DEADLY_DAMAGE);
        }
    }

    public static boolean isEnabled() {
        return deadlyCactusEnabled;
    }
}
