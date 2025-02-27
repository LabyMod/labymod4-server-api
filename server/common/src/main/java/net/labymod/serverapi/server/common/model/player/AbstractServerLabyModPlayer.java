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

package net.labymod.serverapi.server.common.model.player;

import net.labymod.serverapi.core.AbstractLabyModPlayer;
import net.labymod.serverapi.core.packet.clientbound.game.moderation.InstalledAddonsRequestPacket;
import net.labymod.serverapi.core.packet.serverbound.game.moderation.InstalledAddonsResponsePacket;
import net.labymod.serverapi.server.common.AbstractServerLabyModProtocolService;
import net.labymod.serverapi.server.common.model.addon.InstalledAddonsResponse;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class AbstractServerLabyModPlayer<P extends AbstractServerLabyModPlayer<?, ?>, T>
    extends AbstractLabyModPlayer<P> {

  private final T serverPlayer;
  private final InstalledAddonsResponse installedAddonsResponse;

  protected AbstractServerLabyModPlayer(
      AbstractServerLabyModProtocolService<?> protocolService,
      UUID uniqueId,
      T player,
      String labyModVersion
  ) {
    super(protocolService, uniqueId, labyModVersion);
    this.serverPlayer = player;
    this.installedAddonsResponse = new InstalledAddonsResponse();
  }

  /**
   * @return The server player instance.
   */
  public T getPlayer() {
    return this.serverPlayer;
  }

  /**
   * @return the installed addons of the user. The object is empty until one of the request
   * methods is called.
   */
  public @NotNull InstalledAddonsResponse installedAddons() {
    return this.installedAddonsResponse;
  }

  /**
   * Requests ALL installed addons from the user.
   */
  public void requestInstalledAddons() {
    this.requestInstalledAddons(new ArrayList<>(), null);
  }

  /**
   * Requests ALL installed addons from the user.
   *
   * @param response the response consumer. Called when the response is received.
   */
  public void requestInstalledAddons(Consumer<InstalledAddonsResponse> response) {
    this.requestInstalledAddons(new ArrayList<>(), response);
  }

  /**
   * Requests the installed state of the provided addons.
   *
   * @param addonsToRequest The addons to request the installed state of.
   */
  public void requestInstalledAddons(String... addonsToRequest) {
    this.requestInstalledAddons(Arrays.asList(addonsToRequest), null);
  }

  /**
   * Requests the installed state of the provided addons.
   *
   * @param addonsToRequest The addons to request the installed state of.
   */
  public void requestInstalledAddons(@NotNull List<String> addonsToRequest) {
    this.requestInstalledAddons(addonsToRequest, null);
  }

  /**
   * Requests the installed state of the provided addons.
   *
   * @param response        the response consumer. Called when the response is received.
   * @param addonsToRequest The addons to request the installed state of.
   */
  public void requestInstalledAddons(
      Consumer<InstalledAddonsResponse> response,
      String... addonsToRequest
  ) {
    this.requestInstalledAddons(Arrays.asList(addonsToRequest), response);
  }

  /**
   * Requests the installed state of the provided addons.
   *
   * @param addonsToRequest The addons to request the installed state of.
   * @param response        the response consumer. Called when the response is received.
   */
  public void requestInstalledAddons(
      @NotNull List<String> addonsToRequest,
      Consumer<InstalledAddonsResponse> response
  ) {
    this.installedAddonsResponse.setRequestedAddons(addonsToRequest);
    this.sendLabyModPacket(
        new InstalledAddonsRequestPacket(addonsToRequest),
        InstalledAddonsResponsePacket.class,
        packet -> {
          this.installedAddonsResponse.handleResponse(packet);
          if (response != null) {
            response.accept(this.installedAddonsResponse);
          }

          return false;
        }
    );
  }
}
