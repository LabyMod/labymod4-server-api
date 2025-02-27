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

package net.labymod.serverapi.core.packet.clientbound.game.moderation;

import net.labymod.serverapi.api.packet.IdentifiablePacket;
import net.labymod.serverapi.api.packet.OnlyWriteConstructor;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class InstalledAddonsRequestPacket extends IdentifiablePacket {

  private List<String> addonsToRequest;

  /**
   * Creates a packet that requests the installed & enabled state of the provided addons.
   *
   * @param addonsToRequest the addons to request
   */
  public InstalledAddonsRequestPacket(@NotNull List<String> addonsToRequest) {
    Objects.requireNonNull(addonsToRequest, "Addons cannot be null");
    this.addonsToRequest = addonsToRequest;
  }

  /**
   * Creates a packet that requests the installed & enabled state of the provided addons.
   *
   * @param addonsToRequest the addons to request
   */
  public InstalledAddonsRequestPacket(@NotNull String... addonsToRequest) {
    this(Collections.unmodifiableList(Arrays.asList(addonsToRequest)));
  }

  /**
   * Creates a packet that requests the installed & enabled state of ALL addons.
   */
  @OnlyWriteConstructor
  public InstalledAddonsRequestPacket() {
    this(Collections.emptyList());
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    super.read(reader);
    this.addonsToRequest = reader.readList(reader::readString);
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    super.write(writer);
    writer.writeCollection(this.addonsToRequest, writer::writeString);
  }

  public @NotNull List<String> getAddonsToRequest() {
    return this.addonsToRequest;
  }

  public boolean requestsAllAddons() {
    return this.addonsToRequest.isEmpty();
  }

  @Override
  public String toString() {
    return "EnabledAddonsRequestPacket{" +
        "addonsToRequest=" + this.addonsToRequest +
        "} " + super.toString();
  }
}
