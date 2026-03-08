package net.lluis.catsafio.quest;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.lluis.catsafio.Catsafio;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class QuestAdminCommand {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(buildCommand());
    }

    private static LiteralArgumentBuilder<CommandSourceStack> buildCommand() {
        return Commands.literal("questadmin")
            .requires(src -> src.hasPermission(2)) // Requiere nivel OP 2

            // /questadmin unlock <id>
            .then(Commands.literal("unlock")
                .then(Commands.argument("id", StringArgumentType.word())
                    .executes(ctx -> {
                        String id = StringArgumentType.getString(ctx, "id");
                        boolean ok = QuestManager.get().unlockQuest(id);
                        ctx.getSource().sendSuccess(() -> ok
                            ? Component.literal("§a✔ Misión '" + id + "' desbloqueada.")
                            : Component.literal("§cNo se encontró la misión '" + id + "' o ya estaba desbloqueada."), true);
                        return ok ? 1 : 0;
                    })))

            // /questadmin lock <id>
            .then(Commands.literal("lock")
                .then(Commands.argument("id", StringArgumentType.word())
                    .executes(ctx -> {
                        String id = StringArgumentType.getString(ctx, "id");
                        boolean ok = QuestManager.get().lockQuest(id);
                        ctx.getSource().sendSuccess(() -> ok
                            ? Component.literal("§e🔒 Misión '" + id + "' bloqueada.")
                            : Component.literal("§cNo se encontró la misión '" + id + "' o ya estaba bloqueada."), true);
                        return ok ? 1 : 0;
                    })))

            // /questadmin complete <id>
            .then(Commands.literal("complete")
                .then(Commands.argument("id", StringArgumentType.word())
                    .executes(ctx -> {
                        String id = StringArgumentType.getString(ctx, "id");
                        boolean ok = QuestManager.get().completeQuest(id);
                        ctx.getSource().sendSuccess(() -> ok
                            ? Component.literal("§b★ Misión '" + id + "' marcada como completada.")
                            : Component.literal("§cNo se encontró la misión '" + id + "'."), true);
                        return ok ? 1 : 0;
                    })))

            // /questadmin reset <id>
            .then(Commands.literal("reset")
                .then(Commands.argument("id", StringArgumentType.word())
                    .executes(ctx -> {
                        String id = StringArgumentType.getString(ctx, "id");
                        boolean ok = QuestManager.get().resetQuest(id);
                        ctx.getSource().sendSuccess(() -> ok
                            ? Component.literal("§6↺ Misión '" + id + "' reiniciada.")
                            : Component.literal("§cNo se encontró la misión '" + id + "'."), true);
                        return ok ? 1 : 0;
                    })))

            // /questadmin list
            .then(Commands.literal("list")
                .executes(ctx -> {
                    ctx.getSource().sendSuccess(() -> Component.literal("§e=== Misiones ==="), false);
                    for (Quest q : QuestManager.get().getAllQuests()) {
                        String color = switch (q.getState()) {
                            case CLAIMED     -> "§6";
                            case COMPLETED   -> "§a";
                            case IN_PROGRESS -> "§e";
                            case LOCKED      -> "§7";
                        };
                        String symbol = switch (q.getState()) {
                            case CLAIMED     -> "★";
                            case COMPLETED   -> "✔";
                            case IN_PROGRESS -> "◉";
                            case LOCKED      -> "🔒";
                        };
                        ctx.getSource().sendSuccess(() -> Component.literal(
                            color + symbol + " [" + q.getId() + "] " + q.getTitle()
                            + " §f(" + q.getProgress() + "/" + q.getTargetAmount() + ")"
                        ), false);
                    }
                    return 1;
                }));
    }
}
