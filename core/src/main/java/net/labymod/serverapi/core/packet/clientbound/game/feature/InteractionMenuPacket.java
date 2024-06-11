package net.labymod.serverapi.core.packet.clientbound.game.feature;

import net.labymod.serverapi.core.model.feature.InteractionMenuEntry;
import net.labymod.serverapi.core.model.feature.InteractionMenuEntry.InteractionMenuType;
import net.labymod.serverapi.protocol.model.component.ServerAPIComponent;
import net.labymod.serverapi.protocol.packet.Packet;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.labymod.serverapi.protocol.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class InteractionMenuPacket implements Packet {

  private List<InteractionMenuEntry> entries;

  public InteractionMenuPacket(@NotNull List<InteractionMenuEntry> entries) {
    Objects.requireNonNull(entries, "Entries cannot be null");
    this.entries = entries;
  }

  public InteractionMenuPacket(@NotNull InteractionMenuEntry... entries) {
    Objects.requireNonNull(entries, "Entries cannot be null");
    this.entries = List.of(entries);
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.entries = reader.readList(() -> {
      ServerAPIComponent displayName = reader.readComponent();
      InteractionMenuType type = InteractionMenuType.fromString(reader.readString());
      String value = reader.readString();
      String iconUrl = reader.readOptionalString();
      if (type == null) {
        return null; // type is not known. Skip this entry
      }

      return InteractionMenuEntry.create(displayName, type, value, iconUrl);
    });
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeCollection(this.entries, entry -> {
      writer.writeComponent(entry.displayName());
      writer.writeString(entry.type());
      writer.writeString(entry.getValue());
      writer.writeOptionalString(entry.getIconUrl());
    });
  }

  public @NotNull List<InteractionMenuEntry> getEntries() {
    return this.entries;
  }

  @Override
  public String toString() {
    return "InteractionMenuPacket{" +
        "entries=" + this.entries +
        '}';
  }
}
