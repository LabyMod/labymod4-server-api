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

package net.labymod.serverapi.core.packet.clientbound.game.feature.texteffect;

import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Packet to remove server-registered effects and rules.
 */
public class TextEffectClearPacket implements Packet {

  private boolean clearEffects;
  private boolean clearRules;
  private List<String> specificEffects;
  private List<String> specificRules;

  private TextEffectClearPacket(
      boolean clearEffects,
      boolean clearRules,
      @NotNull List<String> specificEffects,
      @NotNull List<String> specificRules
  ) {
    this.clearEffects = clearEffects;
    this.clearRules = clearRules;
    this.specificEffects = specificEffects;
    this.specificRules = specificRules;
  }

  /**
   * Creates a packet to clear all server effects and rules.
   */
  public static TextEffectClearPacket clearAll() {
    return new TextEffectClearPacket(true, true, Collections.emptyList(), Collections.emptyList());
  }

  /**
   * Creates a packet to clear all server effects.
   */
  public static TextEffectClearPacket clearAllEffects() {
    return new TextEffectClearPacket(true, false, Collections.emptyList(), Collections.emptyList());
  }

  /**
   * Creates a packet to clear all server rules.
   */
  public static TextEffectClearPacket clearAllRules() {
    return new TextEffectClearPacket(false, true, Collections.emptyList(), Collections.emptyList());
  }

  /**
   * Creates a packet to clear specific effects by ID.
   */
  public static TextEffectClearPacket clearEffects(@NotNull List<String> effectIds) {
    Objects.requireNonNull(effectIds, "Effect IDs cannot be null");
    return new TextEffectClearPacket(false, false, effectIds, Collections.emptyList());
  }

  /**
   * Creates a packet to clear specific effects by ID.
   */
  public static TextEffectClearPacket clearEffects(@NotNull String... effectIds) {
    Objects.requireNonNull(effectIds, "Effect IDs cannot be null");
    return new TextEffectClearPacket(
        false, false,
        Collections.unmodifiableList(Arrays.asList(effectIds)),
        Collections.emptyList()
    );
  }

  /**
   * Creates a packet to clear specific rules by ID.
   */
  public static TextEffectClearPacket clearRules(@NotNull List<String> ruleIds) {
    Objects.requireNonNull(ruleIds, "Rule IDs cannot be null");
    return new TextEffectClearPacket(false, false, Collections.emptyList(), ruleIds);
  }

  /**
   * Creates a packet to clear specific rules by ID.
   */
  public static TextEffectClearPacket clearRules(@NotNull String... ruleIds) {
    Objects.requireNonNull(ruleIds, "Rule IDs cannot be null");
    return new TextEffectClearPacket(
        false, false,
        Collections.emptyList(),
        Collections.unmodifiableList(Arrays.asList(ruleIds))
    );
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.clearEffects = reader.readBoolean();
    this.clearRules = reader.readBoolean();
    this.specificEffects = reader.readList(reader::readString);
    this.specificRules = reader.readList(reader::readString);
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeBoolean(this.clearEffects);
    writer.writeBoolean(this.clearRules);
    writer.writeCollection(this.specificEffects, writer::writeString);
    writer.writeCollection(this.specificRules, writer::writeString);
  }

  public boolean isClearEffects() {
    return this.clearEffects;
  }

  public boolean isClearRules() {
    return this.clearRules;
  }

  public @NotNull List<String> getSpecificEffects() {
    return this.specificEffects;
  }

  public @NotNull List<String> getSpecificRules() {
    return this.specificRules;
  }

  @Override
  public String toString() {
    return "TextEffectClearPacket{" +
        "clearEffects=" + this.clearEffects +
        ", clearRules=" + this.clearRules +
        ", specificEffects=" + this.specificEffects +
        ", specificRules=" + this.specificRules +
        '}';
  }

  public static class Builder {

    private boolean clearEffects;
    private boolean clearRules;
    private final List<String> specificEffects = new ArrayList<>();
    private final List<String> specificRules = new ArrayList<>();

    private Builder() {
    }

    public Builder clearEffects(boolean clearEffects) {
      this.clearEffects = clearEffects;
      return this;
    }

    public Builder clearRules(boolean clearRules) {
      this.clearRules = clearRules;
      return this;
    }

    public Builder specificEffect(@NotNull String effectId) {
      this.specificEffects.add(effectId);
      return this;
    }

    public Builder specificEffects(@NotNull List<String> effectIds) {
      this.specificEffects.addAll(effectIds);
      return this;
    }

    public Builder specificRule(@NotNull String ruleId) {
      this.specificRules.add(ruleId);
      return this;
    }

    public Builder specificRules(@NotNull List<String> ruleIds) {
      this.specificRules.addAll(ruleIds);
      return this;
    }

    public @NotNull TextEffectClearPacket build() {
      return new TextEffectClearPacket(
          this.clearEffects,
          this.clearRules,
          Collections.unmodifiableList(new ArrayList<>(this.specificEffects)),
          Collections.unmodifiableList(new ArrayList<>(this.specificRules))
      );
    }
  }
}
