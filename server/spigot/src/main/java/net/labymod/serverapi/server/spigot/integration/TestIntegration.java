package net.labymod.serverapi.server.spigot.integration;

import net.labymod.serverapi.core.AbstractLabyModProtocolService;
import net.labymod.serverapi.core.integration.LabyModProtocolIntegration;

public class TestIntegration implements LabyModProtocolIntegration {

  @Override
  public void initialize(AbstractLabyModProtocolService protocolService) {
    protocolService.logger().info("Initializing Integration");
  }
}
