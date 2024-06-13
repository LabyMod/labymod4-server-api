package net.labymod.serverapi.api.packet;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

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
   * @param sender The sender of the packet.
   * @param packet The packet to be handled.
   */
  void handle(@NotNull UUID sender, @NotNull T packet);
}
