package net.lluis.catsafio.network;

import net.lluis.catsafio.Catsafio;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.Supplier;

public class FlashbangPackets {

    private static final String VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Catsafio.MOD_ID, "flashbang"),
            () -> VERSION, VERSION::equals, VERSION::equals
    );

    public static void register() {
        CHANNEL.registerMessage(0, MsgFlash.class,
                MsgFlash::encode,
                MsgFlash::decode,
                MsgFlash::handle);
    }

    public static void sendFlash(ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new MsgFlash());
    }

    public static class MsgFlash {

        public static void encode(MsgFlash msg, FriendlyByteBuf buf) {}

        public static MsgFlash decode(FriendlyByteBuf buf) {
            return new MsgFlash();
        }

        public static void handle(MsgFlash msg, Supplier<NetworkEvent.Context> ctxSupplier) {
            NetworkEvent.Context ctx = ctxSupplier.get();
            ctx.enqueueWork(() ->
                    net.lluis.catsafio.client.overlay.FlashbangOverlay.triggerFlash()
            );
            ctx.setPacketHandled(true);
        }
    }
}