package net.labymod.serverapi.core.packet.clientbound.game.feature.marker;

import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

public class MarkerPacket implements Packet {

  private MarkerSendType type;

  /**
   * Creates a new marker packet.
   *
   * @param type the way the client will send markers. See {@link MarkerSendType}
   */
  public MarkerPacket(MarkerSendType type) {
    this.type = type;
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    String rawType = reader.readString();
    this.type = MarkerSendType.fromString(rawType);
    if (this.type == null) {
      throw new IllegalArgumentException("Invalid marker send type " + rawType);
    }
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeString(this.type);
  }

  /**
   * @return the way the client will send markers. See {@link MarkerSendType}
   */
  public @NotNull MarkerSendType type() {
    return this.type;
  }

  @Override
  public String toString() {
    return "MarkerPacket{" +
        "type=" + this.type +
        '}';
  }

  public enum MarkerSendType {
    /**
     * The client will send markers to the server which can redirect them to other clients.
     */
    SERVER,
    /**
     * The client will send markers to <strong>friends</strong> via the LabyConnect server. This
     * is the default value.
     */
    LABY_CONNECT,
    ;

    private static final MarkerSendType[] VALUES = values();

    public static MarkerSendType fromString(String name) {
      for (MarkerSendType value : VALUES) {
        if (value.name().equalsIgnoreCase(name)) {
          return value;
        }
      }

      return null;
    }
  }
}
