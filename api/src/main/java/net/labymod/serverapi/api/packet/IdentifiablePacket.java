package net.labymod.serverapi.api.packet;

import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
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

  @Override
  public String toString() {
    return "IdentifiablePacket{" +
        "identifier=" + this.identifier +
        '}';
  }
}
