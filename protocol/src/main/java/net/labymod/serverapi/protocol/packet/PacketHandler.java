package net.labymod.serverapi.protocol.packet;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a handler for a packet.
 *
 * @param <T> The packet to be handled.
 */
@FunctionalInterface
public interface PacketHandler<T extends Packet> {

  /**
   * The function is called when a packet is to be handled.
   *
   * @param packet The packet to be handled.
   */
  void handle(@NotNull T packet);
}
