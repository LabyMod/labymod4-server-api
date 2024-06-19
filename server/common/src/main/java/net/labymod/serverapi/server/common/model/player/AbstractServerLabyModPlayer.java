package net.labymod.serverapi.server.common.model.player;

import net.labymod.serverapi.core.AbstractLabyModPlayer;
import net.labymod.serverapi.core.model.display.Subtitle;
import net.labymod.serverapi.server.common.AbstractServerLabyModProtocolService;

import java.util.UUID;

public abstract class AbstractServerLabyModPlayer<P extends AbstractServerLabyModPlayer<?, ?>, T>
    extends AbstractLabyModPlayer<P> {

  private final T serverPlayer;
  private Subtitle subtitle;

  protected AbstractServerLabyModPlayer(
      AbstractServerLabyModProtocolService<?> protocolService,
      UUID uniqueId,
      T player,
      String labyModVersion
  ) {
    super(protocolService, uniqueId, labyModVersion);
    this.serverPlayer = player;
  }

  public T getPlayer() {
    return this.serverPlayer;
  }
}
