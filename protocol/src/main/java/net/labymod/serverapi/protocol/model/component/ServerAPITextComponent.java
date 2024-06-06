package net.labymod.serverapi.protocol.model.component;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ServerAPITextComponent extends ServerAPIBaseComponent<ServerAPITextComponent> {

  private String text;

  protected ServerAPITextComponent(@NotNull String text) {
    Objects.requireNonNull(text, "Text");
    this.text = text;
  }

  public ServerAPITextComponent text(@NotNull String text) {
    Objects.requireNonNull(text, "Text");
    this.text = text;
    return this;
  }

  public @NotNull String getText() {
    return this.text;
  }
}
