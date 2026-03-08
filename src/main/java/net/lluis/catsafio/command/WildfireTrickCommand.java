package net.lluis.catsafio.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.lluis.catsafio.Catsafio;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WildfireTrickCommand {

    private static boolean enabled = false;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("wildfiretrick")
            .requires(source -> source.hasPermission(2))
            .then(Commands.argument("enabled", BoolArgumentType.bool())
                .executes(WildfireTrickCommand::execute))
            .executes(ctx -> {
                ctx.getSource().sendSuccess(() -> 
                    Component.literal("§eWildfire Truco: " + (enabled ? "§aEnabled" : "§cDisabled")), false);
                return Command.SINGLE_SUCCESS;
            })
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        enabled = BoolArgumentType.getBool(ctx, "enabled");
        ctx.getSource().sendSuccess(() -> 
            Component.literal(enabled ? 
                "§a✔ §6§lWILDFIRE TRUCO §aactivado! Los proyectiles de fuego eliminan Fire Resistance!" :
                "§c✘ Wildfire Truco desactivado."), true);
        return Command.SINGLE_SUCCESS;
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!enabled) return;
        
        // Verificar si el daño proviene de fuego
        String damageType = event.getSource().getMsgId();
        
        if (damageType.equals("fireball") || damageType.equals("onFire") || 
            damageType.equals("inFire") || event.getSource().getDirectEntity() instanceof SmallFireball) {
            
            // La entidad que recibe el daño ya es un LivingEntity por el evento
            LivingEntity victim = event.getEntity();
            
            // Remover Fire Resistance
            victim.removeEffect(MobEffects.FIRE_RESISTANCE);
            
            if (victim instanceof net.minecraft.world.entity.player.Player) {
                net.minecraft.world.entity.player.Player player = (net.minecraft.world.entity.player.Player) victim;
                player.displayClientMessage(
                    Component.literal("§c¡Tu Fire Resistance ha sido removida por fuego!"), 
                    true
                );
            }
        }
    }

    public static boolean isEnabled() { return enabled; }
}
