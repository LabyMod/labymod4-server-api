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

package net.labymod.serverapi.core.model.feature.texteffect;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a parameter definition for a text effect.
 */
public class TextEffectParameter {

  private final String name;
  private final String displayName;
  private final TextEffectParameterType type;
  private final Object defaultValue;
  private final Float min;
  private final Float max;
  private final Float step;
  private final String uniform;

  private TextEffectParameter(
      @NotNull String name,
      @Nullable String displayName,
      @NotNull TextEffectParameterType type,
      @NotNull Object defaultValue,
      @Nullable Float min,
      @Nullable Float max,
      @Nullable Float step,
      @NotNull String uniform
  ) {
    Objects.requireNonNull(name, "Name cannot be null");
    Objects.requireNonNull(type, "Type cannot be null");
    Objects.requireNonNull(defaultValue, "Default value cannot be null");
    Objects.requireNonNull(uniform, "Uniform cannot be null");
    this.name = name;
    this.displayName = displayName;
    this.type = type;
    this.defaultValue = defaultValue;
    this.min = min;
    this.max = max;
    this.step = step;
    this.uniform = uniform;
  }

  public static Builder builder(@NotNull String name) {
    return new Builder(name);
  }

  public static TextEffectParameter create(
      @NotNull String name,
      @Nullable String displayName,
      @NotNull TextEffectParameterType type,
      @NotNull Object defaultValue,
      @Nullable Float min,
      @Nullable Float max,
      @Nullable Float step,
      @NotNull String uniform
  ) {
    return new TextEffectParameter(name, displayName, type, defaultValue, min, max, step, uniform);
  }

  public @NotNull String getName() {
    return this.name;
  }

  public @Nullable String getDisplayName() {
    return this.displayName;
  }

  public @NotNull TextEffectParameterType getType() {
    return this.type;
  }

  public @NotNull Object getDefaultValue() {
    return this.defaultValue;
  }

  public @Nullable Float getMin() {
    return this.min;
  }

  public @Nullable Float getMax() {
    return this.max;
  }

  public @Nullable Float getStep() {
    return this.step;
  }

  public @NotNull String getUniform() {
    return this.uniform;
  }

  @Override
  public String toString() {
    return "TextEffectParameter{" +
        "name='" + this.name + '\'' +
        ", displayName='" + this.displayName + '\'' +
        ", type=" + this.type +
        ", defaultValue=" + this.defaultValue +
        ", min=" + this.min +
        ", max=" + this.max +
        ", step=" + this.step +
        ", uniform='" + this.uniform + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TextEffectParameter)) {
      return false;
    }
    TextEffectParameter that = (TextEffectParameter) o;
    return Objects.equals(this.name, that.name)
        && Objects.equals(this.displayName, that.displayName)
        && this.type == that.type
        && Objects.equals(this.defaultValue, that.defaultValue)
        && Objects.equals(this.min, that.min)
        && Objects.equals(this.max, that.max)
        && Objects.equals(this.step, that.step)
        && Objects.equals(this.uniform, that.uniform);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        this.name, this.displayName, this.type, this.defaultValue,
        this.min, this.max, this.step, this.uniform
    );
  }

  public static class Builder {

    private final String name;
    private String displayName;
    private TextEffectParameterType type;
    private Object defaultValue;
    private Float min;
    private Float max;
    private Float step;
    private String uniform;

    private Builder(@NotNull String name) {
      this.name = name;
    }

    public Builder displayName(@Nullable String displayName) {
      this.displayName = displayName;
      return this;
    }

    public Builder type(@NotNull TextEffectParameterType type) {
      this.type = type;
      return this;
    }

    public Builder defaultValue(@NotNull Object defaultValue) {
      this.defaultValue = defaultValue;
      return this;
    }

    public Builder min(float min) {
      this.min = min;
      return this;
    }

    public Builder max(float max) {
      this.max = max;
      return this;
    }

    public Builder step(float step) {
      this.step = step;
      return this;
    }

    public Builder uniform(@NotNull String uniform) {
      this.uniform = uniform;
      return this;
    }

    public @NotNull TextEffectParameter build() {
      Objects.requireNonNull(this.type, "Type must be set");
      Objects.requireNonNull(this.defaultValue, "Default value must be set");
      Objects.requireNonNull(this.uniform, "Uniform must be set");
      return new TextEffectParameter(
          this.name, this.displayName, this.type, this.defaultValue,
          this.min, this.max, this.step, this.uniform
      );
    }
  }
}
