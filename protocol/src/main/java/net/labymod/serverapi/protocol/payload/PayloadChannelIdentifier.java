package net.labymod.serverapi.protocol.payload;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class PayloadChannelIdentifier {

  private final String namespace;
  private final String path;

  private PayloadChannelIdentifier(@NotNull String namespace, @NotNull String path) {
    Objects.requireNonNull(namespace, "Namespace cannot be null");
    Objects.requireNonNull(path, "Path cannot be null");
    this.namespace = namespace;
    this.path = path;
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
    return this.namespace + ":" + this.path;
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


