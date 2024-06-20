package net.labymod.serverapi.server.bungeecord;

import net.labymod.serverapi.server.common.model.player.AbstractServerLabyModPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class LabyModPlayer extends AbstractServerLabyModPlayer<LabyModPlayer, ProxiedPlayer> {

  public LabyModPlayer(
      LabyModProtocolService protocolService,
      UUID uniqueId,
      ProxiedPlayer player,
      String labyModVersion
  ) {
    super(protocolService, uniqueId, player, labyModVersion);
  }
}
