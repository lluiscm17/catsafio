package net.lluis.catsafio.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.lluis.catsafio.Catsafio;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TripleDamageCommand {

    private static boolean tripleDamageEnabled = false;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("tripledamage")
            .requires(source -> source.hasPermission(2))
            .then(Commands.argument("enabled", BoolArgumentType.bool())
                .executes(TripleDamageCommand::execute))
            .executes(ctx -> {
                ctx.getSource().sendSuccess(() -> 
                    Component.literal("§eTriple Damage: " + 
                        (tripleDamageEnabled ? "§aEnabled (x3 damage)" : "§cDisabled")), false);
                return Command.SINGLE_SUCCESS;
            })
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        boolean enable = BoolArgumentType.getBool(ctx, "enabled");
        tripleDamageEnabled = enable;

        if (enable) {
            ctx.getSource().sendSuccess(() -> 
                Component.literal("§a✔ Triple Damage enabled! All vanilla mobs now deal §cx3 §adamage!"), 
                true);
        } else {
            ctx.getSource().sendSuccess(() -> 
                Component.literal("§c✘ Triple Damage disabled. Mobs deal normal damage."), 
                true);
        }

        return Command.SINGLE_SUCCESS;
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!tripleDamageEnabled) return;

        var attacker = event.getSource().getEntity();
        
        // Verificar si el atacante es un mob vanilla (no es de un mod)
        if (attacker instanceof Monster) {
            var entityId = ForgeRegistries.ENTITY_TYPES.getKey(attacker.getType());
            
            // Verificar si es vanilla (namespace "minecraft")
            if (entityId != null && entityId.getNamespace().equals("minecraft")) {
                // Multiplicar el daño por 3
                float originalDamage = event.getAmount();
                event.setAmount(originalDamage * 3.0f);
            }
        }
    }

    public static boolean isEnabled() {
        return tripleDamageEnabled;
    }
}
