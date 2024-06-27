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

package net.labymod.serverapi.server.bungeecord;

import net.labymod.serverapi.api.Protocol;
import net.labymod.serverapi.api.logger.NoOpProtocolPlatformLogger;
import net.labymod.serverapi.api.logger.ProtocolPlatformLogger;
import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.PayloadChannelIdentifier;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.packet.serverbound.login.VersionLoginPacket;
import net.labymod.serverapi.server.bungeecord.event.LabyModPacketReceivedEvent;
import net.labymod.serverapi.server.bungeecord.event.LabyModPacketSentEvent;
import net.labymod.serverapi.server.bungeecord.handler.DefaultVersionLoginPacketHandler;
import net.labymod.serverapi.server.bungeecord.listener.DefaultPlayerQuitListener;
import net.labymod.serverapi.server.bungeecord.listener.DefaultPluginMessageListener;
import net.labymod.serverapi.server.common.AbstractServerLabyModProtocolService;
import net.labymod.serverapi.server.common.JavaProtocolLogger;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class LabyModProtocolService extends AbstractServerLabyModProtocolService<LabyModPlayer> {

  private static final LabyModProtocolService INSTANCE = new LabyModProtocolService();
  private ProtocolPlatformLogger logger = NoOpProtocolPlatformLogger.get();
  private Plugin plugin;

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
   * @param javaPlugin the plugin
   * @throws IllegalStateException if the plugin is already set
   * @throws NullPointerException  if the plugin is null
   */
  public static void initialize(@NotNull Plugin javaPlugin) {
    LabyModProtocolService.get().initializePlugin(javaPlugin);
  }

  @Override
  public void afterPacketHandled(
      @NotNull Protocol protocol,
      @NotNull Packet packet,
      @NotNull UUID sender
  ) {
    if (this.plugin != null) {
      this.plugin.getProxy().getPluginManager().callEvent(new LabyModPacketReceivedEvent(
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
    if (this.plugin != null) {
      this.plugin.getProxy().getPluginManager().callEvent(new LabyModPacketSentEvent(
          this,
          protocol,
          recipient,
          packet
      ));
    }
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

    ProxiedPlayer player = this.plugin.getProxy().getPlayer(recipient);
    if (player == null) {
      return;
    }

    player.sendData(identifier.toString(), writer.toByteArray());
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

  private void initializePlugin(@NotNull Plugin plugin) {
    Objects.requireNonNull(plugin, "Plugin cannot be null");
    if (this.plugin != null) {
      throw new IllegalStateException("The plugin instance is already set");
    }

    this.plugin = plugin;
    this.logger = new JavaProtocolLogger(plugin.getLogger());
    this.registry().addRegisterListener(
        protocol -> plugin.getProxy().getPluginManager().registerListener(
            plugin,
            new DefaultPluginMessageListener(protocol)
        )
    );

    this.initializeManaged();
  }

  private void initializeManaged() {
    this.labyModProtocol.registerHandler(
        VersionLoginPacket.class,
        new DefaultVersionLoginPacketHandler(this, this.plugin)
    );

    this.plugin.getProxy().getPluginManager().registerListener(
        this.plugin,
        new DefaultPlayerQuitListener(this)
    );
  }
}
