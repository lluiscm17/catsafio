package net.lluis.catsafio.network;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.client.overlay.GorgonaPetrificationOverlay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

public class GorgonaPetrificationPacket {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(Catsafio.MOD_ID, "gorgona_petrification"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    // Trackear jugadores petrificados (servidor)
    private static final Set<UUID> petrifiedPlayers = new HashSet<>();

    private final boolean startPetrification;

    public GorgonaPetrificationPacket(boolean startPetrification) {
        this.startPetrification = startPetrification;
    }

    public static void register() {
        CHANNEL.registerMessage(
            0,
            GorgonaPetrificationPacket.class,
            GorgonaPetrificationPacket::encode,
            GorgonaPetrificationPacket::decode,
            GorgonaPetrificationPacket::handle
        );
    }

    public static void encode(GorgonaPetrificationPacket packet, FriendlyByteBuf buffer) {
        buffer.writeBoolean(packet.startPetrification);
    }

    public static GorgonaPetrificationPacket decode(FriendlyByteBuf buffer) {
        return new GorgonaPetrificationPacket(buffer.readBoolean());
    }

    public static void handle(GorgonaPetrificationPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                if (packet.startPetrification) {
                    GorgonaPetrificationOverlay.startPetrification();
                } else {
                    GorgonaPetrificationOverlay.endPetrification();
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }

    // Métodos de servidor
    public static void setPetrified(UUID playerId, boolean petrified) {
        if (petrified) {
            petrifiedPlayers.add(playerId);
        } else {
            petrifiedPlayers.remove(playerId);
        }
    }

    public static boolean isPetrified(UUID playerId) {
        return petrifiedPlayers.contains(playerId);
    }
}
