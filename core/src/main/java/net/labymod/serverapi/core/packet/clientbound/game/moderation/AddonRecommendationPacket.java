package net.labymod.serverapi.core.packet.clientbound.game.moderation;

import net.labymod.serverapi.core.model.moderation.RecommendedAddon;
import net.labymod.serverapi.protocol.packet.IdentifiablePacket;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.labymod.serverapi.protocol.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class AddonRecommendationPacket extends IdentifiablePacket {

  private List<RecommendedAddon> addons;

  public AddonRecommendationPacket(@NotNull List<RecommendedAddon> addons) {
    Objects.requireNonNull(addons, "Addons cannot be null");
    this.addons = addons;
  }

  public AddonRecommendationPacket(@NotNull RecommendedAddon... addons) {
    Objects.requireNonNull(addons, "Addons cannot be null");
    this.addons = List.of(addons);
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    super.read(reader);
    this.addons = reader.readList(
        () -> RecommendedAddon.of(reader.readString(), reader.readBoolean())
    );
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    super.write(writer);
    writer.writeCollection(this.addons, addon -> {
      writer.writeString(addon.getNamespace());
      writer.writeBoolean(addon.isRequired());
    });
  }

  public @NotNull List<RecommendedAddon> getAddons() {
    return this.addons;
  }

  @Override
  public String toString() {
    return "AddonRecommendationPacket{" +
        "addons=" + this.addons +
        "} " + super.toString();
  }
}
