package net.labymod.serverapi.server.bukkit;

import net.labymod.serverapi.core.AbstractLabyModProtocolService;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.labymod.serverapi.protocol.protocol.Protocol;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public final class LabyModServerAPIPlugin extends JavaPlugin implements PluginMessageListener {

  private final AbstractLabyModProtocolService protocolService = null;

  @Override
  public void onEnable() {
    // Plugin startup logic

    Messenger messenger = this.getServer().getMessenger();
    this.protocolService.registry().addRegisterListener(protocol -> {
      String identifier = protocol.identifier().toString();
      messenger.registerOutgoingPluginChannel(this, identifier);
      messenger.registerIncomingPluginChannel(this, identifier, this);
    });
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
      protocol.handleIncomingPayload(reader);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
