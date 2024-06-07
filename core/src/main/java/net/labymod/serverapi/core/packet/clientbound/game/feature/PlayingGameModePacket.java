package net.labymod.serverapi.core.packet.clientbound.game.feature;

import net.labymod.serverapi.protocol.packet.Packet;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.labymod.serverapi.protocol.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayingGameModePacket implements Packet {

  private String gameMode;

  public PlayingGameModePacket(@Nullable String gameMode) {
    this.gameMode = gameMode;
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.gameMode = reader.readOptionalString();
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeOptionalString(this.gameMode);
  }

  public @NotNull String getGameMode() {
    return this.gameMode;
  }

  @Override
  public String toString() {
    return "PlayingGameModePacket{" +
        "gameMode='" + this.gameMode + '\'' +
        '}';
  }
}
