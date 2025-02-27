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

package net.labymod.serverapi.core.packet.clientbound.game.supplement;

import net.labymod.serverapi.api.packet.IdentifiablePacket;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.model.supplement.InputPrompt;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InputPromptPacket extends IdentifiablePacket {

  private InputPrompt prompt;

  public InputPromptPacket(@NotNull InputPrompt prompt) {
    Objects.requireNonNull(prompt, "Prompt cannot be null");
    this.prompt = prompt;
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    super.read(reader);
    this.prompt = InputPrompt.create(
        reader.readComponent(),
        reader.readOptional(reader::readComponent),
        reader.readOptionalString(),
        reader.readVarInt()
    );
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    super.write(writer);
    writer.writeComponent(this.prompt.title());
    writer.writeOptional(this.prompt.getPlaceholder(), writer::writeComponent);
    writer.writeOptionalString(this.prompt.getDefaultValue());
    writer.writeVarInt(this.prompt.getMaxLength());
  }

  public @NotNull InputPrompt prompt() {
    return this.prompt;
  }

  @Override
  public String toString() {
    return "InputPromptPacket{" +
        "prompt=" + this.prompt +
        "} " + super.toString();
  }
}
