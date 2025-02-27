/*
 * MIT License
 *
 * Copyright (c) 2025 LabyMedia GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.labymod.serverapi.server.common.model.addon;

import net.labymod.serverapi.core.model.moderation.InstalledAddon;
import net.labymod.serverapi.core.packet.serverbound.game.moderation.InstalledAddonsResponsePacket;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstalledAddonsResponse {

  private final Map<String, InstalledAddon> installedAddons = new HashMap<>();
  private boolean hasResponse;
  private boolean hasRequested;
  private List<String> requestedAddons = new ArrayList<>();

  public boolean hasResponse() {
    return this.hasResponse;
  }

  public boolean hasRequested() {
    return this.hasRequested;
  }

  public List<String> getRequested() {
    return this.requestedAddons;
  }

  public @Nullable InstalledAddon get(String namespace) {
    return this.installedAddons.get(namespace);
  }

  public boolean isInstalled(String namespace) {
    return this.get(namespace) != null;
  }

  public @NotNull Collection<InstalledAddon> getInstalledAddons() {
    return this.installedAddons.values();
  }

  public boolean isEnabled(String namespace) {
    InstalledAddon installedAddon = this.get(namespace);
    return installedAddon != null && installedAddon.isEnabled();
  }

  public boolean isDisabled(String namespace) {
    InstalledAddon installedAddon = this.get(namespace);
    return installedAddon != null && !installedAddon.isEnabled();
  }

  public @Nullable InstalledAddon.AddonVersion getVersion(String namespace) {
    InstalledAddon installedAddon = this.get(namespace);
    return installedAddon != null ? installedAddon.getVersion() : null;
  }

  public boolean isLocal(String namespace) {
    InstalledAddon installedAddon = this.get(namespace);
    return installedAddon != null && installedAddon.isLocal();
  }

  @ApiStatus.Internal
  public void handleResponse(InstalledAddonsResponsePacket packet) {
    for (InstalledAddon installedAddon : packet.getInstalledAddons()) {
      this.installedAddons.put(installedAddon.getNamespace(), installedAddon);
    }

    this.hasResponse = true;
  }

  @ApiStatus.Internal
  public void setRequestedAddons(@NotNull List<String> requestedAddons) {
    if (this.hasRequested) {
      if (this.requestedAddons.isEmpty()) {
        // Server already requested updates on all addons, no need to specify further
        return;
      }

      if (requestedAddons.isEmpty()) {
        // Server requested updates on all addons
        this.requestedAddons.clear();
        return;
      }

      // Server requested updates on specific addons
      boolean addedRequest = false;
      for (String requestedAddon : requestedAddons) {
        if (!this.requestedAddons.contains(requestedAddon)) {
          this.requestedAddons.add(requestedAddon);
          addedRequest = true;
        }
      }

      if (addedRequest) {
        return;
      }
    }

    this.hasRequested = true;
    this.requestedAddons = requestedAddons;
  }

  @ApiStatus.Internal
  public void updateAddon(InstalledAddon installedAddon) {
    this.installedAddons.put(installedAddon.getNamespace(), installedAddon);
  }

  @Override
  public String toString() {
    return "InstalledAddonsResponse{" +
        "installedAddons=" + this.installedAddons +
        ", hasResponse=" + this.hasResponse +
        ", hasRequested=" + this.hasRequested +
        ", requestedAddons=" + this.requestedAddons +
        '}';
  }
}
