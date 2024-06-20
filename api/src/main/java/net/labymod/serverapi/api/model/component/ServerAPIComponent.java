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

package net.labymod.serverapi.api.model.component;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.List;

public interface ServerAPIComponent {

  static @NotNull ServerAPITextComponent text(@NotNull String text) {
    return new ServerAPITextComponent(text);
  }

  static @NotNull ServerAPITextComponent text(@NotNull String text,
                                              @Nullable ServerAPITextColor color) {
    return ServerAPIComponent.text(text).color(color);
  }

  static @NotNull ServerAPITextComponent text(
      @NotNull String text,
      @Nullable ServerAPITextColor color,
      @NotNull ServerAPITextDecoration... decorations
  ) {
    return ServerAPIComponent.text(text, color).decorate(decorations);
  }

  static @NotNull ServerAPITextComponent text(@NotNull Object text) {
    return new ServerAPITextComponent(String.valueOf(text));
  }

  static @NotNull ServerAPITextComponent text(@NotNull Object text,
                                              @Nullable ServerAPITextColor color) {
    return ServerAPIComponent.text(text).color(color);
  }

  static @NotNull ServerAPITextComponent text(
      @NotNull Object text,
      @Nullable ServerAPITextColor color,
      @NotNull ServerAPITextDecoration... decorations
  ) {
    return ServerAPIComponent.text(text, color).decorate(decorations);
  }

  static @NotNull ServerAPITextComponent empty() {
    return new ServerAPITextComponent("");
  }

  @NotNull ServerAPIComponent append(@NotNull ServerAPIComponent component);

  @NotNull ServerAPIComponent append(int index, @NotNull ServerAPIComponent component);

  @NotNull ServerAPIComponent remove(int index);

  @NotNull ServerAPIComponent replace(int index, @NotNull ServerAPIComponent component);

  @NotNull ServerAPIComponent color(@Nullable ServerAPITextColor color);

  @NotNull ServerAPIComponent colorIfAbsent(@Nullable ServerAPITextColor color);

  @NotNull ServerAPIComponent decorate(@NotNull ServerAPITextDecoration decoration);

  @NotNull ServerAPIComponent decorate(@NotNull ServerAPITextDecoration... decorations);

  @NotNull ServerAPIComponent undecorate(@NotNull ServerAPITextDecoration decoration);

  @NotNull ServerAPIComponent undecorate(@NotNull ServerAPITextDecoration... decorations);

  @NotNull ServerAPIComponent unsetDecoration(@NotNull ServerAPITextDecoration decoration);

  @NotNull ServerAPIComponent unsetDecoration(@NotNull ServerAPITextDecoration... decorations);

  @NotNull ServerAPIComponent applyStyleFrom(@NotNull ServerAPIComponent component);

  /**
   * Tests if the supplied decoration is set to true.
   *
   * @param decoration the decoration to test
   * @return true if the decoration is set to true, false if it is set to false
   */
  boolean hasDecoration(@NotNull ServerAPITextDecoration decoration);

  /**
   * Tests if the supplied decoration is set to either true or false.
   *
   * @param decoration the decoration to test
   * @return true if the decoration is set to either true or false, false if it is not set and thus
   * inherits from the parent
   */
  boolean isDecorationSet(@NotNull ServerAPITextDecoration decoration);

  EnumMap<ServerAPITextDecoration, Boolean> getDecorations();

  List<ServerAPIComponent> getChildren();

  @NotNull ServerAPIComponent setChildren(@NotNull List<ServerAPIComponent> children);

  @Nullable ServerAPITextColor getColor();
}
