package net.labymod.serverapi.server.minestom.event;

import net.labymod.serverapi.api.Protocol;
import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.server.minestom.LabyModPlayer;
import net.labymod.serverapi.server.minestom.LabyModProtocolService;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public record LabyModPacketSentEvent(
        LabyModProtocolService protocolService,
        Protocol protocol,
        LabyModPlayer player,
        Packet packet
) implements PlayerEvent {

    public @NotNull LabyModProtocolService protocolService() {
        return this.protocolService;
    }

    public @Nullable LabyModPlayer getLabyModPlayer() {
        return player;
    }

    public @NotNull Protocol protocol() {
        return this.protocol;
    }

    public @NotNull Packet packet() {
        return this.packet;
    }

    @Override
    public @NotNull Player getPlayer() {
        return player.getPlayer();
    }
}
