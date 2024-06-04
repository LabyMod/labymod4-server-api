package net.labymod.serverapi.core;

import net.labymod.serverapi.protocol.protocol.AbstractProtocolService;

public class AbstractLabyModProtocolService extends AbstractProtocolService {

  private final LabyModProtocol labyModProtocol;

  protected AbstractLabyModProtocolService() {
    this.labyModProtocol = new LabyModProtocol();

    this.registry().registerProtocol(this.labyModProtocol);
  }

  /**
   * @return the LabyMod protocol
   */
  public final LabyModProtocol labyModProtocol() {
    return this.labyModProtocol;
  }
}
