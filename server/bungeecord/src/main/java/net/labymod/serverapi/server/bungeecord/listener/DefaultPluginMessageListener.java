package net.labymod.serverapi.server.bungeecord.listener;

import net.labymod.serverapi.api.Protocol;
import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class DefaultPluginMessageListener implements Listener {

  private final Protocol protocol;

  public DefaultPluginMessageListener(Protocol protocol) {
    this.protocol = protocol;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPluginMessage(PluginMessageEvent event) {
    if (!(event.getSender() instanceof ProxiedPlayer)) {
      return;
    }

    String channel = event.getTag();
    if (!channel.equals(this.protocol.identifier().toString())) {
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) event.getSender();

    try {
      PayloadReader reader = new PayloadReader(event.getData());
      Packet packet = this.protocol.handleIncomingPayload(player.getUniqueId(), reader);
      if (packet != null) {
        event.setCancelled(true);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
