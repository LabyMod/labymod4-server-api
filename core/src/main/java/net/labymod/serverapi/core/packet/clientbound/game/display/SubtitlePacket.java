package net.labymod.serverapi.core.packet.clientbound.game.display;

import net.labymod.serverapi.core.model.display.Subtitle;
import net.labymod.serverapi.protocol.packet.Packet;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.labymod.serverapi.protocol.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SubtitlePacket implements Packet {

  private List<Subtitle> subtitles;

  public SubtitlePacket() {
    // Constructor for reading
  }

  public SubtitlePacket(List<Subtitle> subtitles) {
    this.subtitles = subtitles;
  }

  public SubtitlePacket(Subtitle... subtitles) {
    this.subtitles = List.of(subtitles);
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.subtitles = reader.readList(
        () -> Subtitle.create(reader.readUUID(), reader.readComponent(), reader.readDouble())
    );
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeCollection(this.subtitles, (subtitle) -> {
      writer.writeUUID(subtitle.getUniqueId());
      writer.writeComponent(subtitle.text());
      writer.writeDouble(subtitle.getSize());
    });
  }

  public List<Subtitle> getSubtitles() {
    return this.subtitles;
  }
}
