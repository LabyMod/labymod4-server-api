package net.labymod.serverapi.core.packet.serverbound.game.moderation;

import net.labymod.serverapi.api.packet.IdentifiablePacket;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.model.moderation.InstalledAddons;
import net.labymod.serverapi.core.packet.clientbound.game.moderation.InstalledAddonsRequestPacket;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InstalledAddonsResponsePacket extends IdentifiablePacket {

  private InstalledAddons installedAddons;

  // Constructor for reading
  protected InstalledAddonsResponsePacket() {
    super(null);
  }

  public InstalledAddonsResponsePacket(
      @NotNull InstalledAddonsRequestPacket initiator,
      @NotNull InstalledAddons installedAddons
  ) {
    super(initiator);
    Objects.requireNonNull(installedAddons, "Installed Addons cannot be null");
    this.installedAddons = installedAddons;
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    super.read(reader);
    this.installedAddons = new InstalledAddons(
        reader.readList(reader::readString),
        reader.readList(reader::readString)
    );
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeCollection(this.installedAddons.getEnabled(), writer::writeString);
    writer.writeCollection(this.installedAddons.getDisabled(), writer::writeString);
  }

  public @NotNull InstalledAddons installedAddons() {
    return this.installedAddons;
  }

  @Override
  public String toString() {
    return "InstalledAddonsResponsePacket{" +
        "installedAddons=" + this.installedAddons +
        "} " + super.toString();
  }
}
