package net.labymod.serverapi.server.spigot.listener;

import net.labymod.serverapi.protocol.Protocol;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.labymod.serverapi.server.spigot.LabyModProtocolService;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PluginMessageListener implements org.bukkit.plugin.messaging.PluginMessageListener {

  private final LabyModProtocolService protocolService;

  public PluginMessageListener(LabyModProtocolService protocolService) {
    this.protocolService = protocolService;
  }

  @Override
  public void onPluginMessageReceived(
      @NotNull String channel,
      @NotNull Player player,
      @NotNull byte[] bytes
  ) {
    Protocol protocol = null;
    for (Protocol registeredProtocol : this.protocolService.registry().getProtocols()) {
      if (registeredProtocol.identifier().toString().equals(channel)) {
        protocol = registeredProtocol;
        break;
      }
    }

    if (protocol == null) {
      return;
    }

    try {
      PayloadReader reader = new PayloadReader(bytes);
      protocol.handleIncomingPayload(player.getUniqueId(), reader);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
