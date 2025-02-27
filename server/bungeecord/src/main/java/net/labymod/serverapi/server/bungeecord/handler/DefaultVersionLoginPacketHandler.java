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

package net.labymod.serverapi.server.bungeecord.handler;

import net.labymod.serverapi.api.packet.PacketHandler;
import net.labymod.serverapi.core.packet.serverbound.login.VersionLoginPacket;
import net.labymod.serverapi.server.bungeecord.LabyModPlayer;
import net.labymod.serverapi.server.bungeecord.LabyModProtocolService;
import net.labymod.serverapi.server.bungeecord.event.LabyModPlayerJoinEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DefaultVersionLoginPacketHandler implements PacketHandler<VersionLoginPacket> {

  private final LabyModProtocolService protocolService;
  private final Plugin plugin;

  public DefaultVersionLoginPacketHandler(
      LabyModProtocolService protocolService,
      Plugin plugin
  ) {
    this.protocolService = protocolService;
    this.plugin = plugin;
  }

  @Override
  public void handle(@NotNull UUID uuid, @NotNull VersionLoginPacket versionLoginPacket) {
    if (this.protocolService.getPlayer(uuid) != null) {
      // warn
      return;
    }

    ProxyServer proxy = this.plugin.getProxy();
    final ProxiedPlayer player = proxy.getPlayer(uuid);
    if (player == null) {
      return;
    }

    final LabyModPlayer labyModPlayer = new LabyModPlayer(
        this.protocolService,
        uuid,
        player,
        versionLoginPacket.getVersion()
    );

    this.protocolService.handlePlayerJoin(labyModPlayer);
    proxy.getPluginManager().callEvent(new LabyModPlayerJoinEvent(
        this.protocolService,
        labyModPlayer
    ));
  }
}
