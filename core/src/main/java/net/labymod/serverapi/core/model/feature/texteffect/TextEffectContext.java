/*
 * MIT License
 *
 * Copyright (c) 2025 LabyMedia GmbH
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

package net.labymod.serverapi.core.model.feature.texteffect;

import org.jetbrains.annotations.Nullable;

/**
 * Render contexts for text effects. Determines where the effect should be applied.
 */
public enum TextEffectContext {
  PLAYER_LIST("player_list"),
  NAMETAG("nametag"),
  CHAT("chat"),
  SCOREBOARD("scoreboard"),
  TAB_HEADER("tab_header"),
  TAB_FOOTER("tab_footer"),
  BOSS_BAR("boss_bar"),
  ACTION_BAR("action_bar"),
  TITLE("title"),
  SUBTITLE("subtitle"),
  SIGN("sign"),
  ANY("any");

  private final String identifier;

  TextEffectContext(String identifier) {
    this.identifier = identifier;
  }

  public String getIdentifier() {
    return this.identifier;
  }

  public static @Nullable TextEffectContext fromIdentifier(String identifier) {
    for (TextEffectContext context : values()) {
      if (context.identifier.equalsIgnoreCase(identifier)) {
        return context;
      }
    }
    return null;
  }
}
