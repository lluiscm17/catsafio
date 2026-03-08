package net.lluis.catsafio.network;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.client.DeathAnimationRenderer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class DeathAnimationPacket {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(Catsafio.MOD_ID, "death_animation"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    private final String deathMessage;

    public DeathAnimationPacket(String deathMessage) {
        this.deathMessage = deathMessage;
    }

    public static void register() {
        CHANNEL.registerMessage(
            0,
            DeathAnimationPacket.class,
            DeathAnimationPacket::encode,
            DeathAnimationPacket::decode,
            DeathAnimationPacket::handle
        );
    }

    public static void encode(DeathAnimationPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.deathMessage);
    }

    public static DeathAnimationPacket decode(FriendlyByteBuf buffer) {
        return new DeathAnimationPacket(buffer.readUtf());
    }

    public static void handle(DeathAnimationPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Solo ejecutar en cliente
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                DeathAnimationRenderer.startAnimation(packet.deathMessage);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
