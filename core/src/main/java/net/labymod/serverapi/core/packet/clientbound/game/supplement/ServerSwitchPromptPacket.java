package net.labymod.serverapi.core.packet.clientbound.game.supplement;

import net.labymod.serverapi.core.model.supplement.ServerSwitchPrompt;
import net.labymod.serverapi.protocol.packet.IdentifiablePacket;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.labymod.serverapi.protocol.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ServerSwitchPromptPacket extends IdentifiablePacket {

  private ServerSwitchPrompt prompt;

  public ServerSwitchPromptPacket(@NotNull ServerSwitchPrompt prompt) {
    Objects.requireNonNull(prompt, "Prompt cannot be null");
    this.prompt = prompt;
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    super.read(reader);
    this.prompt = ServerSwitchPrompt.create(
        reader.readComponent(),
        reader.readString(),
        reader.readBoolean()
    );
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    super.write(writer);
    writer.writeComponent(this.prompt.title());
    writer.writeString(this.prompt.getAddress());
    writer.writeBoolean(this.prompt.isShowPreview());
  }

  public @NotNull ServerSwitchPrompt prompt() {
    return this.prompt;
  }

  @Override
  public String toString() {
    return "ServerSwitchPromptPacket{" +
        "prompt=" + this.prompt +
        "} " + super.toString();
  }
}
