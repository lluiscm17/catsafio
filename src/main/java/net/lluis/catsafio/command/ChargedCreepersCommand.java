package net.lluis.catsafio.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.lluis.catsafio.Catsafio;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChargedCreepersCommand {

    private static boolean chargedCreepersEnabled = false;
    private static Field poweredField = null;

    static {
        // Obtener el campo privado DATA_IS_POWERED usando reflexión
        try {
            // Buscar el campo "entityData" que contiene DATA_IS_POWERED
            // En vez de acceder directamente, usaremos un método más simple
            for (Field field : Creeper.class.getDeclaredFields()) {
                if (field.getType() == boolean.class) {
                    field.setAccessible(true);
                    // Probar si es el campo correcto
                    poweredField = field;
                    break;
                }
            }
        } catch (Exception e) {
            Catsafio.LOGGER.error("Failed to access Creeper powered field", e);
        }
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("chargedcreepers")
            .requires(source -> source.hasPermission(2))
            .then(Commands.argument("enabled", BoolArgumentType.bool())
                .executes(ChargedCreepersCommand::execute))
            .executes(ctx -> {
                ctx.getSource().sendSuccess(() -> 
                    Component.literal("§eCharged Creepers: " + 
                        (chargedCreepersEnabled ? "§aEnabled" : "§cDisabled")), false);
                return Command.SINGLE_SUCCESS;
            })
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        boolean enable = BoolArgumentType.getBool(ctx, "enabled");
        chargedCreepersEnabled = enable;

        if (enable) {
            // Convertir todos los creepers existentes en eléctricos
            ServerLevel level = ctx.getSource().getLevel();
            int count = 0;
            for (var entity : level.getAllEntities()) {
                if (entity instanceof Creeper creeper && !creeper.isPowered()) {
                    makeCharged(creeper);
                    count++;
                }
            }
            final int finalCount = count;
            ctx.getSource().sendSuccess(() -> 
                Component.literal("§a✔ Charged Creepers enabled! Converted " + finalCount + 
                    " creepers. All new creepers will spawn charged during day."), 
                true);
        } else {
            ctx.getSource().sendSuccess(() -> 
                Component.literal("§c✘ Charged Creepers disabled. Creepers spawn normally."), 
                true);
        }

        return Command.SINGLE_SUCCESS;
    }

    private static void makeCharged(Creeper creeper) {
        // Método seguro usando NBT
        var nbt = creeper.serializeNBT();
        nbt.putBoolean("powered", true);
        creeper.deserializeNBT(nbt);
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (!chargedCreepersEnabled) return;
        if (event.getLevel().isClientSide()) return;
        
        if (event.getEntity() instanceof Creeper creeper) {
            makeCharged(creeper);
        }
    }

    @SubscribeEvent
    public static void onCheckSpawn(MobSpawnEvent.PositionCheck event) {
        if (!chargedCreepersEnabled) return;
        
        if (event.getEntity() instanceof Creeper) {
            // Forzar que el spawn sea permitido
            event.setResult(Event.Result.DEFAULT);
        }
    }

    public static boolean isEnabled() {
        return chargedCreepersEnabled;
    }
}
