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

import net.labymod.serverapi.api.packet.Direction;
import net.labymod.serverapi.api.packet.IdentifiablePacket;
import net.labymod.serverapi.api.packet.OnlyWriteConstructor;
import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.packet.PacketHandler;
import net.labymod.serverapi.api.payload.PayloadChannelIdentifier;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class Protocol {

  protected final PayloadChannelIdentifier identifier;
  private final Set<ProtocolPacket> packets;
  private final AbstractProtocolService protocolService;
  private final AbstractProtocolService.Side protocolSide;
  private final Set<AwaitingResponse> awaitingResponses;

  /**
   * Creates a new protocol.
   *
   * @param protocolService The protocol service.
   * @param identifier      The identifier of the protocol.
   * @throws NullPointerException If the identifier or protocol service is null.
   */
  public Protocol(
      @NotNull AbstractProtocolService protocolService,
      @NotNull PayloadChannelIdentifier identifier
  ) {
    Objects.requireNonNull(identifier, "Identifier cannot be null");
    Objects.requireNonNull(protocolService, "Protocol service cannot be null");
    this.identifier = identifier;
    this.protocolService = protocolService;
    this.protocolSide = protocolService.getSide();
    this.packets = new HashSet<>();
    this.awaitingResponses = new HashSet<>();

    if (this.identifier.toString().length() > 20) {
      this.protocolService.logger().warn("The identifier of the protocol " + this.identifier + " "
          + "is longer than 20 characters. This will cause issues with 1.8 & 1.12.2 users.");
    }
  }

  /**
   * @return the identifier of the protocol.
   */
  public final @NotNull PayloadChannelIdentifier identifier() {
    return this.identifier;
  }

  /**
   * Registers a packet to the protocol.
   *
   * @param id          The id of the packet.
   * @param packetClass The class of the packet to register.
   * @param direction   The direction of the packet.
   * @throws NullPointerException If the packet class or direction is null.
   */
  public <T extends Packet> void registerPacket(
      int id,
      @NotNull Class<T> packetClass,
      @NotNull Direction direction
  ) {
    this.registerPacket(id, packetClass, direction, null);
  }

  /**
   * Registers a packet (and optionally its handler) to the protocol.
   *
   * @param id          The id of the packet.
   * @param packetClass The class of the packet to register.
   * @param direction   The direction of the packet.
   * @param handler     The handler for the packet. (optional)
   * @throws NullPointerException If the packet class or direction is null.
   */
  public <T extends Packet> void registerPacket(
      int id,
      @NotNull Class<T> packetClass,
      @NotNull Direction direction,
      @Nullable PacketHandler<T> handler
  ) {
    Objects.requireNonNull(packetClass, "Packet cannot be null");
    Objects.requireNonNull(direction, "Direction cannot be null");
    ProtocolPacket protocolPacket = new ProtocolPacket(id, packetClass, direction);
    if (handler != null) {
      protocolPacket.handlers.add(handler);
    }

    this.packets.add(protocolPacket);
  }

  /**
   * Gets the packet class by the id.
   *
   * @param id The id of the packet.
   * @return the packet class or null if no packet with the id is found.
   */
  public final @Nullable Class<? extends Packet> getPacket(int id) {
    ProtocolPacket protocolPacket = this.getProtocolPacketById(id);
    return protocolPacket != null ? protocolPacket.packet : null;
  }

  public boolean hasPacket(Class<? extends Packet> packetClass) {
    return this.getProtocolPacketByClass(packetClass) != null;
  }

  /**
   * Gets the id of the packet.
   *
   * @param packetClass The class of the packet.
   * @return the id of the packet or {@link Integer#MIN_VALUE} if no packet with the class is
   * found.
   */
  public final int getPacketId(Class<? extends Packet> packetClass) {
    ProtocolPacket protocolPacket = this.getProtocolPacketByClass(packetClass);
    return protocolPacket != null ? protocolPacket.id : Integer.MIN_VALUE;
  }

  /**
   * Registers a handler for the packet.
   *
   * @param packetClass The class of the packet.
   * @param handler     The handler for the packet.
   * @param <T>         The type of the packet.
   * @throws NullPointerException          If the packet class or handler is null.
   * @throws IllegalArgumentException      If no packet with the supplied class has been registered.
   * @throws UnsupportedOperationException If the packet already has a handler.
   */
  public final <T extends Packet> void registerHandler(
      @NotNull Class<T> packetClass,
      @NotNull PacketHandler<T> handler
  ) {
    Objects.requireNonNull(packetClass, "Packet cannot be null");
    Objects.requireNonNull(handler, "Handler cannot be null");
    ProtocolPacket protocolPacket = this.getProtocolPacketByClass(packetClass);
    if (protocolPacket == null) {
      throw new IllegalArgumentException(
          "No packet with the id " + packetClass.getSimpleName() + " "
              + "in protocol " + this.identifier + "found");
    }

    for (PacketHandler packetHandler : protocolPacket.handlers) {
      if (packetHandler == handler) {
        throw new UnsupportedOperationException(
            "Packet " + packetClass.getSimpleName() + " in protocol " + this.identifier
                + " already has " + handler.getClass().getSimpleName() + " as handler");
      }
    }

    protocolPacket.handlers.add(handler);
  }

  /**
   * Calls the handler for the packet.
   *
   * @param sender The sender of the packet.
   * @param packet The packet to handle.
   * @throws NullPointerException     If the sender or packet is null.
   * @throws IllegalArgumentException If no packet with the id has been registered.
   */
  public void handlePacket(@NotNull UUID sender, @NotNull Packet packet) {
    Objects.requireNonNull(sender, "Sender cannot be null");
    Objects.requireNonNull(packet, "Packet cannot be null");
    ProtocolPacket protocolPacket = this.getProtocolPacketByClass(packet.getClass());
    if (protocolPacket == null) {
      throw new IllegalArgumentException(
          "No packet with the id " + packet.getClass().getSimpleName() + " in protocol "
              + this.identifier + "found");
    }

    if (!this.protocolSide.isAcceptingDirection(protocolPacket.direction)) {
      return;
    }

    this.handlePacket(protocolPacket, sender, packet);
  }

  /**
   * Handles the incoming payload.
   *
   * @param sender The sender of the payload.
   * @param reader The reader to read the payload.
   * @return the received packet
   * @throws NullPointerException If the sender or reader is null.
   */
  public @Nullable Packet handleIncomingPayload(
      @NotNull UUID sender,
      @NotNull PayloadReader reader
  ) throws Exception {
    Objects.requireNonNull(sender, "Sender cannot be null");
    Objects.requireNonNull(reader, "Reader cannot be null");
    int id = reader.readVarInt();
    ProtocolPacket protocolPacket = this.getProtocolPacketById(id);
    if (protocolPacket == null) {
      return null;
    }

    if (!this.protocolSide.isAcceptingDirection(protocolPacket.direction)) {
      return null;
    }

    Packet packet = protocolPacket.createPacket();
    packet.read(reader);
    this.handlePacket(protocolPacket, sender, packet);
    return packet;
  }

  /**
   * Sends a packet to the recipient and waits for the provided response
   *
   * @param recipient        the recipient of the packet
   * @param packet           the packet to send
   * @param responseClass    the class of the response packet
   * @param responseCallback the callback for the response packet. Return {@code true} to wait
   *                         for another packet or {@code false} remove the callback.
   * @param <R>              the type of the response packet
   * @throws NullPointerException If the recipient, packet, response class or response callback is
   *                              null.
   */
  public <R extends IdentifiablePacket> void sendPacket(
      @NotNull UUID recipient,
      @NotNull IdentifiablePacket packet,
      @NotNull Class<R> responseClass,
      @NotNull Predicate<R> responseCallback
  ) {
    Objects.requireNonNull(responseClass, "Response class cannot be null");
    Objects.requireNonNull(responseCallback, "Response callback cannot be null");
    this.sendPacketInternal(recipient, packet, responseClass, responseCallback);
  }

  /**
   * Sends a packet to the recipient.
   *
   * @param recipient the recipient of the packet
   * @param packet    the packet to send
   * @throws NullPointerException If the recipient or packet is null.
   */
  public void sendPacket(@NotNull UUID recipient, @NotNull Packet packet) {
    this.sendPacketInternal(recipient, packet, null, null);
  }

  /**
   * Clears all awaiting responses for the recipient.
   *
   * @param recipient the recipient to clear the awaiting responses for
   */
  @ApiStatus.Internal
  public void clearAwaitingResponsesFor(UUID recipient) {
    synchronized (this.awaitingResponses) {
      this.awaitingResponses.removeIf(response -> response.recipient.equals(recipient));
    }
  }

  private ProtocolPacket getProtocolPacket(Predicate<ProtocolPacket> filter) {
    for (ProtocolPacket packet : this.packets) {
      if (filter.test(packet)) {
        return packet;
      }
    }

    return null;
  }

  private ProtocolPacket getProtocolPacketByClass(Class<? extends Packet> packetClass) {
    return this.getProtocolPacket(packet -> packet.packet == packetClass);
  }

  private ProtocolPacket getProtocolPacketById(int id) {
    return this.getProtocolPacket(packet -> packet.id == id);
  }

  private void handlePacket(ProtocolPacket protocolPacket, UUID sender, Packet packet) {
    if (packet instanceof IdentifiablePacket) {
      IdentifiablePacket identifiablePacket = (IdentifiablePacket) packet;
      AwaitingResponse responsePacket = null;
      for (AwaitingResponse awaitingResponse : this.awaitingResponses) {
        if (awaitingResponse.recipient.equals(sender)
            && awaitingResponse.responseClass.equals(protocolPacket.packet)
            && identifiablePacket.getIdentifier() == awaitingResponse.identifier) {
          responsePacket = awaitingResponse;
          break;
        }
      }

      if (responsePacket != null) {
        try {
          if (!responsePacket.responseCallback.test(identifiablePacket)) {
            this.awaitingResponses.remove(responsePacket);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    for (PacketHandler handler : protocolPacket.handlers) {
      handler.handle(sender, packet);
    }

    this.protocolService.afterPacketHandled(this, packet, sender);
  }

  private void sendPacketInternal(
      @NotNull UUID recipient,
      @NotNull Packet packet,
      @Nullable Class<? extends IdentifiablePacket> responseClass,
      @Nullable Predicate<? extends IdentifiablePacket> responseCallback
  ) {
    Objects.requireNonNull(recipient, "Recipient cannot be null");
    Objects.requireNonNull(packet, "Packet cannot be null");
    ProtocolPacket protocolPacket = this.getProtocolPacketByClass(packet.getClass());
    if (protocolPacket == null) {
      throw new IllegalArgumentException(
          "No packet with the class " + packet.getClass().getSimpleName() + " in protocol "
              + this.identifier + "found");
    }

    if (this.protocolSide.isAcceptingDirection(protocolPacket.direction)) {
      return;
    }

    PayloadWriter writer = new PayloadWriter();
    writer.writeVarInt(protocolPacket.id);
    packet.write(writer);
    this.protocolService.send(this.identifier, recipient, writer);
    if (responseClass != null && responseCallback != null
        && packet instanceof IdentifiablePacket) {
      IdentifiablePacket identifiablePacket = (IdentifiablePacket) packet;
      synchronized (this.awaitingResponses) {
        this.awaitingResponses.add(new AwaitingResponse(
            recipient,
            identifiablePacket,
            identifiablePacket.getIdentifier(),
            responseClass,
            responseCallback
        ));
      }
    }

    this.protocolService.afterPacketSent(this, packet, recipient);
  }

  private final class AwaitingResponse {

    private final UUID recipient;
    private final IdentifiablePacket initialPacket;
    private final int identifier;
    private final Class responseClass;
    private final Predicate responseCallback;

    private AwaitingResponse(
        UUID recipient,
        IdentifiablePacket initialPacket,
        int identifier,
        Class responseClass,
        Predicate responseCallback
    ) {
      this.recipient = recipient;
      this.initialPacket = initialPacket;
      this.identifier = identifier;
      this.responseClass = responseClass;
      this.responseCallback = responseCallback;
    }

    public UUID recipient() {
      return this.recipient;
    }

    public IdentifiablePacket initialPacket() {
      return this.initialPacket;
    }

    public int identifier() {
      return this.identifier;
    }

    public Class responseClass() {
      return this.responseClass;
    }

    public Predicate responseCallback() {
      return this.responseCallback;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      }
      if (obj == null || obj.getClass() != this.getClass()) {
        return false;
      }
      AwaitingResponse that = (AwaitingResponse) obj;
      return Objects.equals(this.recipient, that.recipient) &&
          Objects.equals(this.initialPacket, that.initialPacket) &&
          this.identifier == that.identifier &&
          Objects.equals(this.responseClass, that.responseClass) &&
          Objects.equals(this.responseCallback, that.responseCallback);
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.recipient, this.initialPacket, this.identifier, this.responseClass,
          this.responseCallback);
    }

    @Override
    public String toString() {
      return "AwaitingResponse[" +
          "recipient=" + this.recipient + ", " +
          "initialPacket=" + this.initialPacket + ", " +
          "identifier=" + this.identifier + ", " +
          "responseClass=" + this.responseClass + ", " +
          "responseCallback=" + this.responseCallback + ']';
    }
  }

  private static class ProtocolPacket {

    private static final UnsafeProvider UNSAFE_PROVIDER = new UnsafeProvider();

    private final int id;
    private final Class<? extends Packet> packet;
    private final Set<PacketHandler> handlers;
    private final Direction direction;

    private boolean fromConstructor = true;
    private Constructor<? extends Packet> constructor;

    private ProtocolPacket(int id, Class<? extends Packet> packet, Direction direction) {
      this.id = id;
      this.packet = packet;
      this.direction = direction;
      this.handlers = new HashSet<>();
    }

    private Packet createPacket() throws Exception {
      if (this.constructor == null && this.fromConstructor) {
        try {
          this.constructor = this.packet.getDeclaredConstructor();
          this.constructor.setAccessible(true);
          this.fromConstructor = !this.constructor.isAnnotationPresent(
              OnlyWriteConstructor.class
          );
        } catch (Exception e) {
          this.fromConstructor = false;
        }
      }

      if (this.fromConstructor) {
        return this.constructor.newInstance();
      }

      return (Packet) UNSAFE_PROVIDER.unsafe.allocateInstance(this.packet);
    }
  }

  private static final class UnsafeProvider {

    private final Unsafe unsafe;

    private UnsafeProvider() {
      try {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        this.unsafe = (Unsafe) field.get(null);
      } catch (Exception exception) {
        throw new IllegalStateException("Failed to get unsafe instance", exception);
      }
    }
  }
}
