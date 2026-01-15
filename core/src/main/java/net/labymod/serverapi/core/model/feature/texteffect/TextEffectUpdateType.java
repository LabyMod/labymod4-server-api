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
 * Types of updates that can be sent for text effects and rules.
 */
public enum TextEffectUpdateType {
  EFFECT_PARAMETERS("effect_parameters"),
  RULE_STATE("rule_state"),
  RULE_PARAMETERS("rule_parameters");

  private final String identifier;

  TextEffectUpdateType(String identifier) {
    this.identifier = identifier;
  }

  public String getIdentifier() {
    return this.identifier;
  }

  public static @Nullable TextEffectUpdateType fromIdentifier(String identifier) {
    for (TextEffectUpdateType type : values()) {
      if (type.identifier.equalsIgnoreCase(identifier)) {
        return type;
      }
    }
    return null;
  }
}
