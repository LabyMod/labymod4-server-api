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
import java.util.UUID;

/**
 * Represents a rule for when a text effect should be applied.
 */
public class TextEffectRule {

  private static final int DEFAULT_PRIORITY = 100;

  private final String id;
  private final String effect;
  private final TextEffectContext context;
  private final int priority;
  private final UUID targetUser;
  private final boolean enabled;
  private final Map<String, Object> parameters;

  private TextEffectRule(
      @NotNull String id,
      @NotNull String effect,
      @NotNull TextEffectContext context,
      int priority,
      @Nullable UUID targetUser,
      boolean enabled,
      @NotNull Map<String, Object> parameters
  ) {
    Objects.requireNonNull(id, "Id cannot be null");
    Objects.requireNonNull(effect, "Effect cannot be null");
    Objects.requireNonNull(context, "Context cannot be null");
    Objects.requireNonNull(parameters, "Parameters cannot be null");
    this.id = id;
    this.effect = effect;
    this.context = context;
    this.priority = priority;
    this.targetUser = targetUser;
    this.enabled = enabled;
    this.parameters = Collections.unmodifiableMap(parameters);
  }

  public static Builder builder(@NotNull String id) {
    return new Builder(id);
  }

  public @NotNull String getId() {
    return this.id;
  }

  public @NotNull String getEffect() {
    return this.effect;
  }

  public @NotNull TextEffectContext getContext() {
    return this.context;
  }

  public int getPriority() {
    return this.priority;
  }

  public @Nullable UUID getTargetUser() {
    return this.targetUser;
  }

  public boolean isEnabled() {
    return this.enabled;
  }

  public @NotNull Map<String, Object> getParameters() {
    return this.parameters;
  }

  @Override
  public String toString() {
    return "TextEffectRule{" +
        "id='" + this.id + '\'' +
        ", effect='" + this.effect + '\'' +
        ", context=" + this.context +
        ", priority=" + this.priority +
        ", targetUser=" + this.targetUser +
        ", enabled=" + this.enabled +
        ", parameters=" + this.parameters +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TextEffectRule)) {
      return false;
    }
    TextEffectRule that = (TextEffectRule) o;
    return this.priority == that.priority
        && this.enabled == that.enabled
        && Objects.equals(this.id, that.id)
        && Objects.equals(this.effect, that.effect)
        && this.context == that.context
        && Objects.equals(this.targetUser, that.targetUser)
        && Objects.equals(this.parameters, that.parameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        this.id, this.effect, this.context, this.priority,
        this.targetUser, this.enabled, this.parameters
    );
  }

  public static class Builder {

    private final String id;
    private String effect;
    private TextEffectContext context;
    private int priority = DEFAULT_PRIORITY;
    private UUID targetUser;
    private boolean enabled = true;
    private final Map<String, Object> parameters = new HashMap<>();

    private Builder(@NotNull String id) {
      this.id = id;
    }

    public Builder effect(@NotNull String effect) {
      this.effect = effect;
      return this;
    }

    public Builder context(@NotNull TextEffectContext context) {
      this.context = context;
      return this;
    }

    public Builder priority(int priority) {
      this.priority = priority;
      return this;
    }

    public Builder targetUser(@Nullable UUID targetUser) {
      this.targetUser = targetUser;
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

    public @NotNull TextEffectRule build() {
      Objects.requireNonNull(this.effect, "Effect must be set");
      Objects.requireNonNull(this.context, "Context must be set");
      return new TextEffectRule(
          this.id, this.effect, this.context, this.priority,
          this.targetUser, this.enabled, this.parameters
      );
    }
  }
}
