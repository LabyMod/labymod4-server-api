package net.labymod.serverapi.server.bungeecord.event;

import net.labymod.serverapi.server.bungeecord.LabyModPlayer;
import net.labymod.serverapi.server.bungeecord.LabyModProtocolService;
import net.md_5.bungee.api.plugin.Event;
import org.jetbrains.annotations.NotNull;

public class LabyModPlayerJoinEvent extends Event {

  private final LabyModProtocolService protocolService;
  private final LabyModPlayer labyModPlayer;

  public LabyModPlayerJoinEvent(
      @NotNull LabyModProtocolService protocolService,
      @NotNull LabyModPlayer labyModPlayer
  ) {
    this.protocolService = protocolService;
    this.labyModPlayer = labyModPlayer;
  }

  public @NotNull LabyModProtocolService protocolService() {
    return this.protocolService;
  }

  public @NotNull LabyModPlayer labyModPlayer() {
    return this.labyModPlayer;
  }

  @Override
  public String toString() {
    return "LabyModPlayerJoinEvent{" +
        ", labyModPlayer=" + this.labyModPlayer +
        "}";
  }
}
