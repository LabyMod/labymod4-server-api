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

package net.labymod.serverapi.core.packet.clientbound.game.feature;

import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.model.feature.Feature;
import net.labymod.serverapi.core.model.feature.Feature.StatedFeature;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UpdateFeaturePacket implements Packet {

  private List<StatedFeature> features;

  public UpdateFeaturePacket(@NotNull List<StatedFeature> features) {
    Objects.requireNonNull(features, "Features");
    this.features = features;
  }

  public UpdateFeaturePacket(@NotNull StatedFeature... features) {
    this(Collections.unmodifiableList(Arrays.asList(features)));
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.features = reader.readList(() -> {
      Feature feature = Feature.of(reader.readString());
      boolean enabled = reader.readBoolean();
      return enabled ? feature.enable() : feature.disable();
    });
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeCollection(this.features, statedFeature -> {
      writer.writeString(statedFeature.feature().getIdentifier());
      writer.writeBoolean(statedFeature.isEnabled());
    });
  }

  public @NotNull List<StatedFeature> getFeatures() {
    return this.features;
  }
}
