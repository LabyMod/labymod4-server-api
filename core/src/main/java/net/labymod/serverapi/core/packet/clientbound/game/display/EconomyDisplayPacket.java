package net.labymod.serverapi.core.packet.clientbound.game.display;

import net.labymod.serverapi.core.model.display.EconomyDisplay;
import net.labymod.serverapi.protocol.packet.Packet;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.labymod.serverapi.protocol.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

public class EconomyDisplayPacket implements Packet {

  private EconomyDisplay economy;

  public EconomyDisplayPacket() {
    // Constructor for reading
  }

  public EconomyDisplayPacket(EconomyDisplay economy) {
    this.economy = economy;
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    String key = reader.readString();
    boolean visible = reader.readBoolean();
    double balance = reader.readDouble();
    String iconUrl = reader.readOptionalString();
    EconomyDisplay.DecimalFormat decimalFormat = reader.readOptional(() -> {
      String format = reader.readOptionalString();
      double divisor = reader.readDouble();
      return new EconomyDisplay.DecimalFormat(format, divisor);
    });

    this.economy = new EconomyDisplay(key, visible, balance, iconUrl, decimalFormat);
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeString(this.economy.getKey());
    writer.writeBoolean(this.economy.isVisible());
    writer.writeDouble(this.economy.getBalance());
    writer.writeOptionalString(this.economy.getIconUrl());
    writer.writeOptional(this.economy.getDecimalFormat(), decimalFormat -> {
      writer.writeOptionalString(decimalFormat.format());
      writer.writeDouble(decimalFormat.divisor());
    });
  }

  public EconomyDisplay getEconomy() {
    return this.economy;
  }
}
