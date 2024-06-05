package net.labymod.serverapi.core;

import net.labymod.serverapi.core.packet.clientbound.game.display.EconomyDisplayPacket;
import net.labymod.serverapi.core.packet.clientbound.game.moderation.PermissionPacket;
import net.labymod.serverapi.core.packet.serverbound.login.VersionLoginPacket;
import net.labymod.serverapi.protocol.Protocol;
import net.labymod.serverapi.protocol.packet.Direction;
import net.labymod.serverapi.protocol.payload.PayloadChannelIdentifier;

public class LabyModProtocol extends Protocol<AbstractLabyModProtocolService> {

  protected LabyModProtocol(AbstractLabyModProtocolService protocolService) {
    super(protocolService, PayloadChannelIdentifier.create("labymod", "neo"));
    this.registerPackets();
  }

  private void registerPackets() {
    this.registerPacket(0, VersionLoginPacket.class, Direction.SERVERBOUND);

    this.registerPacket(13, EconomyDisplayPacket.class, Direction.CLIENTBOUND);

    this.registerPacket(40, PermissionPacket.class, Direction.CLIENTBOUND);
  }
}
