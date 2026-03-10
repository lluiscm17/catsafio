package net.lluis.catsafio.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.effect.ModEffects;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CremaSolarCommand {

    private static boolean enabled = false;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("cremasolar")
            .requires(source -> source.hasPermission(2))
            .then(Commands.argument("enabled", BoolArgumentType.bool())
                .executes(CremaSolarCommand::execute))
            .executes(ctx -> {
                ctx.getSource().sendSuccess(() ->
                    Component.literal("§eSol Abrasador: " +
                        (enabled ? "§aActivado" : "§cDesactivado")), false);
                return Command.SINGLE_SUCCESS;
            })
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        boolean enable = BoolArgumentType.getBool(ctx, "enabled");
        enabled = enable;

        if (enable) {
            ctx.getSource().sendSuccess(() ->
                Component.literal("§c☀ Sol Abrasador §aactivado! Los jugadores sin crema solar se quemarán."),
                true);
        } else {
            ctx.getSource().sendSuccess(() ->
                Component.literal("§7☀ Sol Abrasador §cdesactivado."),
                true);
        }

        return Command.SINGLE_SUCCESS;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!enabled || event.phase != TickEvent.Phase.END) return;
        if (event.player.level().isClientSide()) return;

        Player player = event.player;
        Level level = player.level();

        // Ignorar jugadores en creativo o espectador
        if (player.isCreative() || player.isSpectator()) return;

        // Solo durante el día
        if (!level.isDay()) return;

        // Verificar que el jugador esté expuesto al sol (sin techo)
        BlockPos pos = player.blockPosition();
        if (level.getBrightness(LightLayer.SKY, pos) < 15) return;

        // Si tiene protección solar, está protegido
        if (player.hasEffect(ModEffects.PROTECCION_SOLAR.get())) return;

        // ¡Quemar!
        player.setSecondsOnFire(3);
    }

    public static boolean isEnabled() {
        return enabled;
    }
}
