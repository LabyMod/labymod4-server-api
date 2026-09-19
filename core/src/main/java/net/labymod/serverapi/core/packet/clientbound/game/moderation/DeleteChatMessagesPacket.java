package net.labymod.serverapi.core.packet.clientbound.game.moderation;

import java.util.Objects;
import java.util.UUID;
import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import org.jetbrains.annotations.NotNull;

/** Removes all currently stored game-chat messages sent by one user. */
public class DeleteChatMessagesPacket implements Packet {

  private UUID sender;

  public DeleteChatMessagesPacket(@NotNull UUID sender) {
    this.sender = Objects.requireNonNull(sender, "Sender UUID");
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.sender = reader.readUUID();
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeUUID(this.sender);
  }

  public @NotNull UUID sender() {
    return this.sender;
  }
}
