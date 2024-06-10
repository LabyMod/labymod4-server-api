package net.labymod.serverapi.protocol;

import net.labymod.serverapi.protocol.packet.Direction;
import net.labymod.serverapi.protocol.packet.OnlyWriteConstructor;
import net.labymod.serverapi.protocol.packet.Packet;
import net.labymod.serverapi.protocol.packet.PacketHandler;
import net.labymod.serverapi.protocol.payload.PayloadChannelIdentifier;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.labymod.serverapi.protocol.payload.io.PayloadWriter;
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

public abstract class Protocol<T extends AbstractProtocolService> {

  protected final PayloadChannelIdentifier identifier;
  protected final T protocolService;
  protected final Set<ProtocolPacket> packets;
  private final AbstractProtocolService.Side protocolSide;

  /**
   * Creates a new protocol.
   *
   * @param protocolService The protocol service.
   * @param identifier      The identifier of the protocol.
   * @throws NullPointerException If the identifier or protocol service is null.
   */
  protected Protocol(
      @NotNull T protocolService,
      @NotNull PayloadChannelIdentifier identifier
  ) {
    Objects.requireNonNull(identifier, "Identifier cannot be null");
    Objects.requireNonNull(protocolService, "Protocol service cannot be null");
    this.identifier = identifier;
    this.protocolService = protocolService;
    this.protocolSide = protocolService.getSide();
    this.packets = new HashSet<>();
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
   * Sends a packet to the recipient.
   *
   * @param recipient the recipient of the packet
   * @param packet    the packet to send
   * @throws NullPointerException If the recipient or packet is null.
   */
  public void sendPacket(@NotNull UUID recipient, @NotNull Packet packet) {
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
    for (PacketHandler handler : protocolPacket.handlers) {
      handler.handle(sender, packet);
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
