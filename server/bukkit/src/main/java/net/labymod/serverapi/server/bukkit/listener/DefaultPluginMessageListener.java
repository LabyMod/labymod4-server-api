package net.labymod.serverapi.server.bukkit.listener;

import net.labymod.serverapi.api.Protocol;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class DefaultPluginMessageListener implements PluginMessageListener {

  private final Protocol protocol;

  public DefaultPluginMessageListener(Protocol protocol) {
    this.protocol = protocol;
  }

  @Override
  public void onPluginMessageReceived(
      @NotNull String channel,
      @NotNull Player player,
      @NotNull byte[] bytes
  ) {
    if (!channel.equals(this.protocol.identifier().toString())) {
      return;
    }

    try {
      PayloadReader reader = new PayloadReader(bytes);
      this.protocol.handleIncomingPayload(player.getUniqueId(), reader);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
