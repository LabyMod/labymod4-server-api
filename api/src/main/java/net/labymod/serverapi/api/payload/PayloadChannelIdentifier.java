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

package net.labymod.serverapi.api.payload;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class PayloadChannelIdentifier {

  private final String combinedIdentifier;
  private final String namespace;
  private final String path;

  private PayloadChannelIdentifier(@NotNull String namespace, @NotNull String path) {
    Objects.requireNonNull(namespace, "Namespace cannot be null");
    Objects.requireNonNull(path, "Path cannot be null");
    this.namespace = namespace;
    this.path = path;
    this.combinedIdentifier = namespace + ":" + path;
  }

  /**
   * Creates a new {@link PayloadChannelIdentifier} with the given {@code namespace} and {@code
   * path}.
   *
   * @param namespace The namespace of the channel identifier.
   * @param path      The path of the channel identifier.
   * @return A new created payload channel identifier.
   */
  @Contract(value = "_, _ -> new", pure = true)
  public static @NotNull PayloadChannelIdentifier create(
      @NotNull String namespace,
      @NotNull String path
  ) {
    return new PayloadChannelIdentifier(namespace, path);
  }

  /**
   * Retrieves the {@link #namespace} of this payload channel identifier.
   *
   * @return The payload channel identifier {@link #namespace};
   */
  public @NotNull String getNamespace() {
    return this.namespace;
  }

  /**
   * Retrieves the {@link #path} of this payload channel identifier.
   *
   * @return The payload channel identifier {@link #path}.
   */
  public @NotNull String getPath() {
    return this.path;
  }

  /**
   * {@inheritDoc}
   */
  @Contract(pure = true)
  @Override
  public @NotNull String toString() {
    return this.combinedIdentifier;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    PayloadChannelIdentifier that = (PayloadChannelIdentifier) o;
    return Objects.equals(this.namespace, that.namespace) && Objects.equals(this.path, that.path);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.namespace, this.path);
  }
}


