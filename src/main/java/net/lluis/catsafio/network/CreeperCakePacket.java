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

public class CreeperCakePacket {

    private static final String VERSION = "1";
    public static SimpleChannel CHANNEL;

    public static void register() {
        CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(Catsafio.MOD_ID, "creepercake"),
                () -> VERSION,
                VERSION::equals,
                VERSION::equals
        );

        // Firmas correctas para Forge 1.20.1:
        // encoder:  BiConsumer<MSG, FriendlyByteBuf>
        // decoder:  Function<FriendlyByteBuf, MSG>
        // handler:  BiConsumer<MSG, Supplier<NetworkEvent.Context>>
        CHANNEL.registerMessage(
                0,
                CreeperCakePacket.class,
                (pkt, buf) -> {},                     // encoder
                buf -> new CreeperCakePacket(),        // decoder
                (pkt, ctxSupplier) -> {               // handler
                    NetworkEvent.Context ctx = ctxSupplier.get();
                    ctx.enqueueWork(() ->
                            net.minecraft.client.Minecraft.getInstance().execute(() ->
                                    net.lluis.catsafio.client.overlay.CreeperCakeOverlay.show()
                            )
                    );
                    ctx.setPacketHandled(true);
                },
                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
    }

    public static void send(ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new CreeperCakePacket());
    }
}
