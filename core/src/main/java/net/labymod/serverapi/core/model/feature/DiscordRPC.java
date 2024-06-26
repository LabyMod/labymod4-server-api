package net.labymod.serverapi.core.model.feature;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class DiscordRPC {

  private static final DiscordRPC RESET = new DiscordRPC(null, 0, 0);

  private final String gameMode;
  private final long startTime;
  private final long endTime;

  protected DiscordRPC(@Nullable String gameMode, long startTime, long endTime) {
    if (startTime < 0) {
      throw new IllegalArgumentException("startTime cannot be negative");
    }
    if (endTime < 0) {
      throw new IllegalArgumentException("endTime cannot be negative");
    }

    this.gameMode = gameMode;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public static DiscordRPC createReset() {
    return RESET;
  }

  public static DiscordRPC create(@NotNull String gameMode) {
    Objects.requireNonNull(gameMode, "GameMode cannot be null");
    return new DiscordRPC(gameMode, 0, 0);
  }

  public static DiscordRPC createWithStart(@NotNull String gameMode, long startTime) {
    Objects.requireNonNull(gameMode, "GameMode cannot be null");
    return new DiscordRPC(gameMode, startTime, 0);
  }

  public static DiscordRPC createWithEnd(@NotNull String gameMode, long endTime) {
    Objects.requireNonNull(gameMode, "GameMode cannot be null");
    return new DiscordRPC(gameMode, 0, endTime);
  }

  public @Nullable String getGameMode() {
    return this.gameMode;
  }

  public long getStartTime() {
    return this.startTime;
  }

  public long getEndTime() {
    return this.endTime;
  }

  public boolean hasGameMode() {
    return this.gameMode != null;
  }

  public boolean hasStartTime() {
    return this.startTime != 0;
  }

  public boolean hasEndTime() {
    return this.endTime != 0;
  }

  @Override
  public String toString() {
    return "DiscordRPC{" +
        "gameMode='" + this.gameMode + '\'' +
        ", startTime=" + this.startTime +
        ", endTime=" + this.endTime +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DiscordRPC that)) {
      return false;
    }
    return this.startTime == that.startTime && this.endTime == that.endTime && Objects.equals(
        this.gameMode, that.gameMode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.gameMode, this.startTime, this.endTime);
  }
}
