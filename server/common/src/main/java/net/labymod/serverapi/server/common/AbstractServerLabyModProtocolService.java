package net.labymod.serverapi.server.common;

import net.labymod.serverapi.core.AbstractLabyModProtocolService;
import net.labymod.serverapi.server.common.model.player.AbstractServerLabyModPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class AbstractServerLabyModProtocolService<T extends AbstractServerLabyModPlayer>
    extends AbstractLabyModProtocolService {

  protected final Map<UUID, T> players;

  /**
   * Creates a new labymod protocol service.
   */
  protected AbstractServerLabyModProtocolService() {
    super(Side.SERVER);
    this.players = new HashMap<>();
  }

  /**
   * {@inheritDoc}
   */
  public @Nullable T getPlayer(@NotNull UUID uniqueId) {
    return this.players.get(uniqueId);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isUsingLabyMod(@NotNull UUID uniqueId) {
    return this.players.containsKey(uniqueId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<T> getPlayers() {
    return this.players.values();
  }

  /**
   * Loops through all players and executed the supplied consumer
   *
   * @param action The action to execute
   */
  public void forEachPlayer(@NotNull Consumer<T> action) {
    for (T value : this.players.values()) {
      action.accept(value);
    }
  }
}
