package net.labymod.serverapi.server.bukkit.event;

import net.labymod.serverapi.api.Protocol;
import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.server.bukkit.LabyModPlayer;
import net.labymod.serverapi.server.bukkit.LabyModProtocolService;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class LabyModPacketReceivedEvent extends Event {

  private static final HandlerList handlerList = new HandlerList();

  private final LabyModProtocolService protocolService;
  private final Protocol protocol;
  private final UUID sender;
  private final Packet packet;

  public LabyModPacketReceivedEvent(
      @NotNull LabyModProtocolService protocolService,
      @NotNull Protocol protocol,
      @NotNull UUID sender,
      @NotNull Packet packet
  ) {
    this.protocolService = protocolService;
    this.protocol = protocol;
    this.sender = sender;
    this.packet = packet;
  }

  public static HandlerList getHandlerList() {
    return handlerList;
  }

  public @NotNull LabyModProtocolService protocolService() {
    return this.protocolService;
  }

  public @Nullable LabyModPlayer getLabyModPlayer() {
    return this.protocolService.getPlayer(this.sender);
  }

  public @NotNull Protocol protocol() {
    return this.protocol;
  }

  public @NotNull UUID getSender() {
    return this.sender;
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
    return "LabyModPacketReceivedEvent{" +
        ", protocol=" + this.protocol.identifier() +
        ", sender=" + this.sender +
        ", packet=" + this.packet +
        "}";
  }
}