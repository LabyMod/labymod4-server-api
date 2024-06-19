package net.labymod.serverapi.server.bukkit.player;

import net.labymod.serverapi.server.bukkit.LabyModProtocolService;
import net.labymod.serverapi.server.common.model.player.AbstractLabyModPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LabyModPlayer extends AbstractLabyModPlayer<Player> {

  public LabyModPlayer(
      LabyModProtocolService protocolService,
      UUID uniqueId,
      Player player,
      String labyModVersion
  ) {
    super(protocolService, uniqueId, player, labyModVersion);
  }
}