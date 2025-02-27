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

package net.labymod.serverapi.server.minestom;

import net.labymod.serverapi.api.Protocol;
import net.labymod.serverapi.api.logger.ProtocolPlatformLogger;
import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.PayloadChannelIdentifier;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.packet.serverbound.login.VersionLoginPacket;
import net.labymod.serverapi.server.common.AbstractServerLabyModProtocolService;
import net.labymod.serverapi.server.common.model.player.AbstractServerLabyModPlayer;
import net.labymod.serverapi.server.minestom.event.LabyModInstalledAddonsUpdateEvent;
import net.labymod.serverapi.server.minestom.event.LabyModPacketReceivedEvent;
import net.labymod.serverapi.server.minestom.event.LabyModPacketSentEvent;
import net.labymod.serverapi.server.minestom.handler.DefaultVersionLoginPacketHandler;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerPluginMessageEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.UUID;

public class LabyModProtocolService extends AbstractServerLabyModProtocolService<LabyModPlayer> {

    private static LabyModProtocolService INSTANCE;
    private final ProtocolPlatformLogger logger = new Slf4jPlatformLogger(LoggerFactory.getLogger(LabyModProtocolService.class));
    private boolean initialized = false;

    private LabyModProtocolService() {
    }

    public static LabyModProtocolService get() {
        return INSTANCE;
    }

    public static void initialize() {
        INSTANCE = new LabyModProtocolService();
        INSTANCE.init();
    }

    private void init() {
        if (this.initialized) {
            throw new IllegalStateException("This protocol service is already initialized.");
        }

      this.labyModProtocol.registerHandler(
                VersionLoginPacket.class,
                new DefaultVersionLoginPacketHandler(this)
        );

        this.registry().addRegisterListener(
                protocol -> {
                    MinecraftServer.getGlobalEventHandler().addListener(PlayerPluginMessageEvent.class, event -> this.onPluginMessage(event, protocol));
                }
        );
        MinecraftServer.getGlobalEventHandler().addListener(PlayerDisconnectEvent.class, event -> {
          this.handlePlayerQuit(event.getPlayer().getUuid());
        });

      this.initialized = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ApiStatus.Internal
    public void handleInstalledAddonsUpdate(AbstractServerLabyModPlayer<?, ?> labyModPlayer) {
        MinecraftServer.getGlobalEventHandler().call(new LabyModInstalledAddonsUpdateEvent(
                this,
                (LabyModPlayer) labyModPlayer
        ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(@NotNull PayloadChannelIdentifier identifier, @NotNull UUID recipient, @NotNull PayloadWriter writer) {
        Objects.requireNonNull(identifier, "Identifier cannot be null");
        Objects.requireNonNull(recipient, "Recipient cannot be null");
        Objects.requireNonNull(writer, "Writer cannot be null");

        Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(recipient);
        if (player == null) {
            return;
        }

        player.sendPluginMessage(identifier.toString(), writer.toByteArray());
    }

    @Override
    public void afterPacketHandled(@NotNull Protocol protocol, @NotNull Packet packet, @NotNull UUID sender) {
        MinecraftServer.getGlobalEventHandler().call(
                new LabyModPacketReceivedEvent(
                        this,
                        protocol,
                    this.getPlayer(sender),
                        packet
                )
        );
    }

    @Override
    public void afterPacketSent(@NotNull Protocol protocol, @NotNull Packet packet, @NotNull UUID recipient) {
        MinecraftServer.getGlobalEventHandler().call(
                new LabyModPacketSentEvent(
                        this,
                        protocol,
                    this.getPlayer(recipient),
                        packet
                )
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ProtocolPlatformLogger logger() {
        return this.logger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInitialized() {
        return true;
    }

    private void onPluginMessage(PlayerPluginMessageEvent event, Protocol protocol) {
        if (!event.getIdentifier().equals(protocol.identifier().toString())) {
            return;
        }

        try {
            PayloadReader reader = new PayloadReader(event.getMessage());
            protocol.handleIncomingPayload(event.getPlayer().getUuid(), reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
