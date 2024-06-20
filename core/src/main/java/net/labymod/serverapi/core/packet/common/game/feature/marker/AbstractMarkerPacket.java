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

package net.labymod.serverapi.core.packet.common.game.feature.marker;

import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class AbstractMarkerPacket implements Packet {

  private int x;
  private int y;
  private int z;
  private boolean large;
  private UUID target;

  protected AbstractMarkerPacket(int x, int y, int z, boolean large, @Nullable UUID target) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.large = large;
    this.target = target;
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.x = reader.readVarInt();
    this.y = reader.readVarInt();
    this.z = reader.readVarInt();
    this.large = reader.readBoolean();
    this.target = reader.readOptional(reader::readUUID);
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeVarInt(this.x);
    writer.writeVarInt(this.y);
    writer.writeVarInt(this.z);
    writer.writeBoolean(this.large);
    writer.writeOptional(this.target, writer::writeUUID);
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public int getZ() {
    return this.z;
  }

  public boolean isLarge() {
    return this.large;
  }

  public @Nullable UUID getTarget() {
    return this.target;
  }

  @Override
  public String toString() {
    return "AbstractMarkerPacket{" +
        "x=" + this.x +
        ", y=" + this.y +
        ", z=" + this.z +
        ", large=" + this.large +
        ", target=" + this.target +
        '}';
  }
}
