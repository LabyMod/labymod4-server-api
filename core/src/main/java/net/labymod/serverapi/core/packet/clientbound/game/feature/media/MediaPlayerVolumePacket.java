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

package net.labymod.serverapi.core.packet.clientbound.game.feature.media;

import java.util.Objects;
import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

public class MediaPlayerVolumePacket implements Packet {

  private String canvasId;
  private float decibel;
  private boolean relative;

  public MediaPlayerVolumePacket(
      @NotNull String canvasId,
      float decibel,
      boolean relative
  ) {
    this.canvasId = Objects.requireNonNull(canvasId, "Canvas id");
    this.decibel = decibel;
    this.relative = relative;
  }

  public static MediaPlayerVolumePacket absolute(@NotNull String canvasId, float decibel) {
    return new MediaPlayerVolumePacket(canvasId, decibel, false);
  }

  public static MediaPlayerVolumePacket relative(@NotNull String canvasId, float decibel) {
    return new MediaPlayerVolumePacket(canvasId, decibel, true);
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.canvasId = reader.readString();
    this.decibel = reader.readFloat();
    this.relative = reader.readBoolean();
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeString(this.canvasId);
    writer.writeFloat(this.decibel);
    writer.writeBoolean(this.relative);
  }

  public @NotNull String getCanvasId() {
    return this.canvasId;
  }

  public float getDecibel() {
    return this.decibel;
  }

  public boolean isRelative() {
    return this.relative;
  }
}
