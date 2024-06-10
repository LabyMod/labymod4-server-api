package net.labymod.serverapi.core.model.feature;

import net.labymod.serverapi.protocol.model.component.ServerAPIComponent;
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
