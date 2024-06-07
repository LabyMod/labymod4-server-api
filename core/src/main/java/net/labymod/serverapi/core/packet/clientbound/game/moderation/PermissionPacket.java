package net.labymod.serverapi.core.packet.clientbound.game.moderation;

import net.labymod.serverapi.core.model.moderation.Permission;
import net.labymod.serverapi.core.model.moderation.Permission.StatedPermission;
import net.labymod.serverapi.protocol.packet.Packet;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.labymod.serverapi.protocol.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class PermissionPacket implements Packet {

  private List<StatedPermission> permissions;

  public PermissionPacket(@NotNull List<StatedPermission> permissions) {
    Objects.requireNonNull(permissions, "Permissions");
    this.permissions = permissions;
  }

  public PermissionPacket(@NotNull StatedPermission... permissions) {
    this(List.of(permissions));
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.permissions = reader.readList(() -> {
      Permission permission = Permission.of(reader.readString());
      boolean allowed = reader.readBoolean();
      return allowed ? permission.allow() : permission.deny();
    });
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeCollection(this.permissions, actualPermission -> {
      writer.writeString(actualPermission.permission());
      writer.writeBoolean(actualPermission.allowed());
    });
  }

  public @NotNull List<StatedPermission> getPermissions() {
    return this.permissions;
  }
}
