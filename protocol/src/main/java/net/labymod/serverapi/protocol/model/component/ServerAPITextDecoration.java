package net.labymod.serverapi.protocol.model.component;

import java.util.List;

public enum ServerAPITextDecoration {
  OBFUSCATED,
  BOLD,
  STRIKETHROUGH,
  UNDERLINED,
  ITALIC,
  ;

  private static final List<ServerAPITextDecoration> VALUES = List.of(values());

  public static List<ServerAPITextDecoration> getValues() {
    return VALUES;
  }
}
