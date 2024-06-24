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

package net.labymod.serverapi.server.bungeecord.event;

import net.labymod.serverapi.api.Protocol;
import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.server.bungeecord.LabyModPlayer;
import net.labymod.serverapi.server.bungeecord.LabyModProtocolService;
import net.md_5.bungee.api.plugin.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class LabyModPacketSentEvent extends Event {

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

  @Override
  public String toString() {
    return "LabyModPacketSentEvent{" +
        ", protocol=" + this.protocol.identifier() +
        ", receiver=" + this.receiver +
        ", packet=" + this.packet +
        "}";
  }
}