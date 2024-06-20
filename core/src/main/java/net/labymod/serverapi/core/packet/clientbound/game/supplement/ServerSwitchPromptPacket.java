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

package net.labymod.serverapi.core.packet.clientbound.game.supplement;

import net.labymod.serverapi.api.packet.IdentifiablePacket;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.model.supplement.ServerSwitchPrompt;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ServerSwitchPromptPacket extends IdentifiablePacket {

  private ServerSwitchPrompt prompt;

  public ServerSwitchPromptPacket(@NotNull ServerSwitchPrompt prompt) {
    Objects.requireNonNull(prompt, "Prompt cannot be null");
    this.prompt = prompt;
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    super.read(reader);
    this.prompt = ServerSwitchPrompt.create(
        reader.readComponent(),
        reader.readString(),
        reader.readBoolean()
    );
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    super.write(writer);
    writer.writeComponent(this.prompt.title());
    writer.writeString(this.prompt.getAddress());
    writer.writeBoolean(this.prompt.isShowPreview());
  }

  public @NotNull ServerSwitchPrompt prompt() {
    return this.prompt;
  }

  @Override
  public String toString() {
    return "ServerSwitchPromptPacket{" +
        "prompt=" + this.prompt +
        "} " + super.toString();
  }
}
