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

package net.labymod.serverapi.server.common.handler;

import net.labymod.serverapi.api.packet.PacketHandler;
import net.labymod.serverapi.core.packet.serverbound.game.moderation.AddonStateChangedPacket;
import net.labymod.serverapi.server.common.AbstractServerLabyModProtocolService;
import net.labymod.serverapi.server.common.model.player.AbstractServerLabyModPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DefaultAddonStateChangedPacketHandler
    implements PacketHandler<AddonStateChangedPacket> {

  private final AbstractServerLabyModProtocolService<?> protocolService;

  public DefaultAddonStateChangedPacketHandler(
      AbstractServerLabyModProtocolService<?> protocolService
  ) {
    this.protocolService = protocolService;
  }

  @Override
  public void handle(@NotNull UUID sender, @NotNull AddonStateChangedPacket packet) {
    AbstractServerLabyModPlayer<?, ?> player = this.protocolService.getPlayer(sender);
    if (player == null) {
      return;
    }

    player.installedAddons().updateAddon(packet.addon());
    this.protocolService.handleInstalledAddonsUpdate(player);
  }
}
