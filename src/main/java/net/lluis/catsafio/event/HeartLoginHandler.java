package net.lluis.catsafio.event;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.item.custom.HeartItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HeartLoginHandler {

    private static final UUID[] HEART_UUIDS = {
        UUID.fromString("ca750001-0000-0000-0000-000000000001"),
        UUID.fromString("ca750001-0000-0000-0000-000000000002"),
        UUID.fromString("ca750001-0000-0000-0000-000000000003"),
        UUID.fromString("ca750001-0000-0000-0000-000000000004"),
        UUID.fromString("ca750001-0000-0000-0000-000000000005"),
        UUID.fromString("ca750001-0000-0000-0000-000000000006"),
        UUID.fromString("ca750001-0000-0000-0000-000000000007"),
        UUID.fromString("ca750001-0000-0000-0000-000000000008"),
    };

    private static final double HP_PER_HEART = 2.0;

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        restoreHearts(player);
    }

    // También restaurar al respawnear (los atributos se resetean al morir/respawn)
    // NOTA: HeartDeathHandler ya los borra antes del respawn, así que
    // en respawn el contador ya es 0 — no restauramos nada. Solo en login.
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        // El contador ya fue reseteado a 0 en HeartDeathHandler.onPlayerDeath
        // No hacemos nada aquí
    }

    public static void restoreHearts(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        int hearts = data.getInt(HeartItem.NBT_KEY);
        if (hearts <= 0) return;

        AttributeInstance maxHp = player.getAttribute(Attributes.MAX_HEALTH);
        if (maxHp == null) return;

        // Restaurar modificadores que pudieran haberse perdido al cerrar el juego
        for (int i = 0; i < hearts; i++) {
            UUID uuid = HEART_UUIDS[i];
            if (maxHp.getModifier(uuid) == null) {
                maxHp.addPermanentModifier(new AttributeModifier(
                    uuid,
                    "catsafio_heart_" + i,
                    HP_PER_HEART,
                    AttributeModifier.Operation.ADDITION
                ));
            }
        }
    }
}
