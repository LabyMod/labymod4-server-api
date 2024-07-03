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

package net.labymod.serverapi.core.packet.serverbound.game.feature.marker;

import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.packet.clientbound.game.feature.marker.AddMarkerPacket;
import net.labymod.serverapi.core.packet.common.game.feature.marker.AbstractMarkerPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ClientAddMarkerPacket extends AbstractMarkerPacket {

  private int version;

  public ClientAddMarkerPacket(
      int version,
      int x,
      int y,
      int z,
      boolean large,
      @Nullable UUID target
  ) {
    super(x, y, z, large, target);
    this.version = version;
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.version = reader.readVarInt();
    super.read(reader);
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeVarInt(this.version);
    super.write(writer);
  }

  public int getVersion() {
    return this.version;
  }

  /**
   * Maps this packet to a {@link AddMarkerPacket}.
   *
   * @param sender The sender of the marker
   * @return a client-bound add marker packet
   */
  public AddMarkerPacket toAddMarkerPacket(UUID sender) {
    return new AddMarkerPacket(sender,
        this.getX(),
        this.getY(),
        this.getZ(),
        this.isLarge(),
        this.getTarget()
    );
  }

  @Override
  public String toString() {
    return "ClientMarkerPacket{" +
        "version=" + this.version +
        "} " + super.toString();
  }
}
