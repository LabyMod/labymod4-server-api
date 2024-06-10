package net.labymod.serverapi.core.packet.clientbound.game.feature;

import net.labymod.serverapi.core.model.feature.Emote;
import net.labymod.serverapi.protocol.packet.Packet;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.labymod.serverapi.protocol.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class EmotePacket implements Packet {

  private List<Emote> emotes;

  public EmotePacket(@NotNull List<Emote> emotes) {
    Objects.requireNonNull(emotes, "Emotes cannot be null");
    this.emotes = emotes;
  }

  public EmotePacket(@NotNull Emote... emotes) {
    Objects.requireNonNull(emotes, "Emotes cannot be null");
    this.emotes = List.of(emotes);
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.emotes = reader.readList(() -> Emote.create(reader.readUUID(), reader.readVarInt()));
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeCollection(this.emotes, emote -> {
      writer.writeUUID(emote.getUniqueId());
      writer.writeVarInt(emote.getEmoteId());
    });
  }

  public @NotNull List<Emote> getEmotes() {
    return this.emotes;
  }

  @Override
  public String toString() {
    return "EmotePacket{" +
        "emotes=" + this.emotes +
        '}';
  }
}
