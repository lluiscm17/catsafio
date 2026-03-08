package net.lluis.catsafio.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.lluis.catsafio.Catsafio;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EnderPearlDamageCommand {

    private static boolean enabled = false;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("enderpearldamage")
            .requires(source -> source.hasPermission(2))
            .then(Commands.argument("enabled", BoolArgumentType.bool())
                .executes(EnderPearlDamageCommand::execute))
            .executes(ctx -> {
                ctx.getSource().sendSuccess(() -> 
                    Component.literal("§eEnder Pearlll: " + (enabled ? "§aEnabled" : "§cDisabled")), false);
                return Command.SINGLE_SUCCESS;
            })
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        enabled = BoolArgumentType.getBool(ctx, "enabled");
        ctx.getSource().sendSuccess(() -> 
            Component.literal(enabled ? 
                "§a✔ §5§lENDER PEARLLL §aactivado! Quitan 50% de tu vida al usarlas!" :
                "§c✘ Ender Pearlll desactivado."), true);
        return Command.SINGLE_SUCCESS;
    }

    @SubscribeEvent
    public static void onPearlImpact(ProjectileImpactEvent event) {
        if (!enabled) return;
        
        if (event.getProjectile() instanceof ThrownEnderpearl pearl) {
            var owner = pearl.getOwner();
            
            if (owner instanceof Player player) {
                // Quitar 50% de la vida actual
                float currentHealth = player.getHealth();
                float damage = currentHealth * 0.5f;
                
                // Aplicar daño después del teletransporte (pequeño delay)
                player.level().getServer().execute(() -> {
                    player.hurt(player.damageSources().magic(), damage);
                    player.displayClientMessage(
                        Component.literal("§5¡La perla consumió la mitad de tu vida!"), 
                        true
                    );
                });
            }
        }
    }

    public static boolean isEnabled() { return enabled; }
}
