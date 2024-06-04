package net.labymod.serverapi.protocol.protocol;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractProtocolService {

  private final ProtocolRegistry registry;

  protected AbstractProtocolService() {
    this.registry = new ProtocolRegistry();
  }

  /**
   * @return The protocol registry.
   */
  public @NotNull ProtocolRegistry registry() {
    return this.registry;
  }
}
