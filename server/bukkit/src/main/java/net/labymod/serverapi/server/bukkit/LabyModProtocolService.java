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

package net.labymod.serverapi.server.bukkit;

import net.labymod.serverapi.api.Protocol;
import net.labymod.serverapi.api.logger.NoOpProtocolPlatformLogger;
import net.labymod.serverapi.api.logger.ProtocolPlatformLogger;
import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.PayloadChannelIdentifier;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.packet.serverbound.login.VersionLoginPacket;
import net.labymod.serverapi.server.bukkit.event.LabyModPacketReceivedEvent;
import net.labymod.serverapi.server.bukkit.event.LabyModPacketSentEvent;
import net.labymod.serverapi.server.bukkit.handler.DefaultVersionLoginPacketHandler;
import net.labymod.serverapi.server.bukkit.listener.DefaultPlayerQuitListener;
import net.labymod.serverapi.server.bukkit.listener.DefaultPluginMessageListener;
import net.labymod.serverapi.server.common.AbstractServerLabyModProtocolService;
import net.labymod.serverapi.server.common.JavaProtocolLogger;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class LabyModProtocolService extends AbstractServerLabyModProtocolService<LabyModPlayer> {

  private static final LabyModProtocolService INSTANCE = new LabyModProtocolService();
  private ProtocolPlatformLogger logger = NoOpProtocolPlatformLogger.get();
  private JavaPlugin plugin;

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
  public static void initialize(@NotNull JavaPlugin javaPlugin) {
    LabyModProtocolService.get().initializePlugin(javaPlugin);
  }

  @Override
  public void afterPacketHandled(
      @NotNull Protocol protocol,
      @NotNull Packet packet,
      @NotNull UUID sender
  ) {
    if (this.plugin != null) {
      this.plugin.getServer().getPluginManager().callEvent(new LabyModPacketReceivedEvent(
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
      this.plugin.getServer().getPluginManager().callEvent(new LabyModPacketSentEvent(
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

    Player player = this.plugin.getServer().getPlayer(recipient);
    if (player == null) {
      return;
    }

    player.sendPluginMessage(
        this.plugin,
        identifier.toString(),
        writer.toByteArray()
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
    return this.plugin != null;
  }

  private void initializePlugin(@NotNull JavaPlugin javaPlugin) {
    Objects.requireNonNull(javaPlugin, "JavaPlugin cannot be null");
    if (this.plugin != null) {
      throw new IllegalStateException("The plugin instance is already set");
    }

    this.plugin = javaPlugin;
    this.logger = new JavaProtocolLogger(javaPlugin.getLogger());
    Messenger messenger = this.plugin.getServer().getMessenger();
    this.registry().addRegisterListener(protocol -> {
      String identifier = protocol.identifier().toString();
      messenger.registerOutgoingPluginChannel(this.plugin, identifier);
      messenger.registerIncomingPluginChannel(this.plugin, identifier,
          new DefaultPluginMessageListener(protocol));
    });

    this.initializeManaged();
  }

  private void initializeManaged() {
    this.labyModProtocol.registerHandler(
        VersionLoginPacket.class,
        new DefaultVersionLoginPacketHandler(this, this.players, this.plugin)
    );

    this.plugin.getServer().getPluginManager().registerEvents(
        new DefaultPlayerQuitListener(this.players),
        this.plugin
    );
  }
}
