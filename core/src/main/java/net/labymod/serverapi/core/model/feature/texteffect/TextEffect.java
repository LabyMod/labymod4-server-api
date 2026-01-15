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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a custom text effect definition that can be registered with the client.
 */
public class TextEffect {

  private final String id;
  private final Integer effectId;
  private final String displayName;
  private final List<TextEffectParameter> parameters;
  private final TextEffectShader fragmentShader;
  private final TextEffectShader vertexShader;

  private TextEffect(
      @NotNull String id,
      @Nullable Integer effectId,
      @NotNull String displayName,
      @NotNull List<TextEffectParameter> parameters,
      @Nullable TextEffectShader fragmentShader,
      @Nullable TextEffectShader vertexShader
  ) {
    Objects.requireNonNull(id, "Id cannot be null");
    Objects.requireNonNull(displayName, "Display name cannot be null");
    Objects.requireNonNull(parameters, "Parameters cannot be null");
    this.id = id;
    this.effectId = effectId;
    this.displayName = displayName;
    this.parameters = Collections.unmodifiableList(parameters);
    this.fragmentShader = fragmentShader;
    this.vertexShader = vertexShader;
  }

  public static Builder builder(@NotNull String id) {
    return new Builder(id);
  }

  public @NotNull String getId() {
    return this.id;
  }

  public @Nullable Integer getEffectId() {
    return this.effectId;
  }

  public @NotNull String getDisplayName() {
    return this.displayName;
  }

  public @NotNull List<TextEffectParameter> getParameters() {
    return this.parameters;
  }

  public @Nullable TextEffectShader getFragmentShader() {
    return this.fragmentShader;
  }

  public @Nullable TextEffectShader getVertexShader() {
    return this.vertexShader;
  }

  @Override
  public String toString() {
    return "TextEffect{" +
        "id='" + this.id + '\'' +
        ", effectId=" + this.effectId +
        ", displayName='" + this.displayName + '\'' +
        ", parameters=" + this.parameters +
        ", fragmentShader=" + this.fragmentShader +
        ", vertexShader=" + this.vertexShader +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TextEffect)) {
      return false;
    }
    TextEffect that = (TextEffect) o;
    return Objects.equals(this.id, that.id)
        && Objects.equals(this.effectId, that.effectId)
        && Objects.equals(this.displayName, that.displayName)
        && Objects.equals(this.parameters, that.parameters)
        && Objects.equals(this.fragmentShader, that.fragmentShader)
        && Objects.equals(this.vertexShader, that.vertexShader);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        this.id, this.effectId, this.displayName,
        this.parameters, this.fragmentShader, this.vertexShader
    );
  }

  public static class Builder {

    private final String id;
    private Integer effectId;
    private String displayName;
    private final List<TextEffectParameter> parameters = new ArrayList<>();
    private TextEffectShader fragmentShader;
    private TextEffectShader vertexShader;

    private Builder(@NotNull String id) {
      this.id = id;
    }

    public Builder effectId(int effectId) {
      this.effectId = effectId;
      return this;
    }

    public Builder displayName(@NotNull String displayName) {
      this.displayName = displayName;
      return this;
    }

    public Builder parameter(@NotNull TextEffectParameter parameter) {
      this.parameters.add(parameter);
      return this;
    }

    public Builder parameters(@NotNull List<TextEffectParameter> parameters) {
      this.parameters.addAll(parameters);
      return this;
    }

    public Builder fragmentShader(@NotNull TextEffectShader fragmentShader) {
      this.fragmentShader = fragmentShader;
      return this;
    }

    public Builder fragmentShader(@NotNull String code) {
      this.fragmentShader = TextEffectShader.create(code);
      return this;
    }

    public Builder vertexShader(@NotNull TextEffectShader vertexShader) {
      this.vertexShader = vertexShader;
      return this;
    }

    public Builder vertexShader(@NotNull String code) {
      this.vertexShader = TextEffectShader.create(code);
      return this;
    }

    public @NotNull TextEffect build() {
      Objects.requireNonNull(this.displayName, "Display name must be set");
      return new TextEffect(
          this.id, this.effectId, this.displayName,
          this.parameters, this.fragmentShader, this.vertexShader
      );
    }
  }
}
