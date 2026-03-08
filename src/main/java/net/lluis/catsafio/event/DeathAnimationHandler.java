package net.lluis.catsafio.event;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.network.DeathAnimationPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DeathAnimationHandler {

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        
        // Obtener mensaje de muerte
        DamageSource source = event.getSource();
        Component deathMessage = source.getLocalizedDeathMessage(player);
        String deathText = deathMessage.getString();
        
        // Enviar packet al cliente para iniciar animación
        DeathAnimationPacket.CHANNEL.send(
            PacketDistributor.PLAYER.with(() -> player),
            new DeathAnimationPacket(deathText)
        );
        
        Catsafio.LOGGER.info("Death animation triggered for {}: {}", 
            player.getName().getString(), deathText);
    }
}
