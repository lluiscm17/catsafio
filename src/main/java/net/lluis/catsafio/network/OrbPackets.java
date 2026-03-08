package net.lluis.catsafio.network;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.entity.custom.OrbEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class OrbPackets {

    private static final String VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(Catsafio.MOD_ID, "orb"),
        () -> VERSION, VERSION::equals, VERSION::equals
    );

    public static void register() {
        // S→C: abrir GUI
        CHANNEL.registerMessage(0, MsgOpenGui.class,
            MsgOpenGui::encode, MsgOpenGui::decode, MsgOpenGui::handle);
        // C→S: guardar configuración
        CHANNEL.registerMessage(1, MsgSaveConfig.class,
            MsgSaveConfig::encode, MsgSaveConfig::decode, MsgSaveConfig::handle);
    }

    // ── Server → Client: abrir GUI ─────────────────────────────────────
    public static void sendOpenGui(ServerPlayer player, int entityId,
                                   String selectedMob, int interval) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
            new MsgOpenGui(entityId, selectedMob, interval));
    }

    public static class MsgOpenGui {
        public final int entityId;
        public final String selectedMob;
        public final int interval;

        public MsgOpenGui(int id, String mob, int interval) {
            this.entityId = id; this.selectedMob = mob; this.interval = interval;
        }

        public static void encode(MsgOpenGui m, FriendlyByteBuf buf) {
            buf.writeInt(m.entityId);
            buf.writeUtf(m.selectedMob);
            buf.writeInt(m.interval);
        }

        public static MsgOpenGui decode(FriendlyByteBuf buf) {
            return new MsgOpenGui(buf.readInt(), buf.readUtf(), buf.readInt());
        }

        public static void handle(MsgOpenGui msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() ->
                net.lluis.catsafio.screen.orb.OrbScreen.open(
                    msg.entityId, msg.selectedMob, msg.interval));
            ctx.get().setPacketHandled(true);
        }
    }

    // ── Client → Server: guardar configuración ─────────────────────────
    public static void sendSaveConfig(int entityId, String mobId, int interval) {
        CHANNEL.sendToServer(new MsgSaveConfig(entityId, mobId, interval));
    }

    public static class MsgSaveConfig {
        public final int entityId;
        public final String mobId;
        public final int interval;

        public MsgSaveConfig(int id, String mob, int interval) {
            this.entityId = id; this.mobId = mob; this.interval = interval;
        }

        public static void encode(MsgSaveConfig m, FriendlyByteBuf buf) {
            buf.writeInt(m.entityId);
            buf.writeUtf(m.mobId);
            buf.writeInt(m.interval);
        }

        public static MsgSaveConfig decode(FriendlyByteBuf buf) {
            return new MsgSaveConfig(buf.readInt(), buf.readUtf(), buf.readInt());
        }

        public static void handle(MsgSaveConfig msg, Supplier<NetworkEvent.Context> ctx) {
            ServerPlayer player = ctx.get().getSender();
            ctx.get().enqueueWork(() -> {
                if (player == null) return;
                Entity entity = player.level().getEntity(msg.entityId);
                if (entity instanceof OrbEntity orb && player.isCreative()) {
                    orb.setSelectedMob(msg.mobId);
                    orb.setSpawnInterval(msg.interval);
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
