package net.labymod.serverapi.core;

import net.labymod.serverapi.core.packet.serverbound.login.VersionLoginPacket;
import net.labymod.serverapi.protocol.payload.identifier.PayloadChannelIdentifier;
import net.labymod.serverapi.protocol.protocol.Protocol;

public class LabyModProtocol extends Protocol {

  protected LabyModProtocol() {
    super(PayloadChannelIdentifier.create("labymod", "neo"));
    this.registerPackets();
  }

  private void registerPackets() {
    this.registerPacket(0, VersionLoginPacket.class);
  }
}
