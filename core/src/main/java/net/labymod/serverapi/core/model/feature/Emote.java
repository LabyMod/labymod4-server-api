package net.labymod.serverapi.core.model.feature;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class Emote {

  private final UUID uniqueId;
  private final int emoteId;

  protected Emote(@NotNull UUID uniqueId, int emoteId) {
    Objects.requireNonNull(uniqueId, "UniqueId is null");
    this.uniqueId = uniqueId;
    this.emoteId = emoteId;
  }

  public static Emote create(@NotNull UUID uniqueId, int emoteId) {
    return new Emote(uniqueId, emoteId);
  }

  public @NotNull UUID getUniqueId() {
    return this.uniqueId;
  }

  public int getEmoteId() {
    return this.emoteId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Emote)) {
      return false;
    }

    Emote emote = (Emote) o;
    return this.emoteId == emote.emoteId && Objects.equals(this.uniqueId, emote.uniqueId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.uniqueId, this.emoteId);
  }

  @Override
  public String toString() {
    return "Emote{" +
        "uniqueId=" + this.uniqueId +
        ", emoteId=" + this.emoteId +
        '}';
  }
}
