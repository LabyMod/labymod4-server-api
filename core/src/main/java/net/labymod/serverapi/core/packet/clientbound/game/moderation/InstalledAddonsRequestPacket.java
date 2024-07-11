package net.labymod.serverapi.core.packet.clientbound.game.moderation;

import net.labymod.serverapi.api.packet.IdentifiablePacket;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class InstalledAddonsRequestPacket extends IdentifiablePacket {

  private List<String> addonsToRequest;

  /**
   * Creates a packet that requests the installed & enabled state of the provided addons.
   *
   * @param addonsToRequest the addons to request
   */
  public InstalledAddonsRequestPacket(@NotNull List<String> addonsToRequest) {
    Objects.requireNonNull(addonsToRequest, "Addons cannot be null");
    this.addonsToRequest = addonsToRequest;
  }

  /**
   * Creates a packet that requests the installed & enabled state of the provided addons.
   *
   * @param addonsToRequest the addons to request
   */
  public InstalledAddonsRequestPacket(@NotNull String... addonsToRequest) {
    this(List.of(addonsToRequest));
  }

  /**
   * Creates a packet that requests the installed & enabled state of ALL addons.
   */
  public InstalledAddonsRequestPacket() {
    this(List.of());
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    super.read(reader);
    this.addonsToRequest = reader.readList(reader::readString);
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    super.write(writer);
    writer.writeCollection(this.addonsToRequest, writer::writeString);
  }

  public @NotNull List<String> getAddonsToRequest() {
    return this.addonsToRequest;
  }

  public boolean requestsAllAddons() {
    return this.addonsToRequest.isEmpty();
  }

  @Override
  public String toString() {
    return "EnabledAddonsRequestPacket{" +
        "addonsToRequest=" + this.addonsToRequest +
        "} " + super.toString();
  }
}
