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
