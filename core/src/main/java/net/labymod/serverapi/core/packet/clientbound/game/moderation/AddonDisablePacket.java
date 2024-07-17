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

package net.labymod.serverapi.core.packet.clientbound.game.moderation;

import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AddonDisablePacket implements Packet {

  private List<String> addonsToDisable;

  public AddonDisablePacket(@NotNull List<String> addonsToDisable) {
    Objects.requireNonNull(addonsToDisable, "Addons to disable cannot be null");
    this.addonsToDisable = addonsToDisable;
  }

  public AddonDisablePacket(@NotNull String... addonsToDisable) {
    this(Collections.unmodifiableList(Arrays.asList(addonsToDisable)));
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
