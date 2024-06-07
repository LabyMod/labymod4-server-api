package net.labymod.serverapi.core.packet.clientbound.game.display;

import net.labymod.serverapi.protocol.packet.Packet;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.labymod.serverapi.protocol.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TabListBannerPacket implements Packet {

  private String iconUrl;

  public TabListBannerPacket(@Nullable String iconUrl) {
    this.iconUrl = iconUrl;
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.iconUrl = reader.readOptionalString();
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeOptionalString(this.iconUrl);
  }

  public @NotNull String getIconUrl() {
    return this.iconUrl;
  }

  @Override
  public String toString() {
    return "TabListBannerPacket{" + "iconUrl='" + this.iconUrl + '\'' + '}';
  }
}
