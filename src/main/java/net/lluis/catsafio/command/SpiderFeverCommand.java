package net.lluis.catsafio.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.lluis.catsafio.Catsafio;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpiderFeverCommand {

    private static boolean enabled = false;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("spiderfever")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("enabled", BoolArgumentType.bool())
                        .executes(SpiderFeverCommand::execute))
                .executes(ctx -> {
                    ctx.getSource().sendSuccess(() ->
                            Component.literal("§eFiebre Arácnida: " + (enabled ? "§aEnabled" : "§cDisabled")), false);
                    return Command.SINGLE_SUCCESS;
                })
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        enabled = BoolArgumentType.getBool(ctx, "enabled");
        ctx.getSource().sendSuccess(() ->
                Component.literal(enabled ?
                        "§a✔ §7§lFIEBRE ARÁCNIDA §aactivada! Romper telarañas spawnea arañas venenosas!" :
                        "§c✘ Fiebre Arácnida desactivada."), true);
        return Command.SINGLE_SUCCESS;
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!enabled) return;

        // Verificar si rompió una telaraña
        if (event.getState().is(Blocks.COBWEB)) {
            // Spawnear 1-3 arañas de cueva venenosas
            Player player = event.getPlayer();
            int amount = 1 + player.level().random.nextInt(3);

            for (int i = 0; i < amount; i++) {
                CaveSpider spider = EntityType.CAVE_SPIDER.create(player.level());
                if (spider != null) {
                    spider.moveTo(
                            event.getPos().getX() + 0.5,
                            event.getPos().getY(),
                            event.getPos().getZ() + 0.5,
                            player.level().random.nextFloat() * 360F,
                            0
                    );
                    // Darle veneno adicional permanente
                    spider.addEffect(new MobEffectInstance(MobEffects.POISON, Integer.MAX_VALUE, 1, false, false));
                    player.level().addFreshEntity(spider);
                }
            }

            player.displayClientMessage(
                    Component.literal("§7¡Arañas venenosas emergen de la telaraña!"),
                    true
            );
        }
    }

    public static boolean isEnabled() { return enabled; }
}
