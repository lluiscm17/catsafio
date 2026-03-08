package net.lluis.catsafio.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.lluis.catsafio.network.RoulettePacket;
import net.lluis.catsafio.roulette.RouletteConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouletteCommand {

    private static final List<String> COLORS = Arrays.asList(
            "azul", "amarilla", "verde", "naranja", "turquesa", "morada", "rosada"
    );

    // Guardar qué ruleta está activa y cuándo termina
    private static final Map<String, Long> activeRoulettes = new HashMap<>();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("ruleta")
                .requires(src -> src.hasPermission(2))
                .then(Commands.argument("color", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            COLORS.forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .executes(ctx -> {
                            String color = StringArgumentType.getString(ctx, "color").toLowerCase();

                            if (!COLORS.contains(color)) {
                                ctx.getSource().sendFailure(Component.literal(
                                        "§cColor inválido. Colores disponibles: azul, amarilla, verde, naranja, turquesa, morada, rosada"));
                                return 0;
                            }

                            // Enviar packet a todos los jugadores para iniciar animación
                            RoulettePacket packet = new RoulettePacket(color);
                            for (ServerPlayer player : ctx.getSource().getServer().getPlayerList().getPlayers()) {
                                RoulettePacket.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
                            }

                            ctx.getSource().sendSuccess(() ->
                                    Component.literal("§aRuleta " + color + " iniciada para todos los jugadores"), true);

                            // Programar mensaje y comandos al finalizar (SOLO UNA VEZ desde el servidor)
                            scheduleCompletion(ctx.getSource(), color);

                            return 1;
                        })
                )
        );
    }

    private static void scheduleCompletion(CommandSourceStack source, String color) {
        // Obtener duración del video según el color
        float videoDuration;
        if (color.equals("rosada")) {
            videoDuration = 22.5f; // Video rosada dura 22.5s
        } else {
            videoDuration = 15.229729f; // Otras ruletas duran ~15.23s
        }

        long completionTime = System.currentTimeMillis() + (long)(videoDuration * 1000);
        activeRoulettes.put(color, completionTime);

        // Programar ejecución en el servidor
        new Thread(() -> {
            try {
                Thread.sleep((long)(videoDuration * 1000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            // Ejecutar en el thread del servidor
            source.getServer().execute(() -> {
                // Verificar que esta ruleta todavía debe completarse
                Long scheduledTime = activeRoulettes.get(color);
                if (scheduledTime != null && Math.abs(System.currentTimeMillis() - scheduledTime) < 1000) {
                    RouletteConfig.RouletteData data = RouletteConfig.get(color);

                    // Enviar mensaje al chat
                    source.getServer().getPlayerList().broadcastSystemMessage(
                            Component.literal(data.message), false);

                    // Ejecutar comandos configurados
                    for (String cmd : data.commands) {
                        source.getServer().getCommands().performPrefixedCommand(
                                source.getServer().createCommandSourceStack(), cmd);
                    }

                    // Limpiar
                    activeRoulettes.remove(color);
                }
            });
        }).start();
    }
}