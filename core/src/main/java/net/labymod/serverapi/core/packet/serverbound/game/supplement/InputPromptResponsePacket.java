package net.labymod.serverapi.core.packet.serverbound.game.supplement;

import net.labymod.serverapi.api.packet.IdentifiablePacket;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.packet.clientbound.game.supplement.InputPromptPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InputPromptResponsePacket extends IdentifiablePacket {

  private String value;

  // Constructor for reading
  protected InputPromptResponsePacket() {
    super(null);
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

  public @NotNull String getValue() {
    return this.value;
  }

  @Override
  public String toString() {
    return "InputPromptResponsePacket{" +
        "value='" + this.value + '\'' +
        "} " + super.toString();
  }
}
