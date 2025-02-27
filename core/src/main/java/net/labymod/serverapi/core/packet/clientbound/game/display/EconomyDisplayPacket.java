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

package net.labymod.serverapi.core.packet.clientbound.game.display;

import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.model.display.EconomyDisplay;
import org.jetbrains.annotations.NotNull;

public class EconomyDisplayPacket implements Packet {

  private EconomyDisplay economy;

  public EconomyDisplayPacket(EconomyDisplay economy) {
    this.economy = economy;
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    String key = reader.readString();
    boolean visible = reader.readBoolean();
    double balance = reader.readDouble();
    String iconUrl = reader.readOptionalString();
    EconomyDisplay.DecimalFormat decimalFormat = reader.readOptional(() -> {
      String format = reader.readOptionalString();
      double divisor = reader.readDouble();
      return new EconomyDisplay.DecimalFormat(format, divisor);
    });

    this.economy = new EconomyDisplay(key, visible, balance, iconUrl, decimalFormat);
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeString(this.economy.getKey());
    writer.writeBoolean(this.economy.isVisible());
    writer.writeDouble(this.economy.getBalance());
    writer.writeOptionalString(this.economy.getIconUrl());
    writer.writeOptional(this.economy.getDecimalFormat(), decimalFormat -> {
      writer.writeOptionalString(decimalFormat.getFormat());
      writer.writeDouble(decimalFormat.getDivisor());
    });
  }

  public @NotNull EconomyDisplay economy() {
    return this.economy;
  }

  @Override
  public String toString() {
    return "EconomyDisplayPacket{" +
        "economy=" + this.economy +
        '}';
  }
}
