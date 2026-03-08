package net.lluis.catsafio.network;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.roulette.RouletteConfig;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class RouletteCompletionPacket {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(Catsafio.MOD_ID, "roulette_completion"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    private final String color;

    public RouletteCompletionPacket(String color) {
        this.color = color;
    }

    public static void register() {
        CHANNEL.registerMessage(
            0,
            RouletteCompletionPacket.class,
            RouletteCompletionPacket::encode,
            RouletteCompletionPacket::decode,
            RouletteCompletionPacket::handle
        );
    }

    public static void encode(RouletteCompletionPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.color);
    }

    public static RouletteCompletionPacket decode(FriendlyByteBuf buffer) {
        return new RouletteCompletionPacket(buffer.readUtf());
    }

    public static void handle(RouletteCompletionPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                RouletteConfig.RouletteData data = RouletteConfig.get(packet.color);
                
                // Enviar mensaje al chat de todos
                player.getServer().getPlayerList().broadcastSystemMessage(
                    Component.literal(data.message), false);
                
                // Ejecutar comandos configurados
                for (String cmd : data.commands) {
                    player.getServer().getCommands().performPrefixedCommand(
                        player.getServer().createCommandSourceStack(), cmd);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static void send(String color) {
        CHANNEL.sendToServer(new RouletteCompletionPacket(color));
    }
}
