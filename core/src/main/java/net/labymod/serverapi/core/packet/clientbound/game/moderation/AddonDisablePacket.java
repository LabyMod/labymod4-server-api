package net.labymod.serverapi.core.packet.clientbound.game.moderation;

import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class AddonDisablePacket implements Packet {

  private List<String> addonsToDisable;

  public AddonDisablePacket(@NotNull List<String> addonsToDisable) {
    Objects.requireNonNull(addonsToDisable, "Addons to disable cannot be null");
    this.addonsToDisable = addonsToDisable;
  }

  public AddonDisablePacket(@NotNull String... addonsToDisable) {
    this(List.of(addonsToDisable));
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.addonsToDisable = reader.readList(reader::readString);
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeCollection(this.addonsToDisable, writer::writeString);
  }

  public @NotNull List<String> getAddonsToDisable() {
    return this.addonsToDisable;
  }

  @Override
  public String toString() {
    return "AddonDisablePacket{" +
        "addonsToDisable=" + this.addonsToDisable +
        '}';
  }
}
