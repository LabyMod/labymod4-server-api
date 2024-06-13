package net.labymod.serverapi.api.model.component;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ServerAPITextColor {

  private static final Map<Integer, ServerAPITextColor> NAMED_VALUES = new HashMap<>();
  private static final Map<String, ServerAPITextColor> NAMED = new HashMap<>();

  public static final ServerAPITextColor BLACK = named("BLACK", 0);
  public static final ServerAPITextColor DARK_BLUE = named("DARK_BLUE", 170);
  public static final ServerAPITextColor DARK_GREEN = named("DARK_GREEN", 43520);
  public static final ServerAPITextColor DARK_AQUA = named("DARK_AQUA", 43690);
  public static final ServerAPITextColor DARK_RED = named("DARK_RED", 11141120);
  public static final ServerAPITextColor DARK_PURPLE = named("DARK_PURPLE", 11141290);
  public static final ServerAPITextColor GOLD = named("GOLD", 16755200);
  public static final ServerAPITextColor GRAY = named("GRAY", 11184810);
  public static final ServerAPITextColor DARK_GRAY = named("DARK_GRAY", 5592405);
  public static final ServerAPITextColor BLUE = named("BLUE", 5592575);
  public static final ServerAPITextColor GREEN = named("GREEN", 5635925);
  public static final ServerAPITextColor AQUA = named("AQUA", 5636095);
  public static final ServerAPITextColor RED = named("RED", 16733525);
  public static final ServerAPITextColor LIGHT_PURPLE = named("LIGHT_PURPLE", 16733695);
  public static final ServerAPITextColor YELLOW = named("YELLOW", 16777045);
  public static final ServerAPITextColor WHITE = named("WHITE", 16777215);

  private final int value;

  private ServerAPITextColor(int value) {
    this.value = value;
  }

  public static @NotNull ServerAPITextColor of(int rgb) {
    return new ServerAPITextColor(rgb);
  }

  public static @NotNull ServerAPITextColor of(int red, int green, int blue) {
    return new ServerAPITextColor((red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF));
  }

  public static @NotNull ServerAPITextColor of(String hex) {
    return new ServerAPITextColor(Integer.parseInt(hex, 16));
  }

  public static @NotNull ServerAPITextColor of(int alpha, int red, int green, int blue) {
    return new ServerAPITextColor(
        (alpha & 0xFF) << 24 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF));
  }

  public static @NotNull ServerAPITextColor of(int alpha, int value) {
    return new ServerAPITextColor((alpha & 0xFF) << 24 | value);
  }

  @ApiStatus.Internal
  private static ServerAPITextColor named(String name, int value) {
    ServerAPITextColor color = new ServerAPITextColor(value);
    NAMED.put(name, color);
    NAMED_VALUES.put(value, color);
    return color;
  }

  public static @Nullable ServerAPITextColor getByName(String name) {
    return NAMED.get(name);
  }

  public int getValue() {
    return this.value;
  }

  @Override
  public String toString() {
    return "ServerAPITextColor{" +
        "value=" + this.value +
        '}';
  }
}
