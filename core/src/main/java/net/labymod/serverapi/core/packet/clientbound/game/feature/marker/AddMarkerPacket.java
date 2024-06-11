package net.labymod.serverapi.core.packet.clientbound.game.feature.marker;

import net.labymod.serverapi.core.packet.common.game.feature.marker.AbstractMarkerPacket;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.labymod.serverapi.protocol.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class AddMarkerPacket extends AbstractMarkerPacket {

  private UUID sender;

  public AddMarkerPacket(
      @NotNull UUID sender,
      int x,
      int y,
      int z,
      boolean large,
      @Nullable UUID target
  ) {
    super(x, y, z, large, target);
    Objects.requireNonNull(sender, "Sender entity is null");
    this.sender = sender;
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.sender = reader.readUUID();
    super.read(reader);
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeUUID(this.sender);
    super.write(writer);
  }

  public @NotNull UUID getSender() {
    return this.sender;
  }

  @Override
  public String toString() {
    return "AddMarkerPacket{" +
        "sender=" + this.sender +
        "} " + super.toString();
  }
}
