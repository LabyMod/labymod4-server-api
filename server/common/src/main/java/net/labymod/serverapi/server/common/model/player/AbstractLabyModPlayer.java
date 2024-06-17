package net.labymod.serverapi.server.common.model.player;

import net.labymod.serverapi.api.Protocol;
import net.labymod.serverapi.api.model.component.ServerAPIComponent;
import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.core.LabyModProtocol;
import net.labymod.serverapi.core.model.display.Subtitle;
import net.labymod.serverapi.core.packet.clientbound.game.display.SubtitlePacket;
import net.labymod.serverapi.server.common.AbstractServerLabyModProtocolService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class AbstractLabyModPlayer<T> {

  private final AbstractServerLabyModProtocolService<?> protocolService;
  private final UUID uniqueId;
  private final T serverPlayer;
  private final String labyModVersion;

  private Subtitle subtitle;

  protected AbstractLabyModPlayer(
      AbstractServerLabyModProtocolService<?> protocolService,
      UUID uniqueId,
      T player,
      String labyModVersion
  ) {
    this.protocolService = protocolService;
    this.uniqueId = uniqueId;
    this.serverPlayer = player;
    this.labyModVersion = labyModVersion;
  }

  public @NotNull UUID getUniqueId() {
    return this.uniqueId;
  }

  public @NotNull T getPlayer() {
    return this.serverPlayer;
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

  public void resetSubtitle() {
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
    this.protocolService.forEachPlayer(player -> {
      Subtitle playerSubtitle = player.getSubtitle();
      if (playerSubtitle != null) {
        subtitles.add(playerSubtitle);
      }

      if (player.equals(this)) {
        return;
      }

      labyModProtocol.sendPacket(player.getUniqueId(), packet);
    });

    if (subtitles.isEmpty()) {
      return;
    }

    this.sendPacket(new SubtitlePacket(subtitles));
  }

  @Override
  public String toString() {
    return "AbstractLabyModPlayer{" +
        "uniqueId=" + this.uniqueId +
        ", labyModVersion='" + this.labyModVersion + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof AbstractLabyModPlayer<?>)) {
      return false;
    }

    AbstractLabyModPlayer<?> that = (AbstractLabyModPlayer<?>) o;
    return Objects.equals(this.uniqueId, that.uniqueId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.uniqueId);
  }
}
