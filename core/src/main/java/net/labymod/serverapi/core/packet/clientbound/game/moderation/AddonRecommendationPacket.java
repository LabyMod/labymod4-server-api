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

import net.labymod.serverapi.api.packet.IdentifiablePacket;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.model.moderation.RecommendedAddon;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AddonRecommendationPacket extends IdentifiablePacket {

  private List<RecommendedAddon> addons;

  public AddonRecommendationPacket(@NotNull List<RecommendedAddon> addons) {
    Objects.requireNonNull(addons, "Addons cannot be null");
    this.addons = addons;
  }

  public AddonRecommendationPacket(@NotNull RecommendedAddon... addons) {
    Objects.requireNonNull(addons, "Addons cannot be null");
    this.addons = Collections.unmodifiableList(Arrays.asList(addons));
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    super.read(reader);
    this.addons = reader.readList(
        () -> RecommendedAddon.of(reader.readString(), reader.readBoolean())
    );
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    super.write(writer);
    writer.writeCollection(this.addons, addon -> {
      writer.writeString(addon.getNamespace());
      writer.writeBoolean(addon.isRequired());
    });
  }

  public @NotNull List<RecommendedAddon> getAddons() {
    return this.addons;
  }

  @Override
  public String toString() {
    return "AddonRecommendationPacket{" +
        "addons=" + this.addons +
        "} " + super.toString();
  }
}
