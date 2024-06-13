package net.labymod.serverapi.core;

import net.labymod.serverapi.api.AbstractProtocolService;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractLabyModProtocolService extends AbstractProtocolService {

  protected final LabyModProtocol labyModProtocol;

  /**
   * Creates a new labymod protocol service.
   *
   * @param side the side the protocol service is running on. {@link Side#CLIENT} for client-side
   *             and {@link Side#SERVER} for server-side
   */
  protected AbstractLabyModProtocolService(@NotNull Side side) {
    super(side);
    this.labyModProtocol = new LabyModProtocol(this);
    this.registry().registerProtocol(this.labyModProtocol);
  }

  /**
   * @return the LabyMod protocol
   */
  public final LabyModProtocol labyModProtocol() {
    return this.labyModProtocol;
  }
}
