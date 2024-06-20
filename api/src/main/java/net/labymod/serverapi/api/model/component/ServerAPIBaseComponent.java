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

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class ServerAPIBaseComponent<T extends ServerAPIBaseComponent<?>>
    implements ServerAPIComponent {

  private ServerAPITextColor textColor;
  private EnumMap<ServerAPITextDecoration, Boolean> decorations = new EnumMap<>(
      ServerAPITextDecoration.class);
  private List<ServerAPIComponent> children = new ArrayList<>();

  @Override
  public @NotNull T append(@NotNull ServerAPIComponent component) {
    this.children.add(component);
    return (T) this;
  }

  @Override
  public @NotNull T append(int index, @NotNull ServerAPIComponent component) {
    this.children.add(index, component);
    return (T) this;
  }

  @Override
  public @NotNull T remove(int index) {
    this.children.remove(index);
    return (T) this;
  }

  @Override
  public @NotNull T replace(int index, @NotNull ServerAPIComponent component) {
    this.children.set(index, component);
    return (T) this;
  }

  @Override
  public @NotNull T color(ServerAPITextColor color) {
    this.textColor = color;
    return (T) this;
  }

  @Override
  public @NotNull T colorIfAbsent(ServerAPITextColor color) {
    if (this.textColor == null) {
      this.textColor = color;
    }

    return (T) this;
  }

  @Override
  public @NotNull T decorate(ServerAPITextDecoration decoration) {
    this.decorations.put(decoration, true);
    return (T) this;
  }

  @Override
  public @NotNull T decorate(ServerAPITextDecoration... decorations) {
    for (ServerAPITextDecoration decoration : decorations) {
      this.decorations.put(decoration, true);
    }

    return (T) this;
  }

  @Override
  public @NotNull T undecorate(ServerAPITextDecoration decoration) {
    this.decorations.put(decoration, false);
    return (T) this;
  }

  @Override
  public @NotNull T undecorate(ServerAPITextDecoration... decorations) {
    for (ServerAPITextDecoration decoration : decorations) {
      this.decorations.put(decoration, false);
    }

    return (T) this;
  }

  @Override
  public @NotNull T unsetDecoration(ServerAPITextDecoration decoration) {
    this.decorations.remove(decoration);
    return (T) this;
  }

  @Override
  public @NotNull T unsetDecoration(ServerAPITextDecoration... decorations) {
    for (ServerAPITextDecoration decoration : decorations) {
      this.decorations.remove(decoration);
    }

    return (T) this;
  }

  @Override
  public @NotNull T applyStyleFrom(@NotNull ServerAPIComponent component) {
    if (component instanceof ServerAPIBaseComponent) {
      this.textColor = ((ServerAPIBaseComponent<?>) component).textColor;
      this.decorations = new EnumMap<>(((ServerAPIBaseComponent<?>) component).decorations);
    }

    return (T) this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasDecoration(ServerAPITextDecoration decoration) {
    return this.decorations.get(decoration) == Boolean.TRUE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDecorationSet(ServerAPITextDecoration decoration) {
    return this.decorations.get(decoration) != null;
  }

  @Override
  public EnumMap<ServerAPITextDecoration, Boolean> getDecorations() {
    return this.decorations;
  }

  @Override
  public List<ServerAPIComponent> getChildren() {
    return this.children;
  }

  @Override
  public @NotNull T setChildren(@NotNull List<ServerAPIComponent> children) {
    this.children = children;
    return (T) this;
  }

  @Override
  public @Nullable ServerAPITextColor getColor() {
    return this.textColor;
  }

  @Override
  public String toString() {
    return "ServerAPIBaseComponent{" +
        "textColor=" + this.textColor +
        ", decorations=" + this.decorations +
        ", children=" + this.children +
        '}';
  }
}
