/*
 * MIT License
 *
 * Copyright (c) 2024 LabyMedia GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.labymod.serverapi.server.common;

import net.labymod.serverapi.api.Protocol;
import net.labymod.serverapi.core.AbstractLabyModProtocolService;
import net.labymod.serverapi.core.model.display.Subtitle;
import net.labymod.serverapi.core.model.display.TabListFlag;
import net.labymod.serverapi.core.packet.clientbound.game.display.SubtitlePacket;
import net.labymod.serverapi.core.packet.clientbound.game.display.TabListFlagPacket;
import net.labymod.serverapi.core.packet.serverbound.game.moderation.AddonStateChangedPacket;
import net.labymod.serverapi.core.packet.serverbound.game.moderation.InstalledAddonsResponsePacket;
import net.labymod.serverapi.server.common.handler.DefaultAddonStateChangedPacketHandler;
import net.labymod.serverapi.server.common.handler.DefaultInstalledAddonsResponsePacketHandler;
import net.labymod.serverapi.server.common.model.player.AbstractServerLabyModPlayer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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

    this.registerDefaultHandlers();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public @Nullable T getPlayer(@NotNull UUID uniqueId) {
    return this.players.get(uniqueId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
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

  /**
   * Called when a player quits the server and removes cached values like the player instance and
   * awaited responses.
   */
  @ApiStatus.Internal
  public void handlePlayerQuit(@NotNull UUID uniqueId) {
    this.players.remove(uniqueId);
    for (Protocol protocol : this.registry().getProtocols()) {
      protocol.clearAwaitingResponsesFor(uniqueId);
    }
  }

  @ApiStatus.Internal
  public void handlePlayerJoin(@NotNull T labyModPlayer) {
    this.players.put(labyModPlayer.getUniqueId(), labyModPlayer);
    List<Subtitle> subtitles = new ArrayList<>();
    List<TabListFlag> flags = new ArrayList<>();
    this.forEachPlayer(player -> {
      if (player.hasSubtitle()) {
        subtitles.add(player.subtitle());
      }

      TabListFlag tabListFlag = player.getTabListFlag();
      if (tabListFlag != null) {
        flags.add(tabListFlag);
      }
    });

    if (!subtitles.isEmpty()) {
      this.labyModProtocol.sendPacket(labyModPlayer.getUniqueId(), new SubtitlePacket(subtitles));
    }

    if (!flags.isEmpty()) {
      this.labyModProtocol.sendPacket(labyModPlayer.getUniqueId(), new TabListFlagPacket(flags));
    }
  }

  @ApiStatus.Internal
  public abstract void handleInstalledAddonsUpdate(AbstractServerLabyModPlayer<?, ?> labyModPlayer);

  protected void registerDefaultHandlers() {
    this.labyModProtocol.registerHandler(
        AddonStateChangedPacket.class,
        new DefaultAddonStateChangedPacketHandler(this)
    );

    this.labyModProtocol.registerHandler(
        InstalledAddonsResponsePacket.class,
        new DefaultInstalledAddonsResponsePacketHandler(this)
    );
  }
}
