package net.labymod.serverapi.server.common.model.addon;

import net.labymod.serverapi.core.model.moderation.InstalledAddons;
import net.labymod.serverapi.core.packet.serverbound.game.moderation.InstalledAddonsResponsePacket;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InstalledAddonsResponse extends InstalledAddons {

  private boolean hasResponse;
  private List<String> requestedAddons = new ArrayList<>();

  public InstalledAddonsResponse() {
    super(new ArrayList<>(), new ArrayList<>());
  }

  public boolean hasResponse() {
    return this.hasResponse;
  }

  public List<String> getRequested() {
    return this.requestedAddons;
  }

  @ApiStatus.Internal
  public void handleResponse(InstalledAddonsResponsePacket packet) {
    this.enabledAddons.addAll(packet.installedAddons().getEnabled());
    this.disabledAddons.addAll(packet.installedAddons().getDisabled());
    this.hasResponse = true;
  }

  @ApiStatus.Internal
  public void setRequestedAddons(@NotNull List<String> requestedAddons) {
    this.requestedAddons = requestedAddons;
    this.enabledAddons.clear();
    this.disabledAddons.clear();
    this.hasResponse = false;
  }

  @ApiStatus.Internal
  public void addAddon(String namespace, boolean enabled) {
    List<String> removeFrom = enabled ? this.disabledAddons : this.enabledAddons;
    List<String> addTo = enabled ? this.enabledAddons : this.disabledAddons;
    removeFrom.remove(namespace);
    if (!addTo.contains(namespace)) {
      addTo.add(namespace);
    }
  }

  @Override
  public String toString() {
    return "InstalledAddonsResponse{" +
        "hasResponse=" + this.hasResponse +
        "} " + super.toString();
  }
}
