package net.labymod.serverapi.core.packet.serverbound.game.supplement;

import net.labymod.serverapi.api.packet.IdentifiablePacket;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.packet.clientbound.game.supplement.ServerSwitchPromptPacket;
import org.jetbrains.annotations.NotNull;

public class ServerSwitchPromptResponsePacket extends IdentifiablePacket {

  private String address;
  private boolean accepted;

  // Constructor for reading
  protected ServerSwitchPromptResponsePacket() {
    super(null);
  }

  public ServerSwitchPromptResponsePacket(
      @NotNull ServerSwitchPromptPacket initiator,
      boolean accepted
  ) {
    super(initiator);
    this.address = initiator.prompt().getAddress();
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

  public @NotNull String getAddress() {
    return this.address;
  }

  public boolean wasAccepted() {
    return this.accepted;
  }

  @Override
  public String toString() {
    return "ServerSwitchResponsePacket{" +
        "address='" + this.address + '\'' +
        ", accepted=" + this.accepted +
        "} " + super.toString();
  }
}
