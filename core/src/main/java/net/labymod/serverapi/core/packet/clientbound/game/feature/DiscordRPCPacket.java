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

package net.labymod.serverapi.core.packet.clientbound.game.feature;

import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DiscordRPCPacket implements Packet {

  private String gameMode;
  private long startTime;
  private long endTime;

  private DiscordRPCPacket(@Nullable String gameMode, long startTime, long endTime) {
    this.gameMode = gameMode;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public static DiscordRPCPacket createReset() {
    return new DiscordRPCPacket(null, 0, 0);
  }

  public static DiscordRPCPacket create(String gameMode) {
    return new DiscordRPCPacket(gameMode, 0, 0);
  }

  public static DiscordRPCPacket createWithStart(String gameMode, long startTime) {
    return new DiscordRPCPacket(gameMode, startTime, 0);
  }

  public static DiscordRPCPacket createWithEnd(String gameMode, long endTime) {
    return new DiscordRPCPacket(gameMode, 0, endTime);
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.gameMode = reader.readOptionalString();
    if (reader.readBoolean()) {
      this.startTime = reader.readLong();
    }

    if (reader.readBoolean()) {
      this.endTime = reader.readLong();
    }
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeOptionalString(this.gameMode);

    boolean hasStartTime = this.startTime != 0;
    writer.writeBoolean(hasStartTime);
    if (hasStartTime) {
      writer.writeLong(this.startTime);
    }

    boolean hasEndTime = this.endTime != 0;
    writer.writeBoolean(hasEndTime);
    if (hasEndTime) {
      writer.writeLong(this.endTime);
    }
  }

  public boolean hasGameMode() {
    return this.gameMode != null;
  }

  public @Nullable String getGameMode() {
    return this.gameMode;
  }

  public long getStartTime() {
    return this.startTime;
  }

  public long getEndTime() {
    return this.endTime;
  }

  @Override
  public String toString() {
    return "DiscordRPCPacket{" +
        "gameMode='" + this.gameMode + '\'' +
        ", startTime=" + this.startTime +
        ", endTime=" + this.endTime +
        '}';
  }
}
