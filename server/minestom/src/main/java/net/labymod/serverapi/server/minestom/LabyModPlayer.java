package net.labymod.serverapi.server.minestom;

import net.labymod.serverapi.core.AbstractLabyModPlayer;
import net.labymod.serverapi.core.AbstractLabyModProtocolService;
import net.labymod.serverapi.server.common.AbstractServerLabyModProtocolService;
import net.labymod.serverapi.server.common.model.player.AbstractServerLabyModPlayer;
import net.minestom.server.entity.Player;

import java.util.UUID;

public class LabyModPlayer extends AbstractServerLabyModPlayer<LabyModPlayer, Player> {
    public LabyModPlayer(AbstractServerLabyModProtocolService<?> protocolService, UUID uniqueId, Player player, String labyModVersion) {
        super(protocolService, uniqueId, player, labyModVersion);
    }
}
