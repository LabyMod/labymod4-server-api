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

import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.model.moderation.InstalledAddon;
import net.labymod.serverapi.core.packet.clientbound.game.moderation.InstalledAddonsRequestPacket;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A packet that is sent to the server when an addon is either enabled or disabled. To receive
 * this packet, you first need to send a {@link InstalledAddonsRequestPacket} to the client with
 * all the addons you want to be notified of.
 */
public class AddonStateChangedPacket implements Packet {

  private InstalledAddon addon;

  public AddonStateChangedPacket(@NotNull InstalledAddon addon) {
    Objects.requireNonNull(addon, "Addon cannot be null");
    this.addon = addon;
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.addon = InstalledAddonsResponsePacket.readInstalledAddon(reader);
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    InstalledAddonsResponsePacket.writeInstalledAddon(writer, this.addon);
  }

  public @NotNull InstalledAddon addon() {
    return this.addon;
  }

  @Override
  public String toString() {
    return "AddonStateChangedPacket{" +
        "addon=" + this.addon +
        '}';
  }
}
