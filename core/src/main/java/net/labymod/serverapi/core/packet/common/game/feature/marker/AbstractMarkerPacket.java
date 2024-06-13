package net.labymod.serverapi.core.packet.common.game.feature.marker;

import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class AbstractMarkerPacket implements Packet {

  private int x;
  private int y;
  private int z;
  private boolean large;
  private UUID target;

  protected AbstractMarkerPacket(int x, int y, int z, boolean large, @Nullable UUID target) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.large = large;
    this.target = target;
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.x = reader.readVarInt();
    this.y = reader.readVarInt();
    this.z = reader.readVarInt();
    this.large = reader.readBoolean();
    this.target = reader.readOptional(reader::readUUID);
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeVarInt(this.x);
    writer.writeVarInt(this.y);
    writer.writeVarInt(this.z);
    writer.writeBoolean(this.large);
    writer.writeOptional(this.target, writer::writeUUID);
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public int getZ() {
    return this.z;
  }

  public boolean isLarge() {
    return this.large;
  }

  public @Nullable UUID getTarget() {
    return this.target;
  }

  @Override
  public String toString() {
    return "AbstractMarkerPacket{" +
        "x=" + this.x +
        ", y=" + this.y +
        ", z=" + this.z +
        ", large=" + this.large +
        ", target=" + this.target +
        '}';
  }
}
