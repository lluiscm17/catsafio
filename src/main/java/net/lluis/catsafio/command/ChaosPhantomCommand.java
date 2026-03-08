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
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.lluis.catsafio.Catsafio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChaosPhantomCommand {

    private static boolean enabled = false;
    private static final Random RANDOM = new Random();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("chaosphantom")
            .requires(source -> source.hasPermission(2))
            .then(Commands.argument("enabled", BoolArgumentType.bool())
                .executes(ChaosPhantomCommand::execute))
            .executes(ctx -> {
                ctx.getSource().sendSuccess(() -> 
                    Component.literal("§eAdivinen Quiénes Llegaron: " + (enabled ? "§aEnabled" : "§cDisabled")), false);
                return Command.SINGLE_SUCCESS;
            })
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        enabled = BoolArgumentType.getBool(ctx, "enabled");
        ctx.getSource().sendSuccess(() -> 
            Component.literal(enabled ? 
                "§a✔ §5§lADIVINEN QUIÉNES LLEGARON §a! Phantoms revuelven inventario y visión!" :
                "§c✘ Chaos Phantom desactivado."), true);
        return Command.SINGLE_SUCCESS;
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!enabled) return;
        
        // Verificar si un Phantom atacó a un jugador
        if (event.getSource().getEntity() instanceof Phantom && event.getEntity() instanceof Player player) {
            
            // 1. REVOLVER INVENTARIO (aleatorizar slots)
            shuffleInventory(player);
            
            // 2. INTERFERIR CON LA VISIÓN (efectos de ceguera y náusea)
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0)); // 5 segundos de ceguera
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 1)); // 10 segundos de náusea
            
            // 3. Mensaje de caos
            player.displayClientMessage(
                Component.literal("§5§l¡PHANTOM! §7Tu inventario ha sido revuelto..."), 
                true
            );
        }
    }

    private static void shuffleInventory(Player player) {
        // Obtener todos los items del inventario principal (slots 0-35)
        List<ItemStack> items = new ArrayList<>();
        
        // Recoger items
        for (int i = 0; i < 36; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            items.add(stack.copy());
        }
        
        // Mezclar la lista
        Collections.shuffle(items, RANDOM);
        
        // Colocar items mezclados de vuelta
        for (int i = 0; i < 36; i++) {
            player.getInventory().setItem(i, items.get(i));
        }
        
        // Actualizar inventario en el cliente
        player.containerMenu.broadcastChanges();
    }

    public static boolean isEnabled() { return enabled; }
}
