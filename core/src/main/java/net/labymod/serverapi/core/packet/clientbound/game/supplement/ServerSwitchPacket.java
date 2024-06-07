package net.labymod.serverapi.core.packet.clientbound.game.supplement;

import net.labymod.serverapi.protocol.model.component.ServerAPIComponent;
import net.labymod.serverapi.protocol.packet.IdentifiablePacket;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.labymod.serverapi.protocol.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ServerSwitchPacket extends IdentifiablePacket {

  private ServerAPIComponent title;
  private String address;
  private boolean showPreview;

  public ServerSwitchPacket(
      @NotNull ServerAPIComponent title,
      @NotNull String address,
      boolean showPreview
  ) {
    Objects.requireNonNull(title, "Title cannot be null");
    Objects.requireNonNull(address, "Address cannot be null");
    this.title = title;
    this.address = address;
    this.showPreview = showPreview;
  }

  public ServerSwitchPacket(
      @NotNull ServerAPIComponent title,
      @NotNull String address
  ) {
    this(title, address, false);
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    super.read(reader);
    this.title = reader.readComponent();
    this.address = reader.readString();
    this.showPreview = reader.readBoolean();
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    super.write(writer);
    writer.writeComponent(this.title);
    writer.writeString(this.address);
    writer.writeBoolean(this.showPreview);
  }

  public @NotNull ServerAPIComponent title() {
    return this.title;
  }

  public @NotNull String getAddress() {
    return this.address;
  }

  public boolean isShowPreview() {
    return this.showPreview;
  }

  @Override
  public String toString() {
    return "ServerSwitchPacket{" +
        "title=" + this.title +
        ", address='" + this.address + '\'' +
        ", showPreview=" + this.showPreview +
        "} " + super.toString();
  }
}
