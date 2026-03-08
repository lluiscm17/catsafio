package net.lluis.catsafio.event;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.roulette.RouletteConfig;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Carga la configuración de ruletas cuando el servidor inicia.
 * Crea el archivo catsafio_roulettes.json en la carpeta del mundo.
 */
@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RouletteConfigLoader {

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        try {
            RouletteConfig.load(event.getServer());
            Catsafio.LOGGER.info("✅ Roulette configuration loaded successfully");
        } catch (Exception e) {
            Catsafio.LOGGER.error("❌ Failed to load roulette configuration", e);
        }
    }
}
