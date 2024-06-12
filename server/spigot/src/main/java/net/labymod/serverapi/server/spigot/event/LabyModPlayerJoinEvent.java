package net.labymod.serverapi.server.spigot.event;

import net.labymod.serverapi.server.spigot.player.LabyModPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class LabyModPlayerJoinEvent extends Event {

  private static final HandlerList handlerList = new HandlerList();

  private final LabyModPlayer labyModPlayer;

  public LabyModPlayerJoinEvent(final LabyModPlayer labyModPlayer) {
    this.labyModPlayer = labyModPlayer;
  }

  public static HandlerList getHandlerList() {
    return handlerList;
  }

  public @NotNull LabyModPlayer labyModPlayer() {
    return this.labyModPlayer;
  }

  @NotNull
  @Override
  public HandlerList getHandlers() {
    return handlerList;
  }
}
