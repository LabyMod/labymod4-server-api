/*
 * MIT License
 *
 * Copyright (c) 2025 LabyMedia GmbH
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

package net.labymod.serverapi.core.packet.clientbound.game.feature.texteffect;

import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.model.feature.texteffect.TextEffectUpdate;
import net.labymod.serverapi.core.model.feature.texteffect.TextEffectUpdateType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Packet to dynamically update effect parameters or rule state.
 */
public class TextEffectUpdatePacket implements Packet {

  private List<TextEffectUpdate> updates;

  public TextEffectUpdatePacket(@NotNull List<TextEffectUpdate> updates) {
    Objects.requireNonNull(updates, "Updates cannot be null");
    this.updates = updates;
  }

  public TextEffectUpdatePacket(@NotNull TextEffectUpdate... updates) {
    Objects.requireNonNull(updates, "Updates cannot be null");
    this.updates = Collections.unmodifiableList(Arrays.asList(updates));
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.updates = reader.readList(() -> {
      TextEffectUpdateType type = TextEffectUpdateType.fromIdentifier(reader.readString());
      if (type == null) {
        return null;
      }

      String target = reader.readString();
      Boolean enabled = reader.readOptional(reader::readBoolean);
      Map<String, Object> parameters = TextEffectRulesPacket.readParameters(reader);

      TextEffectUpdate.Builder builder = TextEffectUpdate.builder(type)
          .target(target)
          .parameters(parameters);

      if (enabled != null) {
        builder.enabled(enabled);
      }

      return builder.build();
    });
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeCollection(this.updates, update -> {
      writer.writeString(update.getType().getIdentifier());
      writer.writeString(update.getTarget());
      writer.writeOptional(update.getEnabled(), writer::writeBoolean);
      TextEffectRulesPacket.writeParameters(writer, update.getParameters());
    });
  }

  public @NotNull List<TextEffectUpdate> getUpdates() {
    return this.updates;
  }

  @Override
  public String toString() {
    return "TextEffectUpdatePacket{" +
        "updates=" + this.updates +
        '}';
  }
}
