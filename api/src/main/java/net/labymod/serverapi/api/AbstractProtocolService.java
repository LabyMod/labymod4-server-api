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

package net.labymod.serverapi.api;

import net.labymod.serverapi.api.logger.ProtocolPlatformLogger;
import net.labymod.serverapi.api.packet.Direction;
import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.PayloadChannelIdentifier;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public abstract class AbstractProtocolService {

  public static final UUID DUMMY_UUID = new UUID(0, 0);

  private final ProtocolRegistry registry;
  private final Side side;

  /**
   * Creates a new protocol service.
   *
   * @param side the side the protocol service is running on. {@link Side#CLIENT} for client-side
   *             and {@link Side#SERVER} for server-side
   */
  protected AbstractProtocolService(@NotNull Side side) {
    Objects.requireNonNull(side, "Side cannot be null");
    this.side = side;
    this.registry = new ProtocolRegistry();
  }

  /**
   * @return the side the protocol service is running on
   */
  public @NotNull Side getSide() {
    return this.side;
  }

  /**
   * @return The protocol registry.
   */
  public final @NotNull ProtocolRegistry registry() {
    return this.registry;
  }

  /**
   * Sends a payload to the recipient on the specified channel.
   *
   * @param identifier the channel to send the payload on
   * @param recipient  the recipient of the payload
   * @param writer     the payload writer
   * @throws NullPointerException  if the identifier, sender or writer is null
   * @throws IllegalStateException if the protocol service is not initialized yet and thus
   *                               {@link #isInitialized()} returns {@code false}
   */
  public abstract void send(
      @NotNull PayloadChannelIdentifier identifier,
      @NotNull UUID recipient,
      @NotNull PayloadWriter writer
  );

  /**
   * @return the logger implementation
   */
  public abstract @NotNull ProtocolPlatformLogger logger();

  /**
   * Checks if the protocol service is initialized.
   *
   * @return {@code true} if the protocol service is initialized, otherwise {@code false}
   */
  public abstract boolean isInitialized();

  /**
   * Called after a packet was received and handled by the protocol.
   *
   * @param protocol the protocol that handled the packet
   * @param packet   the packet that was handled
   * @param sender   the sender of the packet
   */
  public void afterPacketHandled(
      @NotNull Protocol protocol,
      @NotNull Packet packet,
      @NotNull UUID sender
  ) {
    // NO-OP
  }

  /**
   * Called after a packet was sent by the protocol.
   *
   * @param protocol  the protocol that sent the packet
   * @param packet    the packet that was sent
   * @param recipient the recipient of the packet
   */
  public void afterPacketSent(
      @NotNull Protocol protocol,
      @NotNull Packet packet,
      @NotNull UUID recipient
  ) {
    // NO-OP
  }

  public enum Side {
    CLIENT(Direction.CLIENTBOUND),
    SERVER(Direction.SERVERBOUND),
    ;

    private final Direction incomingDirection;

    Side(Direction direction) {
      this.incomingDirection = direction;
    }

    public boolean isAcceptingDirection(Direction direction) {
      return direction == Direction.BOTH || this.incomingDirection == direction;
    }
  }
}
