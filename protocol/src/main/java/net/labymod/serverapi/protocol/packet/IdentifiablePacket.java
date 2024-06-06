package net.labymod.serverapi.protocol.packet;

import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.labymod.serverapi.protocol.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class IdentifiablePacket implements Packet {

  private UUID identifier;

  protected IdentifiablePacket() {
    this.identifier = UUID.randomUUID();
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
