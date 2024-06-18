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

    if (!(o instanceof EconomyDisplay)) {
      return false;
    }

    EconomyDisplay that = (EconomyDisplay) o;
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

      if (!(o instanceof DecimalFormat)) {
        return false;
      }

      DecimalFormat that = (DecimalFormat) o;
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
