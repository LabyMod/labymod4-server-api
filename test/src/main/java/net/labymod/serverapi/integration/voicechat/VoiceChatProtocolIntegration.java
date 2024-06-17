package net.labymod.serverapi.integration.voicechat;

import autovalue.shaded.com.google.auto.service.AutoService;
import net.labymod.serverapi.core.AbstractLabyModProtocolService;
import net.labymod.serverapi.core.integration.LabyModProtocolIntegration;

@AutoService(LabyModProtocolIntegration.class)
public class VoiceChatProtocolIntegration implements LabyModProtocolIntegration {

  @Override
  public void initialize(AbstractLabyModProtocolService protocolService) {
    System.out.println("VoiceChatProtocolIntegration initialized");
  }
}
