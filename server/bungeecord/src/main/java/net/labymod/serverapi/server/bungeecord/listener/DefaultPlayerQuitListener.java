package net.labymod.serverapi.server.bungeecord.listener;

import net.labymod.serverapi.server.bungeecord.LabyModPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Map;
import java.util.UUID;

public class DefaultPlayerQuitListener implements Listener {

  private final Map<UUID, LabyModPlayer> players;

  public DefaultPlayerQuitListener(Map<UUID, LabyModPlayer> players) {
    this.players = players;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerLeave(PlayerDisconnectEvent event) {
    this.players.remove(event.getPlayer().getUniqueId());
  }
}
