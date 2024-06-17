package net.labymod.serverapi.core.packet.clientbound.game.feature;

import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.model.feature.Emote;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
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
    this.emotes = Collections.unmodifiableList(Arrays.asList(emotes));
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
