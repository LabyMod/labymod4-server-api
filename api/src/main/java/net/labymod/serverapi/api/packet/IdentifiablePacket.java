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

package net.labymod.serverapi.api.packet;

import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an identifiable packet. Commonly used if a packet has a corresponding response
 * packet. An uuid is randomly generated upon creation of the packet, which can be used to
 * identify the response to the initial packet (if it was implementing correctly, so if both
 * {@link #read(PayloadReader)} and {@link #write(PayloadWriter)} call the super method).
 */
public abstract class IdentifiablePacket implements Packet {

  private static int ID = 0;

  private int identifier = -1;

  /**
   * Default constructor. Use this when creating an initial packet.
   */
  protected IdentifiablePacket() {
    this.identifier = ID++;
    if (ID == Integer.MAX_VALUE) {
      ID = 0;
    }
  }

  /**
   * Constructor for supplying the response packet with the initiator packet so that the
   * identifier stays the same.
   *
   * @param initiator when sending, provide initiator packet of the current packet. When reading:
   *                  {@code null} to not unnecessarily increase the identifier;
   */
  protected IdentifiablePacket(@Nullable IdentifiablePacket initiator) {
    if (initiator == null) {
      return;
    }

    if (initiator == this) {
      throw new IllegalArgumentException(
          "Initiator packet cannot be the same as the current packet"
      );
    }

    this.identifier = initiator.getIdentifier();
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.identifier = reader.readVarInt();
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeVarInt(this.identifier);
  }

  /**
   * Gets the identifier of the packet. Might return -1 when the wrong constructor was used.
   *
   * @return the identifier of the packet
   */
  public int getIdentifier() {
    return this.identifier;
  }

  /**
   * Sets the identifier of the packet. Do not use this if you don't know what you're doing
   *
   * @param identifier the identifier to set
   */
  @ApiStatus.Internal
  public void setIdentifier(int identifier) {
    this.identifier = identifier;
  }

  @Override
  public String toString() {
    return "IdentifiablePacket{" +
        "identifier=" + this.identifier +
        '}';
  }
}
