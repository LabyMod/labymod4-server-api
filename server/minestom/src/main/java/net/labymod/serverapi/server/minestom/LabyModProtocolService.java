package net.labymod.serverapi.server.minestom;

import net.labymod.serverapi.api.Protocol;
import net.labymod.serverapi.api.logger.NoOpProtocolPlatformLogger;
import net.labymod.serverapi.api.logger.ProtocolPlatformLogger;
import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.PayloadChannelIdentifier;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.AbstractLabyModProtocolService;
import net.labymod.serverapi.core.packet.serverbound.login.VersionLoginPacket;
import net.labymod.serverapi.server.common.AbstractServerLabyModProtocolService;
import net.labymod.serverapi.server.common.JavaProtocolLogger;
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

import java.util.Objects;
import java.util.UUID;

public class LabyModProtocolService extends AbstractServerLabyModProtocolService<LabyModPlayer> {

    private static final LabyModProtocolService INSTANCE = new LabyModProtocolService();
    private final ProtocolPlatformLogger logger = NoOpProtocolPlatformLogger.get();
    private boolean initialized = false;

    private LabyModProtocolService() {
    }

    public static LabyModProtocolService get() {
        return INSTANCE;
    }

    public void init() {
        if (initialized) {
            throw new IllegalStateException("This protocol service is already initialized.");
        }

        labyModProtocol.registerHandler(
                VersionLoginPacket.class,
                new DefaultVersionLoginPacketHandler(this)
        );

        this.registry().addRegisterListener(
                protocol -> MinecraftServer.getGlobalEventHandler().addListener(PlayerPluginMessageEvent.class, event -> onPluginMessage(event, protocol))
        );
        MinecraftServer.getGlobalEventHandler().addListener(PlayerDisconnectEvent.class, event -> {
            handlePlayerQuit(event.getPlayer().getUuid());
        });

        initialized = true;
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
                        getPlayer(sender),
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
                        getPlayer(recipient),
                        packet
                )
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ProtocolPlatformLogger logger() {
        return logger;
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
