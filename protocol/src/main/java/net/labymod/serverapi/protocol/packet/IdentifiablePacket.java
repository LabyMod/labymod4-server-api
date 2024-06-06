package net.labymod.serverapi.protocol.packet;

import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.labymod.serverapi.protocol.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents an identifiable packet. Commonly used if a packet has a corresponding response
 * packet. An uuid is randomly generated upon creation of the packet, which can be used to
 * identify the response to the initial packet (if it was implementing correctly, so if both
 * {@link #read(PayloadReader)} and {@link #write(PayloadWriter)} call the super method).
 */
public abstract class IdentifiablePacket implements Packet {

  private UUID identifier;

  /**
   * Default constructor. Use this when creating an initial packet.
   */
  protected IdentifiablePacket() {
    this.identifier = UUID.randomUUID();
  }

  /**
   * Constructor for supplying the response packet with the initiator packet so that the
   * identifier stays the same.
   *
   * @param initiator the initiator packet of the current packet
   */
  protected IdentifiablePacket(@NotNull IdentifiablePacket initiator) {
    Objects.requireNonNull(initiator, "Initiator packet cannot be null");
    if (initiator == this) {
      throw new IllegalArgumentException(
          "Initiator packet cannot be the same as the current packet"
      );
    }

    this.identifier = initiator.getIdentifier();
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.identifier = reader.readUUID();
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeUUID(this.identifier);
  }

  public UUID getIdentifier() {
    return this.identifier;
  }
}
