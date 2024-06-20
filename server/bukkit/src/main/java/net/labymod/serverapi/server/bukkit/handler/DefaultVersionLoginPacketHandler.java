/*
 * MIT License
 *
 * Copyright (c) 2024 LabyMedia GmbH
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

package net.labymod.serverapi.server.bukkit.handler;

import net.labymod.serverapi.api.packet.PacketHandler;
import net.labymod.serverapi.core.packet.serverbound.login.VersionLoginPacket;
import net.labymod.serverapi.server.bukkit.LabyModPlayer;
import net.labymod.serverapi.server.bukkit.LabyModProtocolService;
import net.labymod.serverapi.server.bukkit.event.LabyModPlayerJoinEvent;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class DefaultVersionLoginPacketHandler implements PacketHandler<VersionLoginPacket> {

  private final LabyModProtocolService protocolService;
  private final Map<UUID, LabyModPlayer> players;
  private final JavaPlugin javaPlugin;

  public DefaultVersionLoginPacketHandler(
      LabyModProtocolService protocolService,
      Map<UUID, LabyModPlayer> players,
      JavaPlugin javaPlugin
  ) {
    this.protocolService = protocolService;
    this.players = players;
    this.javaPlugin = javaPlugin;
  }

  @Override
  public void handle(@NotNull UUID uuid, @NotNull VersionLoginPacket versionLoginPacket) {
    final LabyModPlayer existingLabyModPlayer = this.players.get(uuid);
    if (existingLabyModPlayer != null) {
      // warn
      return;
    }

    Server server = this.javaPlugin.getServer();
    final Player player = server.getPlayer(uuid);
    if (player == null) {
      return;
    }

    final LabyModPlayer labyModPlayer = new LabyModPlayer(
        this.protocolService,
        uuid,
        player,
        versionLoginPacket.getVersion()
    );

    this.players.put(uuid, labyModPlayer);
    server.getPluginManager().callEvent(new LabyModPlayerJoinEvent(
        this.protocolService,
        labyModPlayer
    ));
  }
}
