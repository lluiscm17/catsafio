package net.lluis.catsafio.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.lluis.catsafio.Catsafio;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WardenGolemCommand {

    private static boolean enabled = false;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("wardengolem")
            .requires(source -> source.hasPermission(2))
            .then(Commands.argument("enabled", BoolArgumentType.bool())
                .executes(WardenGolemCommand::execute))
            .executes(ctx -> {
                ctx.getSource().sendSuccess(() -> 
                    Component.literal("§eATTE: REVIIL: " + (enabled ? "§aEnabled" : "§cDisabled")), false);
                return Command.SINGLE_SUCCESS;
            })
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        enabled = BoolArgumentType.getBool(ctx, "enabled");
        ctx.getSource().sendSuccess(() -> 
            Component.literal(enabled ? 
                "§a✔ §8§lATTE: REVIIL §aactivado! Los Golems de Hierro son ahora Wardens!" :
                "§c✘ REVIIL desactivado. Golems normales."), true);
        return Command.SINGLE_SUCCESS;
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (!enabled || event.getLevel().isClientSide()) return;
        
        // Reemplazar Iron Golem por Warden
        if (event.getEntity() instanceof IronGolem golem) {
            Warden warden = EntityType.WARDEN.create(golem.level());
            if (warden != null) {
                warden.moveTo(golem.getX(), golem.getY(), golem.getZ(), golem.getYRot(), golem.getXRot());
                golem.level().addFreshEntity(warden);
                golem.discard();
            }
        }
    }

    public static boolean isEnabled() { return enabled; }
}
