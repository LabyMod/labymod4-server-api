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

package net.labymod.serverapi.core.model.supplement;

import net.labymod.serverapi.api.model.component.ServerAPIComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ServerSwitchPrompt {

  private final ServerAPIComponent title;
  private final String address;
  private final boolean showPreview;

  protected ServerSwitchPrompt(
      @NotNull ServerAPIComponent title,
      @NotNull String address,
      boolean showPreview
  ) {
    this.title = title;
    this.address = address;
    this.showPreview = showPreview;
  }

  /**
   * Creates a new server switch prompt instance.
   *
   * @param title       the title of the server switch prompt
   * @param address     the address of the suggested server
   * @param showPreview whether the server switch prompt should contain a live preview of the server
   * @return a new server switch prompt instance
   */
  public static ServerSwitchPrompt create(
      @NotNull ServerAPIComponent title,
      @NotNull String address,
      boolean showPreview
  ) {
    return new ServerSwitchPrompt(title, address, showPreview);
  }

  /**
   * Creates a new server switch prompt instance with the live preview set to true.
   *
   * @param title   the title of the server switch prompt
   * @param address the address of the suggested server
   * @return a new server switch prompt instance
   */
  public static ServerSwitchPrompt create(
      @NotNull ServerAPIComponent title,
      @NotNull String address
  ) {
    return new ServerSwitchPrompt(title, address, true);
  }

  /**
   * @return the title of the server switch prompt
   */
  public @NotNull ServerAPIComponent title() {
    return this.title;
  }

  /**
   * @return the address of the suggested server
   */
  public @NotNull String getAddress() {
    return this.address;
  }

  /**
   * @return whether the server switch prompt should contain a live preview of the server
   */
  public boolean isShowPreview() {
    return this.showPreview;
  }

  @Override
  public String toString() {
    return "ServerSwitchPrompt{" +
        "title=" + this.title +
        ", address='" + this.address + '\'' +
        ", showPreview=" + this.showPreview +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ServerSwitchPrompt that)) {
      return false;
    }

    return this.showPreview == that.showPreview && Objects.equals(this.title, that.title)
        && Objects.equals(this.address, that.address);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.title, this.address, this.showPreview);
  }
}
