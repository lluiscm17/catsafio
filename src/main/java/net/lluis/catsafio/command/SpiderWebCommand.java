package net.lluis.catsafio.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.lluis.catsafio.Catsafio;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpiderWebCommand {

    private static boolean spiderWebEnabled = false;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("spiderweb")
            .requires(source -> source.hasPermission(2))
            .then(Commands.argument("enabled", BoolArgumentType.bool())
                .executes(SpiderWebCommand::execute))
            .executes(ctx -> {
                ctx.getSource().sendSuccess(() -> 
                    Component.literal("§eSpider Web (Fiebre Tejedora): " + 
                        (spiderWebEnabled ? "§aEnabled" : "§cDisabled")), false);
                return Command.SINGLE_SUCCESS;
            })
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        boolean enable = BoolArgumentType.getBool(ctx, "enabled");
        spiderWebEnabled = enable;

        if (enable) {
            ctx.getSource().sendSuccess(() -> 
                Component.literal("§a✔ Fiebre Tejedora activada! Las arañas colocarán telarañas en tus pies."), 
                true);
        } else {
            ctx.getSource().sendSuccess(() -> 
                Component.literal("§c✘ Fiebre Tejedora desactivada. Las arañas atacan normalmente."), 
                true);
        }

        return Command.SINGLE_SUCCESS;
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!spiderWebEnabled) return;

        // Verificar si el atacante es una araña y la víctima es un jugador
        if (event.getSource().getEntity() instanceof Spider && event.getEntity() instanceof Player player) {
            // Obtener la posición a los pies del jugador
            BlockPos feetPos = player.blockPosition();
            
            // Verificar si el bloque está vacío o es reemplazable
            if (player.level().getBlockState(feetPos).isAir() || 
                player.level().getBlockState(feetPos).canBeReplaced()) {
                
                // Colocar telaraña en los pies del jugador
                player.level().setBlock(feetPos, Blocks.COBWEB.defaultBlockState(), 3);
                
                // Mensaje al jugador
                player.displayClientMessage(
                    Component.literal("§7¡Una araña te ha atrapado con su telaraña!"), 
                    true
                );
            }
        }
    }

    public static boolean isEnabled() {
        return spiderWebEnabled;
    }
}
