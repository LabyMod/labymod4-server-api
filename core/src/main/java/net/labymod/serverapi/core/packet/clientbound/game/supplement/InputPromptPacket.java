package net.labymod.serverapi.core.packet.clientbound.game.supplement;

import net.labymod.serverapi.api.packet.IdentifiablePacket;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.model.supplement.InputPrompt;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InputPromptPacket extends IdentifiablePacket {

  private InputPrompt prompt;

  public InputPromptPacket(@NotNull InputPrompt prompt) {
    Objects.requireNonNull(prompt, "Prompt cannot be null");
    this.prompt = prompt;
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    super.read(reader);
    this.prompt = InputPrompt.create(
        reader.readComponent(),
        reader.readOptional(reader::readComponent),
        reader.readOptionalString(),
        reader.readVarInt()
    );
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    super.write(writer);
    writer.writeComponent(this.prompt.title());
    writer.writeOptional(this.prompt.getPlaceholder(), writer::writeComponent);
    writer.writeOptionalString(this.prompt.getDefaultValue());
    writer.writeVarInt(this.prompt.getMaxLength());
  }

  public @NotNull InputPrompt prompt() {
    return this.prompt;
  }

  @Override
  public String toString() {
    return "InputPromptPacket{" +
        "prompt=" + this.prompt +
        "} " + super.toString();
  }
}
