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

package net.labymod.serverapi.core.packet.serverbound.game.moderation;

import net.labymod.serverapi.api.packet.IdentifiablePacket;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.model.moderation.InstalledAddon;
import net.labymod.serverapi.core.model.moderation.InstalledAddon.AddonVersion;
import net.labymod.serverapi.core.packet.clientbound.game.moderation.InstalledAddonsRequestPacket;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class InstalledAddonsResponsePacket extends IdentifiablePacket {

  private List<InstalledAddon> installedAddons;

  // Constructor for reading
  protected InstalledAddonsResponsePacket() {
    super(null);
  }

  public InstalledAddonsResponsePacket(
      @NotNull InstalledAddonsRequestPacket initiator,
      @NotNull List<InstalledAddon> installedAddons
  ) {
    super(initiator);
    Objects.requireNonNull(installedAddons, "Installed Addons cannot be null");
    this.installedAddons = installedAddons;
  }

  static InstalledAddon readInstalledAddon(PayloadReader reader) {
    return new InstalledAddon(
        reader.readString(),
        new AddonVersion(reader.readVarInt(), reader.readVarInt(), reader.readVarInt()),
        reader.readBoolean(),
        reader.readBoolean()
    );
  }

  static void writeInstalledAddon(PayloadWriter writer, InstalledAddon installedAddon) {
    writer.writeString(installedAddon.getNamespace());
    writer.writeVarInt(installedAddon.getVersion().getMajor());
    writer.writeVarInt(installedAddon.getVersion().getMinor());
    writer.writeVarInt(installedAddon.getVersion().getPatch());
    writer.writeBoolean(installedAddon.isEnabled());
    writer.writeBoolean(installedAddon.isLocal());
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    super.read(reader);
    this.installedAddons = reader.readList(() -> readInstalledAddon(reader));
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    super.write(writer);
    writer.writeCollection(this.installedAddons, addon -> writeInstalledAddon(writer, addon));
  }

  public @NotNull List<InstalledAddon> getInstalledAddons() {
    return this.installedAddons;
  }

  @Override
  public String toString() {
    return "InstalledAddonsResponsePacket{" +
        "installedAddons=" + this.installedAddons +
        "} " + super.toString();
  }
}
