package net.labymod.serverapi.core.packet.serverbound.login;

import net.labymod.serverapi.protocol.packet.Packet;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.labymod.serverapi.protocol.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a packet that is sent from the client to the server with the version of the client.
 */
public class VersionLoginPacket implements Packet {

  private String version;

  public VersionLoginPacket() {
    // Empty constructor
  }

  public VersionLoginPacket(String version) {
    this.version = version;
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeString(this.version);
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.version = reader.readString();
  }

  /**
   * @return the version of the client
   */
  public String getVersion() {
    return this.version;
  }
}
