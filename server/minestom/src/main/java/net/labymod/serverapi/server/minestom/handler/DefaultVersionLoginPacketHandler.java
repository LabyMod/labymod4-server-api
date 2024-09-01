package net.labymod.serverapi.server.minestom.handler;

import net.labymod.serverapi.api.packet.PacketHandler;
import net.labymod.serverapi.core.packet.serverbound.login.VersionLoginPacket;
import net.labymod.serverapi.server.minestom.LabyModPlayer;
import net.labymod.serverapi.server.minestom.LabyModProtocolService;
import net.labymod.serverapi.server.minestom.event.LabyModPlayerJoinEvent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record DefaultVersionLoginPacketHandler(
        LabyModProtocolService protocolService
) implements PacketHandler<VersionLoginPacket> {
    @Override
    public void handle(@NotNull UUID sender, @NotNull VersionLoginPacket packet) {
        if (protocolService.getPlayer(sender) != null) {
            // player is already connected
            return;
        }

        Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(sender);
        if (player == null) {
            return;
        }

        LabyModPlayer labyModPlayer = new LabyModPlayer(
                protocolService,
                sender,
                player,
                packet.getVersion()
        );

        protocolService.handlePlayerJoin(labyModPlayer);
        MinecraftServer.getGlobalEventHandler().call(new LabyModPlayerJoinEvent(
                protocolService,
                labyModPlayer
        ));
    }
}
