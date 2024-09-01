package net.labymod.serverapi.server.minestom.event;

import net.labymod.serverapi.server.minestom.LabyModPlayer;
import net.labymod.serverapi.server.minestom.LabyModProtocolService;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public record LabyModPlayerJoinEvent(
        LabyModProtocolService protocolService,
        LabyModPlayer player
) implements PlayerEvent {

    public LabyModProtocolService protocolService() {
        return protocolService;
    }

    public LabyModPlayer getLabyModPlayer() {
        return player;
    }

    @Override
    public @NotNull Player getPlayer() {
        return player.getPlayer();
    }
}
