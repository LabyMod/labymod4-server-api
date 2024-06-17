package net.labymod.serverapi.server.bukkit.event;

import net.labymod.serverapi.api.Protocol;
import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.server.bukkit.LabyModProtocolService;
import net.labymod.serverapi.server.bukkit.player.LabyModPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class LabyModPacketSentEvent extends Event {

  private static final HandlerList handlerList = new HandlerList();

  private final LabyModProtocolService protocolService;
  private final Protocol protocol;
  private final UUID receiver;
  private final Packet packet;

  public LabyModPacketSentEvent(
      @NotNull LabyModProtocolService protocolService,
      @NotNull Protocol protocol,
      @NotNull UUID receiver,
      @NotNull Packet packet
  ) {
    this.protocolService = protocolService;
    this.protocol = protocol;
    this.receiver = receiver;
    this.packet = packet;
  }

  public static HandlerList getHandlerList() {
    return handlerList;
  }

  public @NotNull LabyModProtocolService protocolService() {
    return this.protocolService;
  }

  public @Nullable LabyModPlayer getLabyModPlayer() {
    return this.protocolService.getPlayer(this.receiver);
  }

  public @NotNull Protocol protocol() {
    return this.protocol;
  }

  public @NotNull UUID getReceiver() {
    return this.receiver;
  }

  public @NotNull Packet packet() {
    return this.packet;
  }

  @NotNull
  @Override
  public HandlerList getHandlers() {
    return handlerList;
  }

  @Override
  public String toString() {
    return "LabyModPacketSentEvent{" +
        ", protocol=" + this.protocol.identifier() +
        ", receiver=" + this.receiver +
        ", packet=" + this.packet +
        "}";
  }
}
