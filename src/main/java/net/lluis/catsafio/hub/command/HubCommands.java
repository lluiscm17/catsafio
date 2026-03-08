package net.lluis.catsafio.hub.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.hub.MobInfo;
import net.lluis.catsafio.hub.data.HubData;
import net.lluis.catsafio.hub.mission.Mission;
import net.lluis.catsafio.hub.mission.MissionRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HubCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(buildCatpediaCommand());
        event.getDispatcher().register(buildArbolTechCommand());
        event.getDispatcher().register(buildDificultadCommand());
        event.getDispatcher().register(buildNotificacionCommand());
        event.getDispatcher().register(buildMissionCommand());
    }

    // ── CATPEDIA ─────────────────────────────────────────────────────────
    private static LiteralArgumentBuilder<CommandSourceStack> buildCatpediaCommand() {
        return Commands.literal("catpedia").requires(src -> src.hasPermission(2))
                .then(Commands.literal("unlock")
                        .then(Commands.argument("mobId", StringArgumentType.word())
                                .executes(ctx -> {
                                    String mobId = StringArgumentType.getString(ctx, "mobId");
                                    HubData.get().unlockMob(mobId);
                                    ctx.getSource().sendSuccess(() -> Component.literal("§a✔ Mob '" + mobId + "' desbloqueado"), true);
                                    return 1;
                                })))
                .then(Commands.literal("lock")
                        .then(Commands.argument("mobId", StringArgumentType.word())
                                .executes(ctx -> {
                                    String mobId = StringArgumentType.getString(ctx, "mobId");
                                    HubData.get().lockMob(mobId);
                                    ctx.getSource().sendSuccess(() -> Component.literal("§c🔒 Mob '" + mobId + "' bloqueado"), true);
                                    return 1;
                                })))
                .then(Commands.literal("list")
                        .executes(ctx -> {
                            ctx.getSource().sendSuccess(() -> Component.literal("§e=== Mobs ==="), false);
                            for (MobInfo mob : MobInfo.ALL_MOBS) {
                                boolean unlocked = HubData.get().isMobUnlocked(mob.getId());
                                ctx.getSource().sendSuccess(() -> Component.literal(
                                        (unlocked ? "§a✔" : "§c🔒") + " " + mob.getId()), false);
                            }
                            return 1;
                        }));
    }

    // ── ÁRBOL TECH ───────────────────────────────────────────────────────
    private static LiteralArgumentBuilder<CommandSourceStack> buildArbolTechCommand() {
        return Commands.literal("arboltech").requires(src -> src.hasPermission(2))
                .then(Commands.literal("unlock")
                        .then(Commands.argument("recipeId", StringArgumentType.greedyString())
                                .executes(ctx -> {
                                    String recipeId = StringArgumentType.getString(ctx, "recipeId");
                                    HubData.get().unlockRecipe(recipeId);
                                    ctx.getSource().sendSuccess(() -> Component.literal("§a✔ Receta desbloqueada"), true);
                                    return 1;
                                })))
                .then(Commands.literal("lock")
                        .then(Commands.argument("recipeId", StringArgumentType.greedyString())
                                .executes(ctx -> {
                                    String recipeId = StringArgumentType.getString(ctx, "recipeId");
                                    HubData.get().lockRecipe(recipeId);
                                    ctx.getSource().sendSuccess(() -> Component.literal("§c🔒 Receta bloqueada"), true);
                                    return 1;
                                })));
    }

    // ── DIFICULTAD ───────────────────────────────────────────────────────
    private static LiteralArgumentBuilder<CommandSourceStack> buildDificultadCommand() {
        return Commands.literal("dificultad").requires(src -> src.hasPermission(2))
                .then(Commands.literal("set")
                        .then(Commands.argument("texto", StringArgumentType.greedyString())
                                .executes(ctx -> {
                                    String texto = StringArgumentType.getString(ctx, "texto");
                                    HubData.get().setDificultadText(texto);
                                    ctx.getSource().sendSuccess(() -> Component.literal("§a✔ Texto actualizado"), true);
                                    return 1;
                                })));
    }

    // ── NOTIFICACIONES ───────────────────────────────────────────────────
    private static LiteralArgumentBuilder<CommandSourceStack> buildNotificacionCommand() {
        return Commands.literal("notificacion").requires(src -> src.hasPermission(2))
                .then(Commands.literal("add")
                        .then(Commands.argument("texto", StringArgumentType.greedyString())
                                .executes(ctx -> {
                                    String texto = StringArgumentType.getString(ctx, "texto");
                                    HubData.get().addNotificacion(texto);
                                    ctx.getSource().sendSuccess(() -> Component.literal("§a✔ Notificación añadida"), true);
                                    return 1;
                                })))
                .then(Commands.literal("clear")
                        .executes(ctx -> {
                            HubData.get().clearNotificaciones();
                            ctx.getSource().sendSuccess(() -> Component.literal("§c✔ Notificaciones borradas"), true);
                            return 1;
                        }));
    }

    // ── MISIONES ─────────────────────────────────────────────────────────
    private static LiteralArgumentBuilder<CommandSourceStack> buildMissionCommand() {
        return Commands.literal("mission").requires(src -> src.hasPermission(2))

                // /mission list
                .then(Commands.literal("list")
                        .executes(ctx -> {
                            StringBuilder sb = new StringBuilder("§6=== Misiones ===\n");
                            for (Mission m : MissionRegistry.getAll()) {
                                String status = m.isCompleted() ? "§a✔" : (m.isEnabled() ? "§e○" : "§c✗");
                                sb.append(status).append(" §f").append(m.getId())
                                        .append(" §7(").append(m.getProgress()).append("/").append(m.getGoal()).append(")\n");
                            }
                            ctx.getSource().sendSuccess(() -> Component.literal(sb.toString()), false);
                            return 1;
                        }))

                // /mission enable <id>
                .then(Commands.literal("enable")
                        .then(Commands.argument("id", StringArgumentType.string())
                                .executes(ctx -> {
                                    String id = StringArgumentType.getString(ctx, "id");
                                    Mission m = MissionRegistry.get(id);
                                    if (m == null) { ctx.getSource().sendFailure(Component.literal("Misión no encontrada: " + id)); return 0; }
                                    m.setEnabled(true);
                                    HubData.get().saveMissionEnabled(id, true);
                                    ctx.getSource().sendSuccess(() -> Component.literal("§aMisión activada: " + id), true);
                                    return 1;
                                })))

                // /mission disable <id>
                .then(Commands.literal("disable")
                        .then(Commands.argument("id", StringArgumentType.string())
                                .executes(ctx -> {
                                    String id = StringArgumentType.getString(ctx, "id");
                                    Mission m = MissionRegistry.get(id);
                                    if (m == null) { ctx.getSource().sendFailure(Component.literal("Misión no encontrada: " + id)); return 0; }
                                    m.setEnabled(false);
                                    HubData.get().saveMissionEnabled(id, false);
                                    ctx.getSource().sendSuccess(() -> Component.literal("§cMisión desactivada: " + id), true);
                                    return 1;
                                })))

                // /mission reset <id>
                .then(Commands.literal("reset")
                        .then(Commands.argument("id", StringArgumentType.string())
                                .executes(ctx -> {
                                    String id = StringArgumentType.getString(ctx, "id");
                                    Mission m = MissionRegistry.get(id);
                                    if (m == null) { ctx.getSource().sendFailure(Component.literal("Misión no encontrada: " + id)); return 0; }
                                    m.setProgress(0);
                                    m.setCompleted(false);
                                    HubData.get().saveMissionProgress(id, 0, false);
                                    ctx.getSource().sendSuccess(() -> Component.literal("§eMisión reiniciada: " + id), true);
                                    return 1;
                                })))

                // /mission progress <id> <n>
                .then(Commands.literal("progress")
                        .then(Commands.argument("id", StringArgumentType.string())
                                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                        .executes(ctx -> {
                                            String id     = StringArgumentType.getString(ctx, "id");
                                            int    amount = IntegerArgumentType.getInteger(ctx, "amount");
                                            Mission m = MissionRegistry.get(id);
                                            if (m == null) { ctx.getSource().sendFailure(Component.literal("Misión no encontrada: " + id)); return 0; }
                                            m.setProgress(amount);
                                            HubData.get().saveMissionProgress(id, amount, m.isFinished());
                                            ctx.getSource().sendSuccess(() -> Component.literal("§eProgreso de §f" + id + " §ea §f" + amount), true);
                                            return 1;
                                        }))))

                // /mission complete <id>
                .then(Commands.literal("complete")
                        .then(Commands.argument("id", StringArgumentType.string())
                                .executes(ctx -> {
                                    String id = StringArgumentType.getString(ctx, "id");
                                    Mission m = MissionRegistry.get(id);
                                    if (m == null) { ctx.getSource().sendFailure(Component.literal("Misión no encontrada: " + id)); return 0; }
                                    m.setProgress(m.getGoal());
                                    m.setCompleted(true);
                                    HubData.get().saveMissionProgress(id, m.getGoal(), true);
                                    ctx.getSource().sendSuccess(() -> Component.literal("§aMisión completada: " + id), true);
                                    return 1;
                                })));
    }
}
