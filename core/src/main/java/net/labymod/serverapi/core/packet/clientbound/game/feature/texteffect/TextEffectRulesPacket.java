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

package net.labymod.serverapi.core.packet.clientbound.game.feature.texteffect;

import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.model.feature.texteffect.TextEffectContext;
import net.labymod.serverapi.core.model.feature.texteffect.TextEffectRule;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Packet to define rules for when text effects should be applied.
 */
public class TextEffectRulesPacket implements Packet {

  private List<TextEffectRule> rules;

  public TextEffectRulesPacket(@NotNull List<TextEffectRule> rules) {
    Objects.requireNonNull(rules, "Rules cannot be null");
    this.rules = rules;
  }

  public TextEffectRulesPacket(@NotNull TextEffectRule... rules) {
    Objects.requireNonNull(rules, "Rules cannot be null");
    this.rules = Collections.unmodifiableList(Arrays.asList(rules));
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.rules = reader.readList(() -> {
      String id = reader.readString();
      String effect = reader.readString();
      TextEffectContext context = TextEffectContext.fromIdentifier(reader.readString());
      if (context == null) {
        return null;
      }

      int priority = reader.readVarInt();
      UUID targetUser = reader.readOptional(reader::readUUID);
      boolean enabled = reader.readBoolean();
      Map<String, Object> parameters = readParameters(reader);

      return TextEffectRule.builder(id)
          .effect(effect)
          .context(context)
          .priority(priority)
          .targetUser(targetUser)
          .enabled(enabled)
          .parameters(parameters)
          .build();
    });
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeCollection(this.rules, rule -> {
      writer.writeString(rule.getId());
      writer.writeString(rule.getEffect());
      writer.writeString(rule.getContext().getIdentifier());
      writer.writeVarInt(rule.getPriority());
      writer.writeOptional(rule.getTargetUser(), writer::writeUUID);
      writer.writeBoolean(rule.isEnabled());
      writeParameters(writer, rule.getParameters());
    });
  }

  public @NotNull List<TextEffectRule> getRules() {
    return this.rules;
  }

  @Override
  public String toString() {
    return "TextEffectRulesPacket{" +
        "rules=" + this.rules +
        '}';
  }

  static Map<String, Object> readParameters(PayloadReader reader) {
    int count = reader.readVarInt();
    Map<String, Object> parameters = new HashMap<>(count);
    for (int i = 0; i < count; i++) {
      String name = reader.readString();
      byte typeId = reader.readByte();
      Object value;
      switch (typeId) {
        case 0: // float
          value = reader.readFloat();
          break;
        case 1: // int
          value = reader.readVarInt();
          break;
        case 2: // boolean
          value = reader.readBoolean();
          break;
        case 3: // color (float array)
          float[] color = new float[4];
          for (int j = 0; j < 4; j++) {
            color[j] = reader.readFloat();
          }
          value = color;
          break;
        default:
          continue;
      }
      parameters.put(name, value);
    }
    return parameters;
  }

  static void writeParameters(PayloadWriter writer, Map<String, Object> parameters) {
    writer.writeVarInt(parameters.size());
    for (Map.Entry<String, Object> entry : parameters.entrySet()) {
      writer.writeString(entry.getKey());
      Object value = entry.getValue();
      if (value instanceof Float || value instanceof Double) {
        writer.writeByte((byte) 0);
        writer.writeFloat(((Number) value).floatValue());
      } else if (value instanceof Integer || value instanceof Long) {
        writer.writeByte((byte) 1);
        writer.writeVarInt(((Number) value).intValue());
      } else if (value instanceof Boolean) {
        writer.writeByte((byte) 2);
        writer.writeBoolean((Boolean) value);
      } else if (value instanceof float[]) {
        writer.writeByte((byte) 3);
        float[] color = (float[]) value;
        for (int i = 0; i < 4; i++) {
          writer.writeFloat(i < color.length ? color[i] : 1.0f);
        }
      } else if (value instanceof List) {
        // Assume it's a color list
        writer.writeByte((byte) 3);
        List<?> list = (List<?>) value;
        for (int i = 0; i < 4; i++) {
          if (i < list.size()) {
            writer.writeFloat(((Number) list.get(i)).floatValue());
          } else {
            writer.writeFloat(1.0f);
          }
        }
      } else {
        // Unknown type, write as float with default value
        writer.writeByte((byte) 0);
        writer.writeFloat(0.0f);
      }
    }
  }
}
