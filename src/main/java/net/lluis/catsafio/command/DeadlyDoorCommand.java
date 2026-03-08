package net.lluis.catsafio.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.lluis.catsafio.Catsafio;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DeadlyDoorCommand {

    private static boolean deadlyDoorsEnabled = false;
    private static final float DEADLY_DAMAGE = 5000000f; // 5 millones de daño

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("deadlydoors")
            .requires(source -> source.hasPermission(2))
            .then(Commands.argument("enabled", BoolArgumentType.bool())
                .executes(DeadlyDoorCommand::execute))
            .executes(ctx -> {
                ctx.getSource().sendSuccess(() -> 
                    Component.literal("§eDeadly Doors (Toc Toc... ¿Quién es?): " + 
                        (deadlyDoorsEnabled ? "§aEnabled (5M damage)" : "§cDisabled")), false);
                return Command.SINGLE_SUCCESS;
            })
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        boolean enable = BoolArgumentType.getBool(ctx, "enabled");
        deadlyDoorsEnabled = enable;

        if (enable) {
            ctx.getSource().sendSuccess(() -> 
                Component.literal("§a✔ §6§lTOC TOC... ¿QUIÉN ES? §aLas puertas vanilla ahora hacen §c5 MILLONES §ade daño!"), 
                true);
        } else {
            ctx.getSource().sendSuccess(() -> 
                Component.literal("§c✘ Toc Toc desactivado. Las puertas funcionan normalmente."), 
                true);
        }

        return Command.SINGLE_SUCCESS;
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!deadlyDoorsEnabled) return;
        
        // Verificar si el bloque clickeado es una puerta
        var blockState = event.getLevel().getBlockState(event.getPos());
        if (blockState.getBlock() instanceof DoorBlock) {
            Player player = event.getEntity();
            
            // Aplicar 5 millones de daño
            player.hurt(player.damageSources().generic(), DEADLY_DAMAGE);
            
            // Mensaje de muerte personalizado (el jugador morirá antes de verlo)
            player.displayClientMessage(
                Component.literal("§c§l💀 TOC TOC... ¡LA MUERTE!"), 
                true
            );
        }
    }

    public static boolean isEnabled() {
        return deadlyDoorsEnabled;
    }
}
