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

package net.labymod.serverapi.core.packet.clientbound.game.moderation;

import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AddonDisablePacket implements Packet {

  private List<String> addonsToDisable;
  private Action action;

  protected AddonDisablePacket(@NotNull Action action, @NotNull List<String> addonsToDisable) {
    Objects.requireNonNull(addonsToDisable, "Addons to disable cannot be null");
    Objects.requireNonNull(action, "Action cannot be null");
    this.action = action;
    this.addonsToDisable = addonsToDisable;
  }

  protected AddonDisablePacket(@NotNull Action action, @NotNull String... addonsToDisable) {
    this(action, Collections.unmodifiableList(Arrays.asList(addonsToDisable)));
  }

  /**
   * Creates a packet that force disables the provided addons
   *
   * @param addonsToDisable the addons to disable
   * @return the created packet
   */
  public static AddonDisablePacket disable(@NotNull List<String> addonsToDisable) {
    return new AddonDisablePacket(Action.DISABLE, addonsToDisable);
  }

  /**
   * Creates a packet that reverts the forced disable state for the provided addons
   *
   * @param addonsToRevert the addons to revert
   * @return the created packet
   */
  public static AddonDisablePacket revert(@NotNull List<String> addonsToRevert) {
    return new AddonDisablePacket(Action.REVERT, addonsToRevert);
  }

  /**
   * Creates a packet that force disables the provided addons
   *
   * @param addonsToDisable the addons to disable
   * @return the created packet
   */
  public static AddonDisablePacket disable(@NotNull String... addonsToDisable) {
    return new AddonDisablePacket(Action.DISABLE, addonsToDisable);
  }

  /**
   * Creates a packet that reverts the forced disable state for the provided addons
   *
   * @param addonsToRevert the addons to revert
   * @return the created packet
   */
  public static AddonDisablePacket revert(@NotNull String... addonsToRevert) {
    return new AddonDisablePacket(Action.REVERT, addonsToRevert);
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.action = reader.readBoolean() ? Action.DISABLE : Action.REVERT;
    this.addonsToDisable = reader.readList(reader::readString);
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeBoolean(this.action == Action.DISABLE);
    writer.writeCollection(this.addonsToDisable, writer::writeString);
  }

  public @NotNull List<String> getAddonsToDisable() {
    return this.addonsToDisable;
  }

  public @NotNull Action action() {
    return this.action;
  }

  @Override
  public String toString() {
    return "AddonDisablePacket{" +
        "addonsToDisable=" + this.addonsToDisable +
        ", action=" + this.action +
        '}';
  }

  public enum Action {
    DISABLE,
    REVERT
  }
}
