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

package net.labymod.serverapi.core;

import net.labymod.serverapi.api.Protocol;
import net.labymod.serverapi.api.model.component.ServerAPIComponent;
import net.labymod.serverapi.api.packet.IdentifiablePacket;
import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.core.integration.LabyModIntegrationPlayer;
import net.labymod.serverapi.core.integration.LabyModProtocolIntegration;
import net.labymod.serverapi.core.model.display.EconomyDisplay;
import net.labymod.serverapi.core.model.display.Subtitle;
import net.labymod.serverapi.core.model.display.TabListFlag;
import net.labymod.serverapi.core.model.feature.DiscordRPC;
import net.labymod.serverapi.core.model.feature.InteractionMenuEntry;
import net.labymod.serverapi.core.model.moderation.Permission;
import net.labymod.serverapi.core.model.moderation.RecommendedAddon;
import net.labymod.serverapi.core.model.supplement.InputPrompt;
import net.labymod.serverapi.core.model.supplement.ServerSwitchPrompt;
import net.labymod.serverapi.core.packet.clientbound.game.display.EconomyDisplayPacket;
import net.labymod.serverapi.core.packet.clientbound.game.display.SubtitlePacket;
import net.labymod.serverapi.core.packet.clientbound.game.display.TabListBannerPacket;
import net.labymod.serverapi.core.packet.clientbound.game.display.TabListFlagPacket;
import net.labymod.serverapi.core.packet.clientbound.game.feature.DiscordRPCPacket;
import net.labymod.serverapi.core.packet.clientbound.game.feature.InteractionMenuPacket;
import net.labymod.serverapi.core.packet.clientbound.game.feature.PlayingGameModePacket;
import net.labymod.serverapi.core.packet.clientbound.game.feature.marker.AddMarkerPacket;
import net.labymod.serverapi.core.packet.clientbound.game.feature.marker.MarkerPacket;
import net.labymod.serverapi.core.packet.clientbound.game.moderation.AddonDisablePacket;
import net.labymod.serverapi.core.packet.clientbound.game.moderation.AddonRecommendationPacket;
import net.labymod.serverapi.core.packet.clientbound.game.moderation.PermissionPacket;
import net.labymod.serverapi.core.packet.clientbound.game.supplement.InputPromptPacket;
import net.labymod.serverapi.core.packet.clientbound.game.supplement.ServerSwitchPromptPacket;
import net.labymod.serverapi.core.packet.serverbound.game.moderation.AddonRecommendationResponsePacket;
import net.labymod.serverapi.core.packet.serverbound.game.supplement.InputPromptResponsePacket;
import net.labymod.serverapi.core.packet.serverbound.game.supplement.ServerSwitchPromptResponsePacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class AbstractLabyModPlayer<P extends AbstractLabyModPlayer<?>> {

  protected final UUID uniqueId;
  protected final String labyModVersion;
  private final AbstractLabyModProtocolService protocolService;
  private final List<LabyModIntegrationPlayer> integrationPlayers = new ArrayList<>();
  private final Map<String, EconomyDisplay> economies = new HashMap<>(2);
  private final List<String> disabledAddons = new ArrayList<>();

  private Subtitle subtitle;
  private TabListFlag flag;

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
  public @NotNull Subtitle subtitle() {
    if (this.subtitle == null) {
      this.subtitle = Subtitle.create(this.uniqueId, null);
    }

    return this.subtitle;
  }

  /**
   * @return whether the player has a subtitle set
   */
  public boolean hasSubtitle() {
    return this.subtitle != null && this.subtitle.getText() != null;
  }

  /**
   * Updates the text of the subtitle
   *
   * @param text The text to set as subtitle
   */
  public void updateSubtitle(@NotNull ServerAPIComponent text) {
    this.updateSubtitle(subtitle -> subtitle.text(text));
  }

  /**
   * Sets the subtitle of the player and sends it to all other online players
   *
   * @param text The component to set as subtitle
   * @param size The size of the subtitle
   */
  public void updateSubtitle(@NotNull ServerAPIComponent text, double size) {
    this.updateSubtitle(subtitle -> subtitle.text(text).size(size));
  }

  /**
   * Resets the subtitle of the player and sends it to all other online players
   */
  public void resetSubtitle() {
    this.updateSubtitle(subtitle -> subtitle.text(null));
  }

  /**
   * Updates or creates a new subtitle and calls the consumer
   *
   * @param consumer The consumer to call
   */
  public void updateSubtitle(Consumer<Subtitle> consumer) {
    if (this.subtitle == null) {
      this.subtitle = Subtitle.create(this.uniqueId, null);
    }

    ServerAPIComponent prevText = this.subtitle.getText();
    double prevSize = this.subtitle.getSize();
    consumer.accept(this.subtitle);
    if (this.subtitle.getSize() == prevSize && Objects.equals(prevText, this.subtitle.getText())) {
      return;
    }

    LabyModProtocol labyModProtocol = this.protocolService.labyModProtocol();
    SubtitlePacket packet = new SubtitlePacket(this.subtitle);
    for (AbstractLabyModPlayer<?> player : this.protocolService.getPlayers()) {
      labyModProtocol.sendPacket(player.getUniqueId(), packet);
    }
  }

  /**
   * Forcefully disables the provided addons.
   *
   * @param addonsToDisable the addons to disable
   */
  public void disableAddons(List<String> addonsToDisable) {
    for (String addon : addonsToDisable) {
      if (!this.disabledAddons.contains(addon)) {
        this.disabledAddons.add(addon);
      }
    }

    this.sendPacket(AddonDisablePacket.disable(addonsToDisable));
  }

  /**
   * Forcefully disables the provided addons.
   *
   * @param addonsToDisable the addons to disable
   */
  public void disableAddons(String... addonsToDisable) {
    this.disableAddons(Arrays.asList(addonsToDisable));
  }

  /**
   * Reverts the forced disable state for the provided addons
   *
   * @param addonsToRevert the addons to revert
   */
  public void revertDisabledAddons(List<String> addonsToRevert) {
    for (String addon : addonsToRevert) {
      this.disabledAddons.remove(addon);
    }

    this.sendPacket(AddonDisablePacket.revert(addonsToRevert));
  }

  /**
   * Reverts the forced disable state for the provided addons
   *
   * @param addonsToRevert the addons to revert
   */
  public void revertDisabledAddons(String... addonsToRevert) {
    this.revertDisabledAddons(Arrays.asList(addonsToRevert));
  }

  /**
   * Reverts the forced disable state for all addons disabled via {@link #disableAddons(List)}
   */
  public void revertDisabledAddons() {
    this.sendPacket(AddonDisablePacket.revert(this.disabledAddons));
    this.disabledAddons.clear();
  }

  /**
   * Gets all forcefully disabled addons disabled via {@link #disableAddons(List)}
   *
   * @return the disabled addons
   */
  public List<String> getDisabledAddons() {
    return this.disabledAddons;
  }

  /**
   * Gives LabyMod the instruction to send placed markers by the player as
   * {@link net.labymod.serverapi.core.packet.serverbound.game.feature.marker.ClientAddMarkerPacket}
   *
   * @param sendType the way the client will send markers. See {@link MarkerPacket.MarkerSendType}
   */
  public void sendMarkerSendType(MarkerPacket.MarkerSendType sendType) {
    this.sendPacket(new MarkerPacket(sendType));
  }

  /**
   * Sends a marker to the player
   *
   * @param sender The unique identifier of the sender
   * @param x      The x coordinate of the marker
   * @param y      The y coordinate of the marker
   * @param z      The z coordinate of the marker
   * @param large  Whether the marker should be large
   * @param target The unique identifier of the target player or null
   */
  public void sendMarker(
      @NotNull UUID sender,
      int x,
      int y,
      int z,
      boolean large,
      @Nullable UUID target
  ) {
    this.sendPacket(new AddMarkerPacket(sender, x, y, z, large, target));
  }

  /**
   * @return the tab list flag of the player or null if not set
   */
  public @Nullable TabListFlag getTabListFlag() {
    return this.flag;
  }

  /**
   * Sets the country code for the
   * {@link net.labymod.serverapi.core.packet.clientbound.game.display.TabListFlagPacket}. This
   * will send the country code to every player on the server.
   *
   * @param countryCode the country code to send
   */
  public void setTabListFlag(@NotNull TabListFlag.TabListFlagCountryCode countryCode) {
    this.flag = TabListFlag.create(this.uniqueId, countryCode);
    LabyModProtocol labyModProtocol = this.protocolService.labyModProtocol();
    TabListFlagPacket packet = new TabListFlagPacket(this.flag);
    for (AbstractLabyModPlayer<?> player : this.protocolService.getPlayers()) {
      labyModProtocol.sendPacket(player.getUniqueId(), packet);
    }
  }

  /**
   * @return The display for the bank economy
   */
  public @NotNull EconomyDisplay bankEconomy() {
    return this.economies.computeIfAbsent("bank", EconomyDisplay::new);
  }

  /**
   * @return The display for the cash economy
   */
  public @NotNull EconomyDisplay cashEconomy() {
    return this.economies.computeIfAbsent("cash", EconomyDisplay::new);
  }

  /**
   * Gets the economy display for the provided key
   *
   * @param key The key of the economy display
   * @return The economy display or null if not found
   */
  public @Nullable EconomyDisplay getEconomy(@NotNull String key) {
    Objects.requireNonNull(key, "Key cannot be null");
    return this.economies.get(key);
  }

  /**
   * Gets or creates the economy display for the provided key, calls the consumer and sends
   * {@link EconomyDisplayPacket} to the player
   *
   * @param key      The key of the economy display
   * @param consumer The consumer to call
   */
  public void updateEconomy(@NotNull String key, @NotNull Consumer<EconomyDisplay> consumer) {
    Objects.requireNonNull(key, "Key cannot be null");
    Objects.requireNonNull(consumer, "Consumer cannot be null");

    EconomyDisplay display = this.economies.computeIfAbsent(key, EconomyDisplay::new);
    consumer.accept(display);
    this.sendLabyModPacket(new EconomyDisplayPacket(display));
  }

  /**
   * Gets or creates the display for the bank economy, calls the consumer and sends
   * {@link EconomyDisplayPacket} to the player
   *
   * @param consumer The consumer to call
   */
  public void updateBankEconomy(@NotNull Consumer<EconomyDisplay> consumer) {
    this.updateEconomy("bank", consumer);
  }

  /**
   * Gets or creates the display for the cash economy, calls the consumer and sends
   * {@link EconomyDisplayPacket} to the player
   *
   * @param consumer The consumer to call
   */
  public void updateCashEconomy(@NotNull Consumer<EconomyDisplay> consumer) {
    this.updateEconomy("cash", consumer);
  }

  /**
   * Sends the provided economy display to the player and stores it, so that it can be accessed
   * via {@link #getEconomy(String)}
   *
   * @param display The economy display to send
   */
  public void sendEconomy(EconomyDisplay display) {
    this.economies.put(display.getKey(), display);
    this.sendLabyModPacket(new EconomyDisplayPacket(display));
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

  /**
   * Sends the provided recommended addons to the player
   *
   * @param addons The recommended addons
   */
  public void sendAddonRecommendations(@NotNull List<RecommendedAddon> addons) {
    this.sendLabyModPacket(new AddonRecommendationPacket(addons));
  }

  /**
   * Sends the provided recommended addons to the player
   *
   * @param addons The recommended addons
   */
  public void sendAddonRecommendations(@NotNull RecommendedAddon... addons) {
    this.sendLabyModPacket(new AddonRecommendationPacket(addons));
  }

  /**
   * Sends the provided recommended addons to the player and handle the response via the provided
   * consumer
   *
   * @param responseConsumer The consumer for the response
   * @param addons           The recommended addons
   */
  public void sendAddonRecommendations(
      @NotNull Consumer<AddonRecommendationResponsePacket> responseConsumer,
      @NotNull RecommendedAddon... addons
  ) {
    this.sendAddonRecommendations(Arrays.asList(addons), responseConsumer);
  }

  /**
   * Sends the provided recommended addons to the player and handle the response via the provided
   * consumer
   *
   * @param addons           The recommended addons
   * @param responseConsumer The consumer for the response
   */
  public void sendAddonRecommendations(
      @NotNull List<RecommendedAddon> addons,
      @NotNull Consumer<AddonRecommendationResponsePacket> responseConsumer
  ) {
    Objects.requireNonNull(responseConsumer, "Response consumer cannot be null");
    this.sendLabyModPacket(
        new AddonRecommendationPacket(addons),
        AddonRecommendationResponsePacket.class,
        response -> {
          responseConsumer.accept(response);
          return response.isInitial();
        }
    );
  }

  protected void sendLabyModPacket(@NotNull Packet packet) {
    this.protocolService.labyModProtocol.sendPacket(this.uniqueId, packet);
  }

  protected <T extends IdentifiablePacket> void sendLabyModPacket(
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
