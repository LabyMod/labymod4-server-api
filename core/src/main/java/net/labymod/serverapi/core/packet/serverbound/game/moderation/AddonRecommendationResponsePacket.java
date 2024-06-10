package net.labymod.serverapi.core.packet.serverbound.game.moderation;

import net.labymod.serverapi.core.packet.clientbound.game.moderation.AddonRecommendationPacket;
import net.labymod.serverapi.protocol.packet.IdentifiablePacket;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.labymod.serverapi.protocol.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AddonRecommendationResponsePacket extends IdentifiablePacket {

  private boolean initial;
  private boolean allInstalled;
  private List<String> missingAddons;
  private List<String> installedAddons;

  // Constructor for reading
  protected AddonRecommendationResponsePacket() {
    super(null);
  }

  public AddonRecommendationResponsePacket(
      @NotNull AddonRecommendationPacket initiator,
      boolean initial,
      boolean allInstalled,
      @NotNull List<String> missingAddons,
      @NotNull List<String> installedAddons
  ) {
    super(initiator);
    this.initial = initial;
    this.allInstalled = allInstalled;
    this.missingAddons = missingAddons;
    this.installedAddons = installedAddons;
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    super.read(reader);
    this.initial = reader.readBoolean();
    this.allInstalled = reader.readBoolean();
    this.missingAddons = reader.readList(reader::readString);
    this.installedAddons = reader.readList(reader::readString);
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    super.write(writer);
    writer.writeBoolean(this.initial);
    writer.writeBoolean(this.allInstalled);
    writer.writeCollection(this.missingAddons, writer::writeString);
    writer.writeCollection(this.installedAddons, writer::writeString);
  }

  /**
   * @return {@code true} if this response is the initial one, otherwise {@code false}
   */
  public boolean isInitial() {
    return this.initial;
  }

  /**
   * @return {@code true} if all recommended & required addons are installed, otherwise {@code
   * false}
   */
  public boolean isAllInstalled() {
    return this.allInstalled;
  }

  /**
   * @return a list with the namespaces of all missing addons
   */
  public @NotNull List<String> getMissingAddons() {
    return this.missingAddons;
  }

  /**
   * @return a list with the namespaces of all installed addons (based on the initial
   * {@link AddonRecommendationPacket} packet)
   */
  public @NotNull List<String> getInstalledAddons() {
    return this.installedAddons;
  }
}
