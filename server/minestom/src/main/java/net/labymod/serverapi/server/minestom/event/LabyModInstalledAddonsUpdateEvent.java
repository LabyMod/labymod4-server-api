package net.labymod.serverapi.server.minestom.event;

import net.labymod.serverapi.server.common.model.addon.InstalledAddonsResponse;
import net.labymod.serverapi.server.minestom.LabyModPlayer;
import net.labymod.serverapi.server.minestom.LabyModProtocolService;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public record LabyModInstalledAddonsUpdateEvent(
        LabyModProtocolService protocolService,
        LabyModPlayer player
) implements PlayerEvent {

    public @NotNull LabyModProtocolService protocolService() {
        return this.protocolService;
    }

    public @NotNull LabyModPlayer labyModPlayer() {
        return this.player;
    }

    public @NotNull InstalledAddonsResponse installedAddons() {
        return this.player.installedAddons();
    }

    @Override
    public @NotNull Player getPlayer() {
        return player.getPlayer();
    }
}