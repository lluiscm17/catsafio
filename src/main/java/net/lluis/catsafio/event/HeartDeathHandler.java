package net.lluis.catsafio.event;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.item.custom.HeartItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HeartDeathHandler {

    // Los mismos UUIDs que en HeartItem
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

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        CompoundTag data = player.getPersistentData();
        int hearts = data.getInt(HeartItem.NBT_KEY);
        if (hearts <= 0) return;

        AttributeInstance maxHp = player.getAttribute(Attributes.MAX_HEALTH);
        if (maxHp == null) return;

        // Quitar todos los modificadores de corazones
        for (int i = 0; i < hearts; i++) {
            maxHp.removeModifier(HEART_UUIDS[i]);
        }

        // Resetear contador
        data.putInt(HeartItem.NBT_KEY, 0);

        player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
            "§c☠ Has perdido tus §c" + hearts + " §ccorazones extra al morir."));
    }
}
