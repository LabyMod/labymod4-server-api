package net.labymod.serverapi.core.packet.serverbound.game.supplement;

import net.labymod.serverapi.core.packet.clientbound.game.supplement.ServerSwitchPacket;
import net.labymod.serverapi.protocol.packet.IdentifiablePacket;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.labymod.serverapi.protocol.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

public class ServerSwitchResponsePacket extends IdentifiablePacket {

  private String address;
  private boolean accepted;

  public ServerSwitchResponsePacket() {
    // Constructor for reading
  }

  public ServerSwitchResponsePacket(@NotNull ServerSwitchPacket initiator, boolean accepted) {
    super(initiator);
    this.address = initiator.getAddress();
    this.accepted = accepted;
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    super.read(reader);
    this.address = reader.readString();
    this.accepted = reader.readBoolean();
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    super.write(writer);
    writer.writeString(this.address);
    writer.writeBoolean(this.accepted);
  }

  public String getAddress() {
    return this.address;
  }

  public boolean wasAccepted() {
    return this.accepted;
  }
}
