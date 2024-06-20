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

package net.labymod.serverapi.core.model.moderation;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RecommendedAddon {

  private final String namespace;
  private boolean required;

  protected RecommendedAddon(@NotNull String namespace, boolean required) {
    Objects.requireNonNull(namespace, "Namespace cannot be null");
    this.namespace = namespace;
    this.required = required;
  }

  public static RecommendedAddon of(@NotNull String namespace) {
    return new RecommendedAddon(namespace, false);
  }

  public static RecommendedAddon of(@NotNull String namespace, boolean required) {
    return new RecommendedAddon(namespace, required);
  }

  public @NotNull String getNamespace() {
    return this.namespace;
  }

  public boolean isRequired() {
    return this.required;
  }

  public void setRequired(boolean required) {
    this.required = required;
  }

  public RecommendedAddon require() {
    this.required = true;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof RecommendedAddon)) {
      return false;
    }

    RecommendedAddon that = (RecommendedAddon) o;
    return this.required == that.required && Objects.equals(this.namespace, that.namespace);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.namespace, this.required);
  }

  @Override
  public String toString() {
    return "RecommendedAddon{" +
        "namespace='" + this.namespace + '\'' +
        ", required=" + this.required +
        '}';
  }
}
