package net.labymod.serverapi.protocol.protocol;

import net.labymod.serverapi.protocol.packet.Packet;
import net.labymod.serverapi.protocol.packet.PacketHandler;
import net.labymod.serverapi.protocol.payload.identifier.PayloadChannelIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

public class Protocol {

  protected final PayloadChannelIdentifier identifier;
  protected final Set<ProtocolPacket> packets;

  /**
   * Creates a new protocol.
   *
   * @param identifier The identifier of the protocol.
   * @throws NullPointerException If the identifier is null.
   */
  protected Protocol(@NotNull PayloadChannelIdentifier identifier) {
    Objects.requireNonNull(identifier, "Identifier cannot be null");
    this.identifier = identifier;
    this.packets = new HashSet<>();
  }

  /**
   * Registers a packet to the protocol.
   *
   * @param id          The id of the packet.
   * @param packetClass The class of the packet to register.
   * @throws NullPointerException If the packet class is null.
   */
  public <T extends Packet> void registerPacket(int id, @NotNull Class<T> packetClass) {
    this.registerPacket(id, packetClass, null);
  }

  /**
   * Registers a packet (and optionally its handler) to the protocol.
   *
   * @param id          The id of the packet.
   * @param packetClass The class of the packet to register.
   * @param handler     The handler for the packet. (optional)
   * @throws NullPointerException If the packet class is null.
   */
  public <T extends Packet> void registerPacket(
      int id,
      @NotNull Class<T> packetClass,
      @Nullable PacketHandler<T> handler
  ) {
    Objects.requireNonNull(packetClass, "Packet cannot be null");
    ProtocolPacket protocolPacket = new ProtocolPacket(id, packetClass);
    protocolPacket.handler = handler;
    this.packets.add(protocolPacket);
  }

  /**
   * Gets the packet class by the id.
   *
   * @param id The id of the packet.
   * @return the packet class or null if no packet with the id is found.
   */
  public final @Nullable Class<? extends Packet> getPacket(int id) {
    ProtocolPacket protocolPacket = this.getProtocolPacket(packet -> packet.id == id);
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
    ProtocolPacket protocolPacket = this.getProtocolPacket(packet -> packet.packet == packetClass);
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
    ProtocolPacket protocolPacket = this.getProtocolPacket(packet -> packet.packet == packetClass);
    if (protocolPacket == null) {
      throw new IllegalArgumentException(
          "No packet with the id " + packetClass.getSimpleName() + " "
              + "in protocol " + this.identifier + "found");
    }

    if (protocolPacket.handler != null) {
      throw new UnsupportedOperationException(
          "Packet " + packetClass.getSimpleName() + " in protocol " + this.identifier
              + " already has a handler");
    }

    protocolPacket.handler = handler;
  }

  /**
   * Calls the handler for the packet.
   *
   * @param packet The packet to handle.
   * @throws IllegalArgumentException If no packet with the id has been registered.
   */
  public void handlePacket(Packet packet) {
    ProtocolPacket protocolPacket = this.getProtocolPacket(
        packetClass -> packetClass.packet == packet.getClass()
    );

    if (protocolPacket == null) {
      throw new IllegalArgumentException(
          "No packet with the id " + packet.getClass().getSimpleName() + " in protocol "
              + this.identifier + "found");
    }

    if (protocolPacket.handler != null) {
      protocolPacket.handler.handle(packet);
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

  private static class ProtocolPacket {

    private final int id;
    private final Class<? extends Packet> packet;
    private PacketHandler handler;

    private ProtocolPacket(int id, Class<? extends Packet> packet) {
      this.id = id;
      this.packet = packet;
    }
  }
}
