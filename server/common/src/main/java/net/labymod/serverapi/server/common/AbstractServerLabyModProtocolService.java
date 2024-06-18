package net.labymod.serverapi.server.common;

import net.labymod.serverapi.core.AbstractLabyModProtocolService;
import net.labymod.serverapi.server.common.model.player.AbstractLabyModPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class AbstractServerLabyModProtocolService<T extends AbstractLabyModPlayer<?>>
    extends AbstractLabyModProtocolService {

  protected final Map<UUID, T> players;

  /**
   * Creates a new labymod protocol service.
   */
  protected AbstractServerLabyModProtocolService() {
    super(Side.SERVER);
    this.players = new HashMap<>();
  }

  public @Nullable T getPlayer(@NotNull UUID uniqueId) {
    return this.players.get(uniqueId);
  }

  public boolean isUsingLabyMod(@NotNull UUID uniqueId) {
    return this.players.containsKey(uniqueId);
  }

  public void forEachPlayer(@NotNull Consumer<T> action) {
    for (T value : this.players.values()) {
      action.accept(value);
    }
  }
}
