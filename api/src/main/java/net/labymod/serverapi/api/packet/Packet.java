package net.labymod.serverapi.api.packet;

import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

public interface Packet {

  /**
   * Reads the packet data from the given {@link PayloadReader}.
   *
   * @param reader The reader to read the packet data from.
   * @throws UnsupportedOperationException If the packet does not support reading.
   */
  default void read(@NotNull PayloadReader reader) {
    throw new UnsupportedOperationException("Packet does not support reading.");
  }

  /**
   * Writes the packet data to the given {@link PayloadWriter}.
   *
   * @param writer The writer to write the packet data to.
   * @throws UnsupportedOperationException If the packet does not support writing.
   */
  default void write(@NotNull PayloadWriter writer) {
    throw new UnsupportedOperationException("Packet does not support writing.");
  }
}
