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

package net.labymod.serverapi.core.packet.clientbound.game.feature.marker;

import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MarkerPacket implements Packet {

  private MarkerSendType type;

  /**
   * Creates a new marker packet.
   *
   * @param type the way the client will send markers. See {@link MarkerSendType}
   */
  public MarkerPacket(@NotNull MarkerSendType type) {
    Objects.requireNonNull(type, "Type must not be null");
    this.type = type;
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    String rawType = reader.readString();
    this.type = MarkerSendType.fromString(rawType);
    if (this.type == null) {
      throw new IllegalArgumentException("Invalid marker send type " + rawType);
    }
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeString(this.type);
  }

  /**
   * @return the way the client will send markers. See {@link MarkerSendType}
   */
  public @NotNull MarkerSendType type() {
    return this.type;
  }

  @Override
  public String toString() {
    return "MarkerPacket{" +
        "type=" + this.type +
        '}';
  }

  public enum MarkerSendType {
    /**
     * The client will send markers to the server which can redirect them to other clients.
     */
    SERVER,
    /**
     * The client will send markers to <strong>friends</strong> via the LabyConnect server. This
     * is the default value.
     */
    LABY_CONNECT,
    ;

    private static final MarkerSendType[] VALUES = values();

    public static MarkerSendType fromString(String name) {
      for (MarkerSendType value : VALUES) {
        if (value.name().equalsIgnoreCase(name)) {
          return value;
        }
      }

      return null;
    }
  }
}
