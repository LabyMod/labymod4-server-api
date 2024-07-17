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

package net.labymod.serverapi.core.packet.serverbound.game.moderation;

import net.labymod.serverapi.api.packet.IdentifiablePacket;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.model.moderation.InstalledAddons;
import net.labymod.serverapi.core.packet.clientbound.game.moderation.InstalledAddonsRequestPacket;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InstalledAddonsResponsePacket extends IdentifiablePacket {

  private InstalledAddons installedAddons;

  // Constructor for reading
  protected InstalledAddonsResponsePacket() {
    super(null);
  }

  public InstalledAddonsResponsePacket(
      @NotNull InstalledAddonsRequestPacket initiator,
      @NotNull InstalledAddons installedAddons
  ) {
    super(initiator);
    Objects.requireNonNull(installedAddons, "Installed Addons cannot be null");
    this.installedAddons = installedAddons;
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    super.read(reader);
    this.installedAddons = new InstalledAddons(
        reader.readList(reader::readString),
        reader.readList(reader::readString)
    );
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    super.write(writer);
    writer.writeCollection(this.installedAddons.getEnabled(), writer::writeString);
    writer.writeCollection(this.installedAddons.getDisabled(), writer::writeString);
  }

  public @NotNull InstalledAddons installedAddons() {
    return this.installedAddons;
  }

  @Override
  public String toString() {
    return "InstalledAddonsResponsePacket{" +
        "installedAddons=" + this.installedAddons +
        "} " + super.toString();
  }
}
