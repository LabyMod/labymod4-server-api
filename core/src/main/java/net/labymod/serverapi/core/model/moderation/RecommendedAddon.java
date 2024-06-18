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
