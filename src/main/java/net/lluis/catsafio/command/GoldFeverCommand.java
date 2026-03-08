package net.lluis.catsafio.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.lluis.catsafio.Catsafio;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GoldFeverCommand {

    private static boolean enabled = false;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("goldfever")
            .requires(source -> source.hasPermission(2))
            .then(Commands.argument("enabled", BoolArgumentType.bool())
                .executes(GoldFeverCommand::execute))
            .executes(ctx -> {
                ctx.getSource().sendSuccess(() -> 
                    Component.literal("§eFiebre del Oro: " + (enabled ? "§aEnabled" : "§cDisabled")), false);
                return Command.SINGLE_SUCCESS;
            })
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        enabled = BoolArgumentType.getBool(ctx, "enabled");
        ctx.getSource().sendSuccess(() -> 
            Component.literal(enabled ? 
                "§a✔ §6§lFIEBRE DEL ORO §aactivada! Piglins dropean pepitas de oro!" :
                "§c✘ Fiebre del Oro desactivada."), true);
        return Command.SINGLE_SUCCESS;
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!enabled) return;
        
        if (event.getEntity() instanceof Piglin piglin) {
            // Dropear 1-3 pepitas de oro
            int amount = 1 + piglin.level().random.nextInt(3);
            ItemEntity item = new ItemEntity(
                piglin.level(), 
                piglin.getX(), piglin.getY(), piglin.getZ(),
                new ItemStack(Items.GOLD_NUGGET, amount)
            );
            piglin.level().addFreshEntity(item);
        }
    }

    public static boolean isEnabled() { return enabled; }
}
