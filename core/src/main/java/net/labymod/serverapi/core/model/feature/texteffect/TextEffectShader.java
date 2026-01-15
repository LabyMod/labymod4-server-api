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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a shader definition for a text effect.
 */
public class TextEffectShader {

  private final String code;
  private final String functionName;

  private TextEffectShader(@NotNull String code, @Nullable String functionName) {
    Objects.requireNonNull(code, "Code cannot be null");
    this.code = code;
    this.functionName = functionName;
  }

  public static TextEffectShader create(@NotNull String code) {
    return new TextEffectShader(code, null);
  }

  public static TextEffectShader create(@NotNull String code, @Nullable String functionName) {
    return new TextEffectShader(code, functionName);
  }

  public @NotNull String getCode() {
    return this.code;
  }

  public @Nullable String getFunctionName() {
    return this.functionName;
  }

  @Override
  public String toString() {
    return "TextEffectShader{" +
        "code='" + this.code + '\'' +
        ", functionName='" + this.functionName + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TextEffectShader)) {
      return false;
    }
    TextEffectShader that = (TextEffectShader) o;
    return Objects.equals(this.code, that.code)
        && Objects.equals(this.functionName, that.functionName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.code, this.functionName);
  }
}
