/*
 * MIT License
 *
 * Copyright (c) 2024 LabyMedia GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.labymod.serverapi.core.packet.clientbound.game.display;

import net.labymod.serverapi.api.model.component.ServerAPIComponent;
import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.model.display.Subtitle;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SubtitlePacket implements Packet {

  private List<Subtitle> subtitles;

  public SubtitlePacket(List<Subtitle> subtitles) {
    this.subtitles = subtitles;
  }

  public SubtitlePacket(Subtitle... subtitles) {
    this.subtitles = Collections.unmodifiableList(Arrays.asList(subtitles));
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
