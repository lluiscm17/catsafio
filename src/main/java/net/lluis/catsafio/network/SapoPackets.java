package net.lluis.catsafio.network;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.client.overlay.SapoCaptureOverlay;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class SapoPackets {

    private static final String VERSION = "1";
    public static SimpleChannel CHANNEL;

    public static void register() {
        CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Catsafio.MOD_ID, "sapo"),
            () -> VERSION, VERSION::equals, VERSION::equals
        );

        // 0: inicio captura por lengua → mostrar UI pulsa espacio
        CHANNEL.registerMessage(0, MsgCaptureStart.class,
            (m, buf) -> {},
            buf -> new MsgCaptureStart(),
            (m, ctx) -> {
                ctx.get().enqueueWork(() ->
                    net.minecraft.client.Minecraft.getInstance().execute(() ->
                        SapoCaptureOverlay.onCaptureStart()));
                ctx.get().setPacketHandled(true);
            }, Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        // 1: fin captura (escape o fallo)
        CHANNEL.registerMessage(1, MsgCaptureEnd.class,
            (m, buf) -> {},
            buf -> new MsgCaptureEnd(),
            (m, ctx) -> {
                ctx.get().enqueueWork(() ->
                    net.minecraft.client.Minecraft.getInstance().execute(() ->
                        SapoCaptureOverlay.onCaptureEnd()));
                ctx.get().setPacketHandled(true);
            }, Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        // 2: tragado (dentro del sapo)
        CHANNEL.registerMessage(2, MsgSwallowed.class,
            (m, buf) -> buf.writeBoolean(m.inside),
            buf -> new MsgSwallowed(buf.readBoolean()),
            (m, ctx) -> {
                ctx.get().enqueueWork(() ->
                    net.minecraft.client.Minecraft.getInstance().execute(() ->
                        SapoCaptureOverlay.onSwallowed(m.inside)));
                ctx.get().setPacketHandled(true);
            }, Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        // 3: cliente → servidor: jugador pulsó espacio
        CHANNEL.registerMessage(3, MsgSpacePress.class,
            (m, buf) -> {},
            buf -> new MsgSpacePress(),
            (m, ctx) -> {
                ServerPlayer player = ctx.get().getSender();
                ctx.get().enqueueWork(() -> {
                    if (player == null) return;
                    SapoPackets.handleSpacePressServer(player);
                });
                ctx.get().setPacketHandled(true);
            }, Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }

    // ── API servidor → cliente ─────────────────────────────────────────
    public static void sendCaptureStart(ServerPlayer p) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> p), new MsgCaptureStart());
    }
    public static void sendCaptureEnd(ServerPlayer p) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> p), new MsgCaptureEnd());
    }
    public static void sendSwallowed(ServerPlayer p, boolean inside) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> p), new MsgSwallowed(inside));
    }

    // ── API cliente → servidor ─────────────────────────────────────────
    public static void sendSpacePress() {
        CHANNEL.sendToServer(new MsgSpacePress());
    }

    /** Lógica servidor: el escape es solo matando al sapo (pulsaciones solo son UI) */
    private static void handleSpacePressServer(ServerPlayer player) {
        // El escape visual lo maneja el cliente; la liberación real ocurre en SapoEntity.die()
    }

    // ── Mensajes ───────────────────────────────────────────────────────
    public record MsgCaptureStart() {}
    public record MsgCaptureEnd()   {}
    public record MsgSwallowed(boolean inside) {}
    public record MsgSpacePress()   {}
}
