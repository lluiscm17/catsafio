package net.lluis.catsafio.network;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.client.overlay.BombaAmarillaOverlay;
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

public class BombaAmarillaAttachPacket {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(Catsafio.MOD_ID, "bomba_amarilla_attach"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    private static final Set<UUID> attachedPlayers = new HashSet<>();

    private final boolean startAttached;
    private final int explosionTime;

    public BombaAmarillaAttachPacket(boolean startAttached, int explosionTime) {
        this.startAttached = startAttached;
        this.explosionTime = explosionTime;
    }

    public static void register() {
        CHANNEL.registerMessage(
            0,
            BombaAmarillaAttachPacket.class,
            BombaAmarillaAttachPacket::encode,
            BombaAmarillaAttachPacket::decode,
            BombaAmarillaAttachPacket::handle
        );
    }

    public static void encode(BombaAmarillaAttachPacket packet, FriendlyByteBuf buffer) {
        buffer.writeBoolean(packet.startAttached);
        buffer.writeInt(packet.explosionTime);
    }

    public static BombaAmarillaAttachPacket decode(FriendlyByteBuf buffer) {
        return new BombaAmarillaAttachPacket(buffer.readBoolean(), buffer.readInt());
    }

    public static void handle(BombaAmarillaAttachPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                if (packet.startAttached) {
                    BombaAmarillaOverlay.startAttached(packet.explosionTime);
                } else {
                    BombaAmarillaOverlay.endAttached();
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }

    public static void setAttached(UUID playerId, boolean attached) {
        if (attached) {
            attachedPlayers.add(playerId);
        } else {
            attachedPlayers.remove(playerId);
        }
    }

    public static boolean isAttached(UUID playerId) {
        return attachedPlayers.contains(playerId);
    }
}
