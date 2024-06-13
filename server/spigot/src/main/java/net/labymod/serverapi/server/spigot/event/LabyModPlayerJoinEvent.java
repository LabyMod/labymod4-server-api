package net.labymod.serverapi.server.spigot.event;

import net.labymod.serverapi.server.spigot.LabyModProtocolService;
import net.labymod.serverapi.server.spigot.player.LabyModPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class LabyModPlayerJoinEvent extends Event {

  private static final HandlerList handlerList = new HandlerList();

  private final LabyModProtocolService protocolService;
  private final LabyModPlayer labyModPlayer;

  public LabyModPlayerJoinEvent(
      @NotNull LabyModProtocolService protocolService,
      @NotNull LabyModPlayer labyModPlayer
  ) {
    this.protocolService = protocolService;
    this.labyModPlayer = labyModPlayer;
  }

  public static HandlerList getHandlerList() {
    return handlerList;
  }

  public @NotNull LabyModProtocolService protocolService() {
    return this.protocolService;
  }

  public @NotNull LabyModPlayer labyModPlayer() {
    return this.labyModPlayer;
  }

  @NotNull
  @Override
  public HandlerList getHandlers() {
    return handlerList;
  }

  @Override
  public String toString() {
    return "LabyModPlayerJoinEvent{" +
        ", labyModPlayer=" + this.labyModPlayer +
        "}";
  }
}
