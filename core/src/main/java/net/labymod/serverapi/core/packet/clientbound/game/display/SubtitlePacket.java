package net.labymod.serverapi.core.packet.clientbound.game.display;

import net.labymod.serverapi.api.model.component.ServerAPIComponent;
import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.model.display.Subtitle;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class SubtitlePacket implements Packet {

  private List<Subtitle> subtitles;

  public SubtitlePacket(List<Subtitle> subtitles) {
    this.subtitles = subtitles;
  }

  public SubtitlePacket(Subtitle... subtitles) {
    this.subtitles = List.of(subtitles);
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.subtitles = reader.readList(() -> {
      UUID uniqueId = reader.readUUID();
      boolean hasText = reader.readBoolean();
      if (!hasText) {
        return Subtitle.create(uniqueId, null);
      }

      return Subtitle.create(uniqueId, reader.readComponent(), reader.readDouble());
    });
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeCollection(this.subtitles, (subtitle) -> {
      writer.writeUUID(subtitle.getUniqueId());
      ServerAPIComponent text = subtitle.getText();
      writer.writeBoolean(text != null);

      if (text != null) {
        writer.writeComponent(text);
        writer.writeDouble(subtitle.getSize());
      }
    });
  }

  public @NotNull List<Subtitle> getSubtitles() {
    return this.subtitles;
  }

  @Override
  public String toString() {
    return "SubtitlePacket{" +
        "subtitles=" + this.subtitles +
        '}';
  }
}
