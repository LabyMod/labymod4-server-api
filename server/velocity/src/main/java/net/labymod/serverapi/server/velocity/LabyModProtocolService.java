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

package net.labymod.serverapi.server.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import net.labymod.serverapi.api.Protocol;
import net.labymod.serverapi.api.logger.NoOpProtocolPlatformLogger;
import net.labymod.serverapi.api.logger.ProtocolPlatformLogger;
import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.PayloadChannelIdentifier;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.packet.serverbound.login.VersionLoginPacket;
import net.labymod.serverapi.server.common.AbstractServerLabyModProtocolService;
import net.labymod.serverapi.server.velocity.event.LabyModPacketReceivedEvent;
import net.labymod.serverapi.server.velocity.event.LabyModPacketSentEvent;
import net.labymod.serverapi.server.velocity.handler.DefaultVersionLoginPacketHandler;
import net.labymod.serverapi.server.velocity.listener.DefaultDisconnectListener;
import net.labymod.serverapi.server.velocity.listener.DefaultPluginMessageListener;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.UUID;

public class LabyModProtocolService extends AbstractServerLabyModProtocolService<LabyModPlayer> {

  private static final LabyModProtocolService INSTANCE = new LabyModProtocolService();
  private ProtocolPlatformLogger logger = NoOpProtocolPlatformLogger.get();

  private Object plugin;
  private ProxyServer server;

  private LabyModProtocolService() {
    // singleton
  }

  /**
   * @return the instance of the protocol service
   */
  public static LabyModProtocolService get() {
    return INSTANCE;
  }

  /**
   * Initializes the protocol services. Can only be done once.
   *
   * @param plugin the instance of the plugin main class
   * @param server the proxy server instance
   * @param logger the logger instance
   * @throws IllegalStateException if the plugin is already set
   * @throws NullPointerException  if the plugin is null
   */
  public static void initialize(
      @NotNull Object plugin,
      @NotNull ProxyServer server,
      @NotNull Logger logger
  ) {
    LabyModProtocolService.get().initializePlugin(plugin, server, logger);
  }

  @Override
  public void afterPacketHandled(
      @NotNull Protocol protocol,
      @NotNull Packet packet,
      @NotNull UUID sender
  ) {
    if (this.server != null) {
      this.server.getEventManager().fire(new LabyModPacketReceivedEvent(
          this,
          protocol,
          sender,
          packet
      ));
    }
  }

  @Override
  public void afterPacketSent(
      @NotNull Protocol protocol,
      @NotNull Packet packet,
      @NotNull UUID recipient
  ) {
    if (this.server != null) {
      this.server.getEventManager().fire(new LabyModPacketSentEvent(
          this,
          protocol,
          recipient,
          packet
      ));
    }
  }

  public @NotNull MinecraftChannelIdentifier mapToVelocityChannelIdentifier(
      @NotNull PayloadChannelIdentifier identifier
  ) {
    Objects.requireNonNull(identifier, "Identifier cannot be null");
    return MinecraftChannelIdentifier.create(identifier.getNamespace(), identifier.getPath());
  }

  public @NotNull PayloadChannelIdentifier mapFromVelocityChannelIdentifier(
      @NotNull MinecraftChannelIdentifier identifier
  ) {
    Objects.requireNonNull(identifier, "Identifier cannot be null");
    return PayloadChannelIdentifier.create(identifier.getNamespace(), identifier.getName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void send(
      @NotNull PayloadChannelIdentifier identifier,
      @NotNull UUID recipient,
      @NotNull PayloadWriter writer
  ) {
    Objects.requireNonNull(identifier, "Identifier cannot be null");
    Objects.requireNonNull(recipient, "Recipient cannot be null");
    Objects.requireNonNull(writer, "Writer cannot be null");
    if (this.plugin == null) {
      throw new IllegalStateException("The plugin instance is not set yet");
    }

    this.server.getPlayer(recipient).ifPresent(player -> player.sendPluginMessage(
        this.mapToVelocityChannelIdentifier(identifier),
        writer.toByteArray()
    ));
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
    return this.plugin != null;
  }

  private void initializePlugin(
      @NotNull Object plugin,
      @NotNull ProxyServer server,
      @NotNull Logger logger
  ) {
    Objects.requireNonNull(plugin, "Plugin cannot be null");
    Objects.requireNonNull(server, "Server cannot be null");
    Objects.requireNonNull(logger, "Logger cannot be null");
    if (this.plugin != null) {
      throw new IllegalStateException("The plugin instance is already set");
    }

    this.server = server;
    this.plugin = plugin;
    this.logger = new Slf4jPlatformLogger(logger);
    this.registry().addRegisterListener(protocol -> {
          this.server.getChannelRegistrar().register(
              this.mapToVelocityChannelIdentifier(protocol.identifier())
          );

          this.server.getEventManager().register(
              plugin,
              new DefaultPluginMessageListener(this, protocol)
          );
        }
    );

    this.initializeManaged();
  }

  private void initializeManaged() {
    this.labyModProtocol.registerHandler(
        VersionLoginPacket.class,
        new DefaultVersionLoginPacketHandler(this, this.players, this.server)
    );

    this.server.getEventManager().register(
        this.plugin,
        new DefaultDisconnectListener(this)
    );
  }
}
