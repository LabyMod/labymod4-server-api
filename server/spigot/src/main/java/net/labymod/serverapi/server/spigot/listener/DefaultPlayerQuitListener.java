package net.labymod.serverapi.server.spigot.listener;

import net.labymod.serverapi.server.spigot.player.LabyModPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;

public class DefaultPlayerQuitListener implements Listener {

  private final Map<UUID, LabyModPlayer> players;

  public DefaultPlayerQuitListener(Map<UUID, LabyModPlayer> players) {
    this.players = players;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerLeave(PlayerQuitEvent event) {
    this.players.remove(event.getPlayer().getUniqueId());
  }
}
