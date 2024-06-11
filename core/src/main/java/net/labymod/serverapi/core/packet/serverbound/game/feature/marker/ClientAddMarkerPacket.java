package net.labymod.serverapi.core.packet.serverbound.game.feature.marker;

import net.labymod.serverapi.core.packet.common.game.feature.marker.AbstractMarkerPacket;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.labymod.serverapi.protocol.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ClientAddMarkerPacket extends AbstractMarkerPacket {

  private int version;

  public ClientAddMarkerPacket(
      int version,
      int x,
      int y,
      int z,
      boolean large,
      @NotNull UUID target
  ) {
    super(x, y, z, large, target);
    this.version = version;
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.version = reader.readVarInt();
    super.read(reader);
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeVarInt(this.version);
    super.write(writer);
  }

  public int getVersion() {
    return this.version;
  }

  @Override
  public String toString() {
    return "ClientMarkerPacket{" +
        "version=" + this.version +
        "} " + super.toString();
  }
}
