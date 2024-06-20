/*
 * MIT License
 *
 * Copyright (c) 2024 LabyMedia GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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