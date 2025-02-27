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

package net.labymod.serverapi.core.model.display;

import net.labymod.serverapi.api.model.component.ServerAPIComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class Subtitle {

  private final UUID uniqueId;
  private ServerAPIComponent text;
  private double size;

  private Subtitle(@NotNull UUID uniqueId, @Nullable ServerAPIComponent text, double size) {
    Objects.requireNonNull(uniqueId, "UniqueId is null");
    this.uniqueId = uniqueId;
    this.text = text;
    this.size(size);
  }

  private Subtitle(@NotNull UUID uniqueId, @Nullable ServerAPIComponent text) {
    this(uniqueId, text, 1.0);
  }

  public static Subtitle create(@NotNull UUID uniqueId, @Nullable ServerAPIComponent text) {
    return new Subtitle(uniqueId, text);
  }

  public static Subtitle create(
      @NotNull UUID uniqueId,
      @Nullable ServerAPIComponent text,
      double size
  ) {
    return new Subtitle(uniqueId, text, size);
  }

  public @NotNull UUID getUniqueId() {
    return this.uniqueId;
  }

  public @Nullable ServerAPIComponent getText() {
    return this.text;
  }

  public double getSize() {
    return this.size;
  }

  public Subtitle size(double size) {
    if (size < 0.25) {
      size = 0.25;
    }

    if (size > 1.0) {
      size = 1.0;
    }

    this.size = size;
    return this;
  }

  public Subtitle text(@Nullable ServerAPIComponent text) {
    this.text = text;
    return this;
  }

  /**
   * @deprecated Use {@link #size(double)} instead.
   */
  @Deprecated
  private void setSize(double size) {
    this.size(size);
  }

  @Override
  public String toString() {
    return "Subtitle{" +
        "uniqueId=" + this.uniqueId +
        ", text=" + this.text +
        ", size=" + this.size +
        '}';
  }
}
