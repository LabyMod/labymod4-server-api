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

  @Override
  public String toString() {
    return "ServerAPITextComponent{" +
        "text='" + this.text + '\'' +
        "} " + super.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ServerAPITextComponent)) {
      return false;
    }

    if (!super.equals(o)) {
      return false;
    }

    ServerAPITextComponent that = (ServerAPITextComponent) o;
    return Objects.equals(this.text, that.text);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.text);
  }
}
