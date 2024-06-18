package net.labymod.serverapi.api.model.component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum ServerAPITextDecoration {
  OBFUSCATED,
  BOLD,
  STRIKETHROUGH,
  UNDERLINED,
  ITALIC,
  ;

  private static final List<ServerAPITextDecoration> VALUES = Collections.unmodifiableList(
      Arrays.asList(values())
  );

  public static List<ServerAPITextDecoration> getValues() {
    return VALUES;
  }
}
