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

  public record DecimalFormat(@NotNull String format, double divisor) {

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

    @Override
    public int hashCode() {
      return Objects.hash(this.format, this.divisor);
    }
  }
}
