/*
 * MIT License
 *
 * Copyright (c) 2025 LabyMedia GmbH
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

package net.labymod.serverapi.core.packet.clientbound.game.feature;

import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.model.feature.DiscordRPC;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DiscordRPCPacket implements Packet {

  private DiscordRPC discordRPC;

  public DiscordRPCPacket(@NotNull DiscordRPC discordRPC) {
    Objects.requireNonNull(discordRPC, "DiscordRPC cannot be null");
    this.discordRPC = discordRPC;
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    String gameMode = reader.readOptionalString();
    long startTime = reader.readBoolean() ? reader.readLong() : 0;
    long endTime = reader.readBoolean() ? reader.readLong() : 0;

    if (gameMode == null) {
      this.discordRPC = DiscordRPC.createReset();
      return;
    }

    if (startTime != 0) {
      this.discordRPC = DiscordRPC.createWithStart(gameMode, startTime);
    } else if (endTime != 0) {
      this.discordRPC = DiscordRPC.createWithEnd(gameMode, endTime);
    } else {
      this.discordRPC = DiscordRPC.create(gameMode);
    }
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeOptionalString(this.discordRPC.getGameMode());

    boolean hasStartTime = this.discordRPC.hasStartTime();
    writer.writeBoolean(hasStartTime);
    if (hasStartTime) {
      writer.writeLong(this.discordRPC.getStartTime());
    }

    boolean hasEndTime = this.discordRPC.hasEndTime();
    writer.writeBoolean(hasEndTime);
    if (hasEndTime) {
      writer.writeLong(this.discordRPC.getEndTime());
    }
  }

  public DiscordRPC discordRPC() {
    return this.discordRPC;
  }

  @Override
  public String toString() {
    return "DiscordRPCPacket{" +
        "discordRPC=" + this.discordRPC +
        '}';
  }
}
