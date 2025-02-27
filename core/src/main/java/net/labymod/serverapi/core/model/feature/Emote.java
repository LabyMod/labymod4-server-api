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

  /**
   * Plays the emote for the supplied npc.
   *
   * @param uniqueId The unique id of the npc.
   * @param emoteId  The emote id.
   * @return The emote instance.
   */
  public static Emote play(@NotNull UUID uniqueId, int emoteId) {
    return new Emote(uniqueId, emoteId);
  }

  /**
   * Stops the current emote for the supplied npc.
   *
   * @param uniqueId The unique id of the npc.
   * @return The emote instance.
   */
  public static Emote stop(@NotNull UUID uniqueId) {
    return new Emote(uniqueId, -1);
  }

  public @NotNull UUID getUniqueId() {
    return this.uniqueId;
  }

  public int getEmoteId() {
    return this.emoteId;
  }

  public boolean isStop() {
    return this.emoteId == -1;
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
