package net.lluis.catsafio.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.lluis.catsafio.entity.ModEntities;
import net.lluis.catsafio.entity.custom.CreeperCakeEntity;
import net.lluis.catsafio.entity.custom.FlashbangEntity;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.lluis.catsafio.Catsafio;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CrazyHourCommand {

    private static boolean crazyHourEnabled = false;
    private static final Random RANDOM = new Random();
    private static int tickCounter = 0;
    private static final int SPAWN_INTERVAL = 100; // Cada 5 segundos (100 ticks)

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("crazyhour")
            .requires(source -> source.hasPermission(2))
            .then(Commands.argument("enabled", BoolArgumentType.bool())
                .executes(CrazyHourCommand::execute))
            .executes(ctx -> {
                ctx.getSource().sendSuccess(() -> 
                    Component.literal("§eCrazy Hour (Hora Loca): " + 
                        (crazyHourEnabled ? "§aEnabled" : "§cDisabled")), false);
                return Command.SINGLE_SUCCESS;
            })
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        boolean enable = BoolArgumentType.getBool(ctx, "enabled");
        crazyHourEnabled = enable;

        if (enable) {
            ctx.getSource().sendSuccess(() -> 
                Component.literal("§a✔ §6§lHORA LOCA §aactivada! Flashbangs y Creeper Cakes spawnean en dirt paths!"), 
                true);
        } else {
            ctx.getSource().sendSuccess(() -> 
                Component.literal("§c✘ Hora Loca desactivada. Spawn normal."), 
                true);
        }

        return Command.SINGLE_SUCCESS;
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (!crazyHourEnabled || event.phase != TickEvent.Phase.END) return;

        tickCounter++;
        if (tickCounter < SPAWN_INTERVAL) return;
        tickCounter = 0;

        // Iterar por todos los mundos del servidor
        for (ServerLevel level : event.getServer().getAllLevels()) {
            // Buscar dirt paths en el mundo y spawnear mobs
            level.players().forEach(player -> {
                // Buscar dirt paths cerca del jugador (radio 32 bloques)
                BlockPos playerPos = player.blockPosition();
                
                for (int attempt = 0; attempt < 5; attempt++) {
                    int x = playerPos.getX() + RANDOM.nextInt(64) - 32;
                    int z = playerPos.getZ() + RANDOM.nextInt(64) - 32;
                    int y = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
                    
                    BlockPos spawnPos = new BlockPos(x, y, z);
                    
                    // Verificar si hay un dirt path
                    if (level.getBlockState(spawnPos.below()).is(Blocks.DIRT_PATH)) {
                        // 50% Flashbang, 50% Creeper Cake
                        if (RANDOM.nextBoolean()) {
                            FlashbangEntity flashbang = new FlashbangEntity(ModEntities.FLASHBANG.get(), level);
                            flashbang.moveTo(x + 0.5, y, z + 0.5, RANDOM.nextFloat() * 360F, 0);
                            level.addFreshEntity(flashbang);
                        } else {
                            CreeperCakeEntity creeperCake = new CreeperCakeEntity(ModEntities.CREEPER_CAKE.get(), level);
                            creeperCake.moveTo(x + 0.5, y, z + 0.5, RANDOM.nextFloat() * 360F, 0);
                            level.addFreshEntity(creeperCake);
                        }
                        break; // Solo un spawn por jugador por tick
                    }
                }
            });
        }
    }

    public static boolean isEnabled() {
        return crazyHourEnabled;
    }
}
