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

package net.labymod.serverapi.core.packet.serverbound.game.supplement;

import net.labymod.serverapi.api.packet.IdentifiablePacket;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.packet.clientbound.game.supplement.ServerSwitchPromptPacket;
import org.jetbrains.annotations.NotNull;

public class ServerSwitchPromptResponsePacket extends IdentifiablePacket {

  private String address;
  private boolean accepted;

  // Constructor for reading
  protected ServerSwitchPromptResponsePacket() {
    super(null);
  }

  public ServerSwitchPromptResponsePacket(
      @NotNull ServerSwitchPromptPacket initiator,
      boolean accepted
  ) {
    super(initiator);
    this.address = initiator.prompt().getAddress();
    this.accepted = accepted;
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    super.read(reader);
    this.address = reader.readString();
    this.accepted = reader.readBoolean();
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    super.write(writer);
    writer.writeString(this.address);
    writer.writeBoolean(this.accepted);
  }

  public @NotNull String getAddress() {
    return this.address;
  }

  public boolean wasAccepted() {
    return this.accepted;
  }

  @Override
  public String toString() {
    return "ServerSwitchResponsePacket{" +
        "address='" + this.address + '\'' +
        ", accepted=" + this.accepted +
        "} " + super.toString();
  }
}
