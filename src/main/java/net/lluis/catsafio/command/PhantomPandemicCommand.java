package net.lluis.catsafio.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.lluis.catsafio.entity.ModEntities;
import net.lluis.catsafio.entity.custom.FantasmaEntity;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.lluis.catsafio.Catsafio;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PhantomPandemicCommand {

    private static boolean enabled = false;
    private static final Random RANDOM = new Random();
    private static int tickCounter = 0;
    private static final int SPAWN_INTERVAL = 200; // Cada 10 segundos

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("phantompandemic")
            .requires(source -> source.hasPermission(2))
            .then(Commands.argument("enabled", BoolArgumentType.bool())
                .executes(PhantomPandemicCommand::execute))
            .executes(ctx -> {
                ctx.getSource().sendSuccess(() -> 
                    Component.literal("§ePandemia Fantasma: " + (enabled ? "§aEnabled" : "§cDisabled")), false);
                return Command.SINGLE_SUCCESS;
            })
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        enabled = BoolArgumentType.getBool(ctx, "enabled");
        ctx.getSource().sendSuccess(() -> 
            Component.literal(enabled ? 
                "§a✔ §c§lPANDEMIA FANTASMA §aactivada! Fantasmas en desierto, nieve, hongos y bosques rosa!" :
                "§c✘ Pandemia Fantasma desactivada."), true);
        return Command.SINGLE_SUCCESS;
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (!enabled || event.phase != TickEvent.Phase.END) return;

        tickCounter++;
        if (tickCounter < SPAWN_INTERVAL) return;
        tickCounter = 0;

        for (ServerLevel level : event.getServer().getAllLevels()) {
            level.players().forEach(player -> {
                BlockPos playerPos = player.blockPosition();
                ResourceKey<Biome> biomeKey = level.getBiome(playerPos).unwrapKey().orElse(null);
                if (biomeKey == null) return;

                String biomeName = biomeKey.location().toString();
                
                // Verificar biomas: desierto, nieve, hongos, bosques rosa
                if (biomeName.contains("desert") || biomeName.contains("snowy") || 
                    biomeName.contains("mushroom") || biomeName.contains("cherry")) {
                    
                    // Spawn fantasma cerca
                    for (int i = 0; i < 2; i++) {
                        int x = playerPos.getX() + RANDOM.nextInt(32) - 16;
                        int z = playerPos.getZ() + RANDOM.nextInt(32) - 16;
                        int y = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
                        
                        FantasmaEntity fantasma = new FantasmaEntity(ModEntities.FANTASMA.get(), level);
                        fantasma.moveTo(x + 0.5, y, z + 0.5, RANDOM.nextFloat() * 360F, 0);
                        level.addFreshEntity(fantasma);
                    }
                }
            });
        }
    }

    public static boolean isEnabled() { return enabled; }
}
