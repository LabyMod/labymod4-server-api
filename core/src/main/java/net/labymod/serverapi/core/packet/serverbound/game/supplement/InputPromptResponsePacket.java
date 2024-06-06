package net.labymod.serverapi.core.packet.serverbound.game.supplement;

import net.labymod.serverapi.core.packet.clientbound.game.supplement.InputPromptPacket;
import net.labymod.serverapi.protocol.packet.IdentifiablePacket;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.labymod.serverapi.protocol.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InputPromptResponsePacket extends IdentifiablePacket {

  private String value;

  public InputPromptResponsePacket() {
    // Constructor for reading
  }

  public InputPromptResponsePacket(@NotNull InputPromptPacket initiator, @Nullable String value) {
    super(initiator);
    this.value = value;
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    super.read(reader);
    this.value = reader.readOptionalString();
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    super.write(writer);
    writer.writeOptionalString(this.value);
  }

  public String getValue() {
    return this.value;
  }
}
