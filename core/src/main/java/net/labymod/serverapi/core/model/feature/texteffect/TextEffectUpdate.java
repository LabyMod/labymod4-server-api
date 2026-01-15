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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents an update to a text effect or rule.
 */
public class TextEffectUpdate {

  private final TextEffectUpdateType type;
  private final String target;
  private final Boolean enabled;
  private final Map<String, Object> parameters;

  private TextEffectUpdate(
      @NotNull TextEffectUpdateType type,
      @NotNull String target,
      @Nullable Boolean enabled,
      @NotNull Map<String, Object> parameters
  ) {
    Objects.requireNonNull(type, "Type cannot be null");
    Objects.requireNonNull(target, "Target cannot be null");
    Objects.requireNonNull(parameters, "Parameters cannot be null");
    this.type = type;
    this.target = target;
    this.enabled = enabled;
    this.parameters = Collections.unmodifiableMap(parameters);
  }

  /**
   * Creates an update for effect parameters.
   */
  public static TextEffectUpdate effectParameters(
      @NotNull String effectId,
      @NotNull Map<String, Object> parameters
  ) {
    return new TextEffectUpdate(
        TextEffectUpdateType.EFFECT_PARAMETERS,
        effectId,
        null,
        parameters
    );
  }

  /**
   * Creates an update for rule state (enable/disable).
   */
  public static TextEffectUpdate ruleState(@NotNull String ruleId, boolean enabled) {
    return new TextEffectUpdate(
        TextEffectUpdateType.RULE_STATE,
        ruleId,
        enabled,
        Collections.emptyMap()
    );
  }

  /**
   * Creates an update for rule parameters.
   */
  public static TextEffectUpdate ruleParameters(
      @NotNull String ruleId,
      @NotNull Map<String, Object> parameters
  ) {
    return new TextEffectUpdate(
        TextEffectUpdateType.RULE_PARAMETERS,
        ruleId,
        null,
        parameters
    );
  }

  public static Builder builder(@NotNull TextEffectUpdateType type) {
    return new Builder(type);
  }

  public @NotNull TextEffectUpdateType getType() {
    return this.type;
  }

  public @NotNull String getTarget() {
    return this.target;
  }

  public @Nullable Boolean getEnabled() {
    return this.enabled;
  }

  public @NotNull Map<String, Object> getParameters() {
    return this.parameters;
  }

  @Override
  public String toString() {
    return "TextEffectUpdate{" +
        "type=" + this.type +
        ", target='" + this.target + '\'' +
        ", enabled=" + this.enabled +
        ", parameters=" + this.parameters +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TextEffectUpdate)) {
      return false;
    }
    TextEffectUpdate that = (TextEffectUpdate) o;
    return this.type == that.type
        && Objects.equals(this.target, that.target)
        && Objects.equals(this.enabled, that.enabled)
        && Objects.equals(this.parameters, that.parameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.type, this.target, this.enabled, this.parameters);
  }

  public static class Builder {

    private final TextEffectUpdateType type;
    private String target;
    private Boolean enabled;
    private final Map<String, Object> parameters = new HashMap<>();

    private Builder(@NotNull TextEffectUpdateType type) {
      this.type = type;
    }

    public Builder target(@NotNull String target) {
      this.target = target;
      return this;
    }

    public Builder enabled(boolean enabled) {
      this.enabled = enabled;
      return this;
    }

    public Builder parameter(@NotNull String name, @NotNull Object value) {
      this.parameters.put(name, value);
      return this;
    }

    public Builder parameters(@NotNull Map<String, Object> parameters) {
      this.parameters.putAll(parameters);
      return this;
    }

    public @NotNull TextEffectUpdate build() {
      Objects.requireNonNull(this.target, "Target must be set");
      return new TextEffectUpdate(this.type, this.target, this.enabled, this.parameters);
    }
  }
}
