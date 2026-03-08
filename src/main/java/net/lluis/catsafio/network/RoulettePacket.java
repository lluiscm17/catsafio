package net.lluis.catsafio.network;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.client.RouletteAnimationRenderer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class RoulettePacket {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(Catsafio.MOD_ID, "roulette"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    private final String color;

    public RoulettePacket(String color) {
        this.color = color;
    }

    public static void register() {
        CHANNEL.registerMessage(
            0,
            RoulettePacket.class,
            RoulettePacket::encode,
            RoulettePacket::decode,
            RoulettePacket::handle
        );
    }

    public static void encode(RoulettePacket packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.color);
    }

    public static RoulettePacket decode(FriendlyByteBuf buffer) {
        return new RoulettePacket(buffer.readUtf());
    }

    public static void handle(RoulettePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                RouletteAnimationRenderer.startAnimation(packet.color);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
