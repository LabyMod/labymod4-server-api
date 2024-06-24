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

import net.labymod.serverapi.api.model.component.ServerAPIComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class InteractionMenuEntry {

  private final ServerAPIComponent displayName;
  private final InteractionMenuType type;
  private final String value;
  private final String iconUrl;

  protected InteractionMenuEntry(
      @NotNull ServerAPIComponent displayName,
      @NotNull InteractionMenuType type,
      @NotNull String value,
      @Nullable String iconUrl
  ) {
    Objects.requireNonNull(displayName, "Display name cannot be null");
    Objects.requireNonNull(type, "Type cannot be null");
    Objects.requireNonNull(value, "Value cannot be null");
    this.displayName = displayName;
    this.iconUrl = iconUrl;
    this.type = type;
    this.value = value;
  }

  public static InteractionMenuEntry create(
      @NotNull ServerAPIComponent displayName,
      @NotNull InteractionMenuType type,
      @NotNull String value,
      @Nullable String iconUrl
  ) {
    return new InteractionMenuEntry(displayName, type, value, iconUrl);
  }

  public static InteractionMenuEntry create(
      @NotNull ServerAPIComponent displayName,
      @NotNull InteractionMenuType type,
      @NotNull String value
  ) {
    return new InteractionMenuEntry(displayName, type, value, null);
  }

  public @NotNull ServerAPIComponent displayName() {
    return this.displayName;
  }

  public @NotNull InteractionMenuType type() {
    return this.type;
  }

  public @NotNull String getValue() {
    return this.value;
  }

  public @Nullable String getIconUrl() {
    return this.iconUrl;
  }

  @Override
  public String toString() {
    return "InteractionMenuEntry{" +
        "displayName=" + this.displayName +
        ", type=" + this.type +
        ", value='" + this.value + '\'' +
        ", iconUrl='" + this.iconUrl + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof InteractionMenuEntry that)) {
      return false;
    }

    return Objects.equals(this.displayName, that.displayName) && this.type == that.type
        && Objects.equals(this.value, that.value) && Objects.equals(this.iconUrl,
        that.iconUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.displayName, this.type, this.value, this.iconUrl);
  }

  public enum InteractionMenuType {
    CLIPBOARD,
    RUN_COMMAND,
    SUGGEST_COMMAND,
    OPEN_BROWSER,
    ;

    private static final InteractionMenuType[] VALUES = values();

    public static InteractionMenuType fromString(String type) {
      for (InteractionMenuType value : values()) {
        if (value.name().equalsIgnoreCase(type)) {
          return value;
        }
      }

      return null;
    }
  }
}
