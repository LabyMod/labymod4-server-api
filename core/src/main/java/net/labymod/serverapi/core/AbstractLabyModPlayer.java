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

package net.labymod.serverapi.core;

import net.labymod.serverapi.api.Protocol;
import net.labymod.serverapi.api.model.component.ServerAPIComponent;
import net.labymod.serverapi.api.packet.IdentifiablePacket;
import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.core.integration.LabyModIntegrationPlayer;
import net.labymod.serverapi.core.integration.LabyModProtocolIntegration;
import net.labymod.serverapi.core.model.display.Subtitle;
import net.labymod.serverapi.core.model.feature.DiscordRPC;
import net.labymod.serverapi.core.model.feature.InteractionMenuEntry;
import net.labymod.serverapi.core.model.moderation.Permission;
import net.labymod.serverapi.core.model.supplement.InputPrompt;
import net.labymod.serverapi.core.model.supplement.ServerSwitchPrompt;
import net.labymod.serverapi.core.packet.clientbound.game.display.SubtitlePacket;
import net.labymod.serverapi.core.packet.clientbound.game.display.TabListBannerPacket;
import net.labymod.serverapi.core.packet.clientbound.game.feature.DiscordRPCPacket;
import net.labymod.serverapi.core.packet.clientbound.game.feature.InteractionMenuPacket;
import net.labymod.serverapi.core.packet.clientbound.game.feature.PlayingGameModePacket;
import net.labymod.serverapi.core.packet.clientbound.game.moderation.PermissionPacket;
import net.labymod.serverapi.core.packet.clientbound.game.supplement.InputPromptPacket;
import net.labymod.serverapi.core.packet.clientbound.game.supplement.ServerSwitchPromptPacket;
import net.labymod.serverapi.core.packet.serverbound.game.supplement.InputPromptResponsePacket;
import net.labymod.serverapi.core.packet.serverbound.game.supplement.ServerSwitchPromptResponsePacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class AbstractLabyModPlayer<P extends AbstractLabyModPlayer<?>> {

  protected final UUID uniqueId;
  protected final String labyModVersion;
  private final AbstractLabyModProtocolService protocolService;
  private final List<LabyModIntegrationPlayer> integrationPlayers = new ArrayList<>();
  private Subtitle subtitle;

  protected AbstractLabyModPlayer(
      AbstractLabyModProtocolService protocolService,
      UUID uniqueId,
      String labyModVersion
  ) {
    this.protocolService = protocolService;
    this.uniqueId = uniqueId;
    this.labyModVersion = labyModVersion;

    for (LabyModProtocolIntegration integration : protocolService.integrations) {
      LabyModIntegrationPlayer player = integration.createIntegrationPlayer(this);
      if (player == null) {
        continue;
      }

      LabyModIntegrationPlayer existingPlayer = this.getIntegrationPlayer(player.getClass());
      if (existingPlayer == null) {
        this.integrationPlayers.add(player);
      } else {
        protocolService.logger().warn(
            "Tried to add integration player " + player + " (" + player.getClass().getName()
                + ") to " + uniqueId + " but it's already registered"
        );
      }
    }
  }

  /**
   * Gets the integration player of the provided class
   *
   * @param clazz The class of the integration player
   * @param <T>   The type of the integration player
   * @return The integration player or null if not found
   */
  public <T extends LabyModIntegrationPlayer> @Nullable T getIntegrationPlayer(Class<T> clazz) {
    for (LabyModIntegrationPlayer integrationPlayer : this.integrationPlayers) {
      if (clazz == integrationPlayer.getClass()) {
        return (T) integrationPlayer;
      }
    }

    return null;
  }

  /**
   * @return The unique identifier of the player
   */
  public @NotNull UUID getUniqueId() {
    return this.uniqueId;
  }

  /**
   * @return The LabyMod version of the player
   */
  public @NotNull String getLabyModVersion() {
    return this.labyModVersion;
  }

  /**
   * @return the currently set subtitle
   */
  public @Nullable Subtitle getSubtitle() {
    return this.subtitle;
  }

  /**
   * Sets the subtitle of the player and sends it to all other online players
   *
   * @param component The component to set as subtitle
   */
  public void setSubtitle(@NotNull ServerAPIComponent component) {
    this.setSubtitle(Subtitle.create(this.uniqueId, component));
  }

  public void setSubtitle(@Nullable Subtitle subtitle) {
    if (Objects.equals(this.subtitle, subtitle)) {
      return;
    }

    this.subtitle = subtitle;

    List<Subtitle> subtitles = new ArrayList<>();
    if (subtitle == null) {
      subtitle = Subtitle.create(this.uniqueId, null);
      subtitles.add(subtitle);
    }

    LabyModProtocol labyModProtocol = this.protocolService.labyModProtocol();
    SubtitlePacket packet = new SubtitlePacket(subtitle);
    for (AbstractLabyModPlayer<?> player : this.protocolService.getPlayers()) {
      Subtitle playerSubtitle = player.getSubtitle();
      if (playerSubtitle != null) {
        subtitles.add(playerSubtitle);
      }

      if (player.equals(this)) {
        continue;
      }

      labyModProtocol.sendPacket(player.getUniqueId(), packet);
    }

    if (subtitles.isEmpty()) {
      return;
    }

    this.sendLabyModPacket(new SubtitlePacket(subtitles));
  }

  /**
   * Sets the subtitle of the player and sends it to all other online players
   *
   * @param component The component to set as subtitle
   * @param size      The size of the subtitle
   */
  public void setSubtitle(@NotNull ServerAPIComponent component, double size) {
    this.setSubtitle(Subtitle.create(this.uniqueId, component, size));
  }

  /**
   * Resets the subtitle of the player and sends it to all other online players
   */
  public void resetSubtitle() {
    this.setSubtitle((Subtitle) null);
  }

  /**
   * Sends the provided interaction menu entries to the player
   *
   * @param entries The entries to send
   */
  public void sendInteractionMenuEntries(@NotNull InteractionMenuEntry... entries) {
    this.sendLabyModPacket(new InteractionMenuPacket(entries));
  }

  /**
   * Sends the provided interaction menu entries to the player
   *
   * @param entries The entries to send
   */
  public void sendInteractionMenuEntries(@NotNull List<InteractionMenuEntry> entries) {
    this.sendLabyModPacket(new InteractionMenuPacket(entries));
  }

  /**
   * Sends the provided game mode to the LabyMod Chat friends of the player
   *
   * @param gameMode The game mode to send or {@code null} to unset
   */
  public void sendPlayingGameMode(@Nullable String gameMode) {
    this.sendLabyModPacket(new PlayingGameModePacket(gameMode));
  }

  /**
   * Sends the provided discord rpc to the player
   *
   * @param discordRPC The discord rpc to send
   */
  public void sendDiscordRPC(@NotNull DiscordRPC discordRPC) {
    this.sendLabyModPacket(new DiscordRPCPacket(discordRPC));
  }

  /**
   * Sends the provided icon url as tab list banner to the player
   *
   * @param iconUrl The icon url to send or {@code null} to unset the banner
   */
  public void sendTabListBanner(@Nullable String iconUrl) {
    this.sendLabyModPacket(new TabListBannerPacket(iconUrl));
  }

  /**
   * Sends the provided packet to the player
   *
   * @param packet The packet to send
   */
  public void sendPacket(@NotNull Packet packet) {
    Class<? extends Packet> packetClass = packet.getClass();
    for (Protocol protocol : this.protocolService.registry().getProtocols()) {
      if (protocol.hasPacket(packetClass)) {
        protocol.sendPacket(this.uniqueId, packet);
      }
    }
  }

  /**
   * Sends the provided permissions to the player
   *
   * @param permissions The permissions to send
   */
  public void sendPermissions(@NotNull Permission.StatedPermission... permissions) {
    this.sendLabyModPacket(new PermissionPacket(permissions));
  }

  /**
   * Sends the provided permissions to the player
   *
   * @param permissions The permissions to send
   */
  public void sendPermissions(@NotNull List<Permission.StatedPermission> permissions) {
    this.sendLabyModPacket(new PermissionPacket(permissions));
  }

  /**
   * Opens the provided input prompt for the player
   *
   * @param inputPrompt The input prompt to open
   */
  public void openInputPrompt(@NotNull InputPrompt inputPrompt) {
    this.sendLabyModPacket(new InputPromptPacket(inputPrompt));
  }

  /**
   * Opens the provided input prompt for the player and handle the response via the provided
   * consumer
   *
   * @param inputPrompt      The input prompt to open
   * @param responseConsumer The consumer for the response
   */
  public void openInputPrompt(
      @NotNull InputPrompt inputPrompt,
      @NotNull Consumer<String> responseConsumer
  ) {
    Objects.requireNonNull(responseConsumer, "Response consumer cannot be null");
    this.sendLabyModPacket(
        new InputPromptPacket(inputPrompt),
        InputPromptResponsePacket.class,
        response -> {
          responseConsumer.accept(response.getValue());
          return false;
        }
    );
  }

  /**
   * Opens the provided server switch prompt
   *
   * @param serverSwitchPrompt The server switch prompt to open
   */
  public void openServerSwitchPrompt(@NotNull ServerSwitchPrompt serverSwitchPrompt) {
    this.sendLabyModPacket(new ServerSwitchPromptPacket(serverSwitchPrompt));
  }

  /**
   * Opens the provided server switch prompt and handle the response via the provided consumer
   *
   * @param serverSwitchPrompt The server switch prompt to open
   * @param responseConsumer   The consumer for the response
   */
  public void openServerSwitchPrompt(
      @NotNull ServerSwitchPrompt serverSwitchPrompt,
      @NotNull Consumer<ServerSwitchPromptResponsePacket> responseConsumer
  ) {
    Objects.requireNonNull(responseConsumer, "Response consumer cannot be null");
    this.sendLabyModPacket(
        new ServerSwitchPromptPacket(serverSwitchPrompt),
        ServerSwitchPromptResponsePacket.class,
        response -> {
          responseConsumer.accept(response);
          return false;
        }
    );
  }

  private void sendLabyModPacket(@NotNull Packet packet) {
    this.protocolService.labyModProtocol.sendPacket(this.uniqueId, packet);
  }

  private <T extends IdentifiablePacket> void sendLabyModPacket(
      @NotNull IdentifiablePacket packet,
      @NotNull Class<T> responseClass,
      @NotNull Predicate<T> responseConsumer
  ) {
    this.protocolService.labyModProtocol.sendPacket(
        this.uniqueId,
        packet,
        responseClass,
        responseConsumer
    );
  }
}
