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

package net.labymod.serverapi.server.minestom.handler;

import net.labymod.serverapi.api.packet.PacketHandler;
import net.labymod.serverapi.core.packet.serverbound.login.VersionLoginPacket;
import net.labymod.serverapi.server.minestom.LabyModPlayer;
import net.labymod.serverapi.server.minestom.LabyModProtocolService;
import net.labymod.serverapi.server.minestom.event.LabyModPlayerJoinEvent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record DefaultVersionLoginPacketHandler(
        LabyModProtocolService protocolService
) implements PacketHandler<VersionLoginPacket> {
    @Override
    public void handle(@NotNull UUID sender, @NotNull VersionLoginPacket packet) {
        if (protocolService.getPlayer(sender) != null) {
            // player is already connected
            return;
        }

        Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(sender);
        if (player == null) {
            return;
        }

        LabyModPlayer labyModPlayer = new LabyModPlayer(
                protocolService,
                sender,
                player,
                packet.getVersion()
        );

        protocolService.handlePlayerJoin(labyModPlayer);
        MinecraftServer.getGlobalEventHandler().call(new LabyModPlayerJoinEvent(
                protocolService,
                labyModPlayer
        ));
    }
}
