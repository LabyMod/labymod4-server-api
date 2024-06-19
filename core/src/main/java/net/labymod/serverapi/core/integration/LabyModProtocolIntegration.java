package net.labymod.serverapi.core.integration;

import net.labymod.serverapi.core.AbstractLabyModPlayer;
import net.labymod.serverapi.core.AbstractLabyModProtocolService;

public interface LabyModProtocolIntegration {

  void initialize(AbstractLabyModProtocolService protocolService);

  default LabyModIntegrationPlayer createIntegrationPlayer(AbstractLabyModPlayer<?> labyModPlayer) {
    return null;
  }
}
