package net.labymod.serverapi.core.model.display;

import net.labymod.serverapi.protocol.model.component.ServerAPIComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class Subtitle {

  private final UUID uniqueId;
  private final ServerAPIComponent text;
  private double size;

  private Subtitle(@NotNull UUID uniqueId, @NotNull ServerAPIComponent text, double size) {
    Objects.requireNonNull(uniqueId, "UniqueId is null");
    Objects.requireNonNull(text, "Text is null");
    this.uniqueId = uniqueId;
    this.text = text;
    this.setSize(size);
  }

  private Subtitle(@NotNull UUID uniqueId, @NotNull ServerAPIComponent text) {
    this(uniqueId, text, 1.0);
  }

  public static Subtitle create(@NotNull UUID uniqueId, @NotNull ServerAPIComponent text) {
    return new Subtitle(uniqueId, text);
  }

  public static Subtitle create(
      @NotNull UUID uniqueId,
      @NotNull ServerAPIComponent text,
      double size
  ) {
    return new Subtitle(uniqueId, text, size);
  }

  public @NotNull UUID getUniqueId() {
    return this.uniqueId;
  }

  public double getSize() {
    return this.size;
  }

  private void setSize(double size) {
    if (size < 0.25) {
      size = 0.25;
    }

    if (size > 1.0) {
      size = 1.0;
    }

    this.size = size;
  }

  public @NotNull ServerAPIComponent text() {
    return this.text;
  }
}
