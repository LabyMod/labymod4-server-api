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

package net.labymod.serverapi.core.model.display;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class EconomyDisplay {

  private final String key;
  private final boolean visible;
  private final double balance;

  private final String iconUrl;
  private final DecimalFormat decimalFormat;

  public EconomyDisplay(
      @NotNull String key,
      boolean visible,
      double balance,
      @Nullable String iconUrl,
      @Nullable DecimalFormat decimalFormat
  ) {
    this.key = key;
    this.visible = visible;
    this.balance = balance;
    this.iconUrl = iconUrl;
    this.decimalFormat = decimalFormat;
  }

  public String getKey() {
    return this.key;
  }

  public boolean isVisible() {
    return this.visible;
  }

  public double getBalance() {
    return this.balance;
  }

  public String getIconUrl() {
    return this.iconUrl;
  }

  public DecimalFormat getDecimalFormat() {
    return this.decimalFormat;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof EconomyDisplay that)) {
      return false;
    }

    return this.visible == that.visible && Double.compare(this.balance, that.balance) == 0
        && Objects.equals(this.key, that.key) && Objects.equals(this.iconUrl, that.iconUrl)
        && Objects.equals(this.decimalFormat, that.decimalFormat);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.key, this.visible, this.balance, this.iconUrl, this.decimalFormat);
  }

  @Override
  public String toString() {
    return "EconomyDisplay{" +
        "key='" + this.key + '\'' +
        ", visible=" + this.visible +
        ", balance=" + this.balance +
        ", iconUrl='" + this.iconUrl + '\'' +
        ", decimalFormat=" + this.decimalFormat +
        '}';
  }

  public static class DecimalFormat {

    private final String format;
    private final double divisor;

    public DecimalFormat(@NotNull String format, double divisor) {
      Objects.requireNonNull(format, "Format must not be null");
      this.format = format;
      this.divisor = divisor;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }

      if (!(o instanceof DecimalFormat that)) {
        return false;
      }

      return Double.compare(this.divisor, that.divisor) == 0 && Objects.equals(this.format,
          that.format);
    }

    public String getFormat() {
      return this.format;
    }

    public double getDivisor() {
      return this.divisor;
    }

    /**
     * @deprecated Use {@link #getFormat()}
     */
    public String format() {
      return this.format;
    }

    /**
     * @deprecated Use {@link #getDivisor()}
     */
    public double divisor() {
      return this.divisor;
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.format, this.divisor);
    }

    @Override
    public String toString() {
      return "DecimalFormat{" +
          "format='" + this.format + '\'' +
          ", divisor=" + this.divisor +
          '}';
    }
  }
}
