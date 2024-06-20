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

package net.labymod.serverapi.core.packet.clientbound.game.feature;

import net.labymod.serverapi.api.model.component.ServerAPIComponent;
import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.model.feature.InteractionMenuEntry;
import net.labymod.serverapi.core.model.feature.InteractionMenuEntry.InteractionMenuType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
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
    this.entries = Collections.unmodifiableList(Arrays.asList(entries));
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
