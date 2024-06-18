package net.labymod.serverapi.server.bukkit.listener;

import net.labymod.serverapi.api.Protocol;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.server.bukkit.LabyModProtocolService;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class DefaultPluginMessageListener implements PluginMessageListener {

  private final LabyModProtocolService protocolService;

  public DefaultPluginMessageListener(LabyModProtocolService protocolService) {
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
