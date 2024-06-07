package net.labymod.serverapi.core.packet.clientbound.game.display;

import net.labymod.serverapi.core.model.display.TabListFlag;
import net.labymod.serverapi.protocol.packet.Packet;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.labymod.serverapi.protocol.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TabListFlagPacket implements Packet {

  private List<TabListFlag> flags;

  public TabListFlagPacket(List<TabListFlag> flags) {
    this.flags = flags;
  }

  public TabListFlagPacket(TabListFlag... flags) {
    this.flags = List.of(flags);
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.flags = reader.readList(() -> TabListFlag.create(reader.readUUID(), reader.readString()));
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeCollection(this.flags, (flag) -> {
      writer.writeUUID(flag.getUniqueId());
      writer.writeString(flag.getCountryCode().name());
    });
  }

  public @NotNull List<TabListFlag> getFlags() {
    return this.flags;
  }

  @Override
  public String toString() {
    return "TabListFlagPacket{" +
        "flags=" + this.flags +
        '}';
  }
}
