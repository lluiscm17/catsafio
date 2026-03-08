package net.lluis.catsafio.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.lluis.catsafio.Catsafio;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ExplosiveFireworksCommand {

    private static boolean enabled = false;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("explosivefireworks")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("enabled", BoolArgumentType.bool())
                        .executes(ExplosiveFireworksCommand::execute))
                .executes(ctx -> {
                    ctx.getSource().sendSuccess(() ->
                            Component.literal("§eFeliz Año Nuevo: " + (enabled ? "§aEnabled" : "§cDisabled")), false);
                    return Command.SINGLE_SUCCESS;
                })
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        enabled = BoolArgumentType.getBool(ctx, "enabled");
        ctx.getSource().sendSuccess(() ->
                Component.literal(enabled ?
                        "§a✔ §e§l¡FELIZ AÑO NUEVO! §aPiglins y Brujas usan fuegos artificiales explosivos!" :
                        "§c✘ Feliz Año Nuevo desactivado."), true);
        return Command.SINGLE_SUCCESS;
    }

    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        if (!enabled) return;

        // Verificar si es una flecha disparada por Piglin o Bruja
        if (event.getProjectile() instanceof Arrow arrow) {
            var shooter = arrow.getOwner();

            if (shooter instanceof Piglin || shooter instanceof Witch) {
                // Crear explosión al impactar (fuegos artificiales explosivos)
                // Método correcto para 1.20.1: explode(Entity, double, double, double, float, ExplosionInteraction)
                arrow.level().explode(
                        arrow,                              // Entity que causa la explosión
                        arrow.getX(),                       // X
                        arrow.getY(),                       // Y
                        arrow.getZ(),                       // Z
                        2.5f,                              // Potencia
                        Level.ExplosionInteraction.MOB     // Tipo de interacción (MOB destruye bloques)
                );

                // Efectos visuales de fuegos artificiales
                arrow.level().broadcastEntityEvent(arrow, (byte) 17); // Partículas

                // Remover la flecha para evitar duplicar la explosión
                arrow.discard();
            }
        }
    }

    public static boolean isEnabled() { return enabled; }
}