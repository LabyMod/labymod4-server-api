package net.labymod.serverapi.core.packet.serverbound.game.moderation;

import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.packet.clientbound.game.moderation.InstalledAddonsRequestPacket;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A packet that is sent to the server when an addon is either enabled or disabled. To receive
 * this packet, you first need to send a {@link InstalledAddonsRequestPacket} to the client with
 * all the addons you want to be notified of.
 */
public class AddonStateChangedPacket implements Packet {

  private String namespace;
  private boolean enabled;

  public AddonStateChangedPacket(@NotNull String namespace, boolean enabled) {
    Objects.requireNonNull(namespace, "Namespace cannot be null");
    this.namespace = namespace;
    this.enabled = enabled;
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.namespace = reader.readString();
    this.enabled = reader.readBoolean();
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeString(this.namespace);
    writer.writeBoolean(this.enabled);
  }

  public @NotNull String getNamespace() {
    return this.namespace;
  }

  public boolean isEnabled() {
    return this.enabled;
  }

  @Override
  public String toString() {
    return "AddonStateChangedPacket{" +
        "namespace='" + this.namespace + '\'' +
        ", enabled=" + this.enabled +
        '}';
  }
}
