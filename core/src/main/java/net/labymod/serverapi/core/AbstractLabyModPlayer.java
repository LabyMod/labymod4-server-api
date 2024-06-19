package net.labymod.serverapi.core;

import net.labymod.serverapi.api.Protocol;
import net.labymod.serverapi.api.model.component.ServerAPIComponent;
import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.core.integration.LabyModIntegrationPlayer;
import net.labymod.serverapi.core.integration.LabyModProtocolIntegration;
import net.labymod.serverapi.core.model.display.Subtitle;
import net.labymod.serverapi.core.packet.clientbound.game.display.SubtitlePacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

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

  public <T extends LabyModIntegrationPlayer> @Nullable T getIntegrationPlayer(Class<T> clazz) {
    for (LabyModIntegrationPlayer integrationPlayer : this.integrationPlayers) {
      if (clazz == integrationPlayer.getClass()) {
        return (T) integrationPlayer;
      }
    }

    return null;
  }

  public @NotNull UUID getUniqueId() {
    return this.uniqueId;
  }

  public @NotNull String getLabyModVersion() {
    return this.labyModVersion;
  }

  public @Nullable Subtitle getSubtitle() {
    return this.subtitle;
  }

  public void setSubtitle(@NotNull ServerAPIComponent component) {
    this.setSubtitleInternal(Subtitle.create(this.uniqueId, component));
  }

  public void resetSubtitle(Consumer<P> consumer) {
    this.setSubtitleInternal(null);
  }

  public void setSubtitle(@NotNull ServerAPIComponent component, double size) {
    this.setSubtitleInternal(Subtitle.create(this.uniqueId, component, size));
  }

  public void sendPacket(@NotNull Packet packet) {
    Class<? extends Packet> packetClass = packet.getClass();
    for (Protocol protocol : this.protocolService.registry().getProtocols()) {
      if (protocol.hasPacket(packetClass)) {
        protocol.sendPacket(this.uniqueId, packet);
      }
    }
  }

  private void setSubtitleInternal(Subtitle subtitle) {
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

    this.sendPacket(new SubtitlePacket(subtitles));
  }
}
