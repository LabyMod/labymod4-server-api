package net.labymod.serverapi.core.packet.clientbound.game.feature.banner;

import java.util.Objects;
import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Updates the image displayed by all banner canvases using the given namespace. */
public class BannerImagePacket implements Packet {

  private String namespace;
  private String imageUrl;

  public BannerImagePacket(@NotNull String namespace, @Nullable String imageUrl) {
    this.namespace = Objects.requireNonNull(namespace, "Namespace");
    this.imageUrl = imageUrl;
  }

  public static BannerImagePacket update(@NotNull String namespace, @NotNull String imageUrl) {
    return new BannerImagePacket(namespace, Objects.requireNonNull(imageUrl, "Image URL"));
  }

  public static BannerImagePacket clear(@NotNull String namespace) {
    return new BannerImagePacket(namespace, null);
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.namespace = reader.readString();
    this.imageUrl = reader.readOptionalString();
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeString(this.namespace);
    writer.writeOptionalString(this.imageUrl);
  }

  public @NotNull String namespace() {
    return this.namespace;
  }

  public @Nullable String imageUrl() {
    return this.imageUrl;
  }
}
