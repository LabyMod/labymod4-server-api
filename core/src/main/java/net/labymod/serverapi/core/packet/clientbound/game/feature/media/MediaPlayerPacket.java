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
import net.labymod.serverapi.core.model.feature.media.MediaPlayerAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MediaPlayerPacket implements Packet {

  private MediaPlayerAction action;
  private String canvasId;
  private String url;
  private long positionMillis;

  public MediaPlayerPacket(
      @NotNull MediaPlayerAction action,
      @NotNull String canvasId
  ) {
    this(action, canvasId, null, -1L);
  }

  public MediaPlayerPacket(
      @NotNull MediaPlayerAction action,
      @NotNull String canvasId,
      @Nullable String url,
      long positionMillis
  ) {
    this.action = Objects.requireNonNull(action, "Action");
    this.canvasId = Objects.requireNonNull(canvasId, "Canvas id");
    this.url = url;
    this.positionMillis = positionMillis;
  }

  public static MediaPlayerPacket register(@NotNull String canvasId) {
    return new MediaPlayerPacket(MediaPlayerAction.REGISTER, canvasId);
  }

  public static MediaPlayerPacket unregister(@NotNull String canvasId) {
    return new MediaPlayerPacket(MediaPlayerAction.UNREGISTER, canvasId);
  }

  public static MediaPlayerPacket play(@NotNull String canvasId, @NotNull String url) {
    return new MediaPlayerPacket(MediaPlayerAction.PLAY, canvasId, url, -1L);
  }

  public static MediaPlayerPacket pause(@NotNull String canvasId) {
    return new MediaPlayerPacket(MediaPlayerAction.PAUSE, canvasId);
  }

  public static MediaPlayerPacket resume(@NotNull String canvasId) {
    return new MediaPlayerPacket(MediaPlayerAction.RESUME, canvasId);
  }

  public static MediaPlayerPacket seek(@NotNull String canvasId, long positionMillis) {
    return new MediaPlayerPacket(MediaPlayerAction.SEEK, canvasId, null, positionMillis);
  }

  public static MediaPlayerPacket stop(@NotNull String canvasId) {
    return new MediaPlayerPacket(MediaPlayerAction.STOP, canvasId);
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.action = MediaPlayerAction.values()[reader.readVarInt()];
    this.canvasId = reader.readString();
    this.url = reader.readOptional(reader::readString);
    this.positionMillis = reader.readLong();
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeVarInt(this.action.ordinal());
    writer.writeString(this.canvasId);
    writer.writeOptionalString(this.url);
    writer.writeLong(this.positionMillis);
  }

  public @NotNull MediaPlayerAction getAction() {
    return this.action;
  }

  public @NotNull String getCanvasId() {
    return this.canvasId;
  }

  public @Nullable String getUrl() {
    return this.url;
  }

  public long getPositionMillis() {
    return this.positionMillis;
  }
}
