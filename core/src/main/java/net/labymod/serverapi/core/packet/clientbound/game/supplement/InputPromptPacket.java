package net.labymod.serverapi.core.packet.clientbound.game.supplement;

import net.labymod.serverapi.protocol.model.component.ServerAPIComponent;
import net.labymod.serverapi.protocol.packet.IdentifiablePacket;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.labymod.serverapi.protocol.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class InputPromptPacket extends IdentifiablePacket {

  private ServerAPIComponent title;
  private ServerAPIComponent placeholder;

  private String defaultValue;
  private int maxLength;

  //todo: maybe also add minlength?

  public InputPromptPacket() {
    // Constructor for reading
  }

  public InputPromptPacket(
      @NotNull ServerAPIComponent title,
      @Nullable ServerAPIComponent placeholder,
      @Nullable String defaultValue,
      int maxLength
  ) {
    Objects.requireNonNull(title, "Title cannot be null");
    this.title = title;
    this.placeholder = placeholder;
    this.defaultValue = defaultValue;
    this.maxLength = maxLength;
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    super.read(reader);
    this.title = reader.readComponent();
    this.placeholder = reader.readOptional(reader::readComponent);
    this.defaultValue = reader.readOptionalString();
    this.maxLength = reader.readVarInt();
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    super.write(writer);
    writer.writeComponent(this.title);
    writer.writeOptional(this.placeholder, writer::writeComponent);
    writer.writeOptionalString(this.defaultValue);
    writer.writeVarInt(this.maxLength);
  }

  public ServerAPIComponent title() {
    return this.title;
  }

  public ServerAPIComponent getPlaceholder() {
    return this.placeholder;
  }

  public String getDefaultValue() {
    return this.defaultValue;
  }

  public int getMaxLength() {
    return this.maxLength;
  }
}
