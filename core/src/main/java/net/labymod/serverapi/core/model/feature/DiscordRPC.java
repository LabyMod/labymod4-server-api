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

package net.labymod.serverapi.core.model.feature;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class DiscordRPC {

  private static final DiscordRPC RESET = new DiscordRPC(null, 0, 0);

  private final String gameMode;
  private final long startTime;
  private final long endTime;

  protected DiscordRPC(@Nullable String gameMode, long startTime, long endTime) {
    if (startTime < 0) {
      throw new IllegalArgumentException("startTime cannot be negative");
    }
    if (endTime < 0) {
      throw new IllegalArgumentException("endTime cannot be negative");
    }

    this.gameMode = gameMode;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public static DiscordRPC createReset() {
    return RESET;
  }

  public static DiscordRPC create(@NotNull String gameMode) {
    Objects.requireNonNull(gameMode, "GameMode cannot be null");
    return new DiscordRPC(gameMode, 0, 0);
  }

  public static DiscordRPC createWithStart(@NotNull String gameMode, long startTime) {
    Objects.requireNonNull(gameMode, "GameMode cannot be null");
    return new DiscordRPC(gameMode, startTime, 0);
  }

  public static DiscordRPC createWithEnd(@NotNull String gameMode, long endTime) {
    Objects.requireNonNull(gameMode, "GameMode cannot be null");
    return new DiscordRPC(gameMode, 0, endTime);
  }

  public @Nullable String getGameMode() {
    return this.gameMode;
  }

  public long getStartTime() {
    return this.startTime;
  }

  public long getEndTime() {
    return this.endTime;
  }

  public boolean hasGameMode() {
    return this.gameMode != null;
  }

  public boolean hasStartTime() {
    return this.startTime != 0;
  }

  public boolean hasEndTime() {
    return this.endTime != 0;
  }

  @Override
  public String toString() {
    return "DiscordRPC{" +
        "gameMode='" + this.gameMode + '\'' +
        ", startTime=" + this.startTime +
        ", endTime=" + this.endTime +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof DiscordRPC)) {
      return false;
    }

    DiscordRPC that = (DiscordRPC) o;
    return this.startTime == that.startTime && this.endTime == that.endTime && Objects.equals(
        this.gameMode, that.gameMode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.gameMode, this.startTime, this.endTime);
  }
}
