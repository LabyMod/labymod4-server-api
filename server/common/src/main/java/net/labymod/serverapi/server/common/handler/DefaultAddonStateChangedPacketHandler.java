package net.labymod.serverapi.server.common.handler;

import net.labymod.serverapi.api.packet.PacketHandler;
import net.labymod.serverapi.core.packet.serverbound.game.moderation.AddonStateChangedPacket;
import net.labymod.serverapi.server.common.AbstractServerLabyModProtocolService;
import net.labymod.serverapi.server.common.model.player.AbstractServerLabyModPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DefaultAddonStateChangedPacketHandler
    implements PacketHandler<AddonStateChangedPacket> {

  private final AbstractServerLabyModProtocolService<?> protocolService;

  public DefaultAddonStateChangedPacketHandler(
      AbstractServerLabyModProtocolService<?> protocolService) {
    this.protocolService = protocolService;
  }

  @Override
  public void handle(@NotNull UUID sender, @NotNull AddonStateChangedPacket packet) {
    AbstractServerLabyModPlayer<?, ?> player = this.protocolService.getPlayer(sender);
    if (player == null) {
      return;
    }

    player.installedAddons().addAddon(packet.getNamespace(), packet.isEnabled());
  }
}
