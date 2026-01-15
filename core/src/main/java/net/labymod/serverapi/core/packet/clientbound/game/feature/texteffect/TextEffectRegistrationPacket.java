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
import net.labymod.serverapi.core.model.feature.texteffect.TextEffect;
import net.labymod.serverapi.core.model.feature.texteffect.TextEffectParameter;
import net.labymod.serverapi.core.model.feature.texteffect.TextEffectParameterType;
import net.labymod.serverapi.core.model.feature.texteffect.TextEffectShader;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Packet to register custom text effects with the client.
 */
public class TextEffectRegistrationPacket implements Packet {

  private List<TextEffect> effects;

  public TextEffectRegistrationPacket(@NotNull List<TextEffect> effects) {
    Objects.requireNonNull(effects, "Effects cannot be null");
    this.effects = effects;
  }

  public TextEffectRegistrationPacket(@NotNull TextEffect... effects) {
    Objects.requireNonNull(effects, "Effects cannot be null");
    this.effects = Collections.unmodifiableList(Arrays.asList(effects));
  }

  @Override
  public void read(@NotNull PayloadReader reader) {
    this.effects = reader.readList(() -> {
      String id = reader.readString();
      Integer effectId = reader.readOptional(reader::readVarInt);
      String displayName = reader.readString();

      List<TextEffectParameter> parameters = reader.readList(() -> {
        String paramName = reader.readString();
        String paramDisplayName = reader.readOptionalString();
        TextEffectParameterType paramType = TextEffectParameterType.fromIdentifier(
            reader.readString()
        );
        if (paramType == null) {
          return null;
        }

        Object defaultValue = readParameterValue(reader, paramType);
        Float min = reader.readOptional(reader::readFloat);
        Float max = reader.readOptional(reader::readFloat);
        Float step = reader.readOptional(reader::readFloat);
        String uniform = reader.readString();

        return TextEffectParameter.create(
            paramName, paramDisplayName, paramType, defaultValue,
            min, max, step, uniform
        );
      });

      TextEffectShader fragmentShader = reader.readOptional(() -> {
        String code = reader.readString();
        String functionName = reader.readOptionalString();
        return TextEffectShader.create(code, functionName);
      });

      TextEffectShader vertexShader = reader.readOptional(() -> {
        String code = reader.readString();
        String functionName = reader.readOptionalString();
        return TextEffectShader.create(code, functionName);
      });

      TextEffect.Builder builder = TextEffect.builder(id)
          .displayName(displayName)
          .parameters(parameters);

      if (effectId != null) {
        builder.effectId(effectId);
      }
      if (fragmentShader != null) {
        builder.fragmentShader(fragmentShader);
      }
      if (vertexShader != null) {
        builder.vertexShader(vertexShader);
      }

      return builder.build();
    });
  }

  @Override
  public void write(@NotNull PayloadWriter writer) {
    writer.writeCollection(this.effects, effect -> {
      writer.writeString(effect.getId());
      writer.writeOptional(effect.getEffectId(), writer::writeVarInt);
      writer.writeString(effect.getDisplayName());

      writer.writeCollection(effect.getParameters(), param -> {
        writer.writeString(param.getName());
        writer.writeOptionalString(param.getDisplayName());
        writer.writeString(param.getType().getIdentifier());
        writeParameterValue(writer, param.getType(), param.getDefaultValue());
        writer.writeOptional(param.getMin(), writer::writeFloat);
        writer.writeOptional(param.getMax(), writer::writeFloat);
        writer.writeOptional(param.getStep(), writer::writeFloat);
        writer.writeString(param.getUniform());
      });

      writer.writeOptional(effect.getFragmentShader(), shader -> {
        writer.writeString(shader.getCode());
        writer.writeOptionalString(shader.getFunctionName());
      });

      writer.writeOptional(effect.getVertexShader(), shader -> {
        writer.writeString(shader.getCode());
        writer.writeOptionalString(shader.getFunctionName());
      });
    });
  }

  public @NotNull List<TextEffect> getEffects() {
    return this.effects;
  }

  @Override
  public String toString() {
    return "TextEffectRegistrationPacket{" +
        "effects=" + this.effects +
        '}';
  }

  static Object readParameterValue(PayloadReader reader, TextEffectParameterType type) {
    switch (type) {
      case FLOAT:
        return reader.readFloat();
      case INT:
        return reader.readVarInt();
      case BOOLEAN:
        return reader.readBoolean();
      case COLOR:
        float[] color = new float[4];
        for (int i = 0; i < 4; i++) {
          color[i] = reader.readFloat();
        }
        return color;
      default:
        return null;
    }
  }

  static void writeParameterValue(PayloadWriter writer, TextEffectParameterType type, Object value) {
    switch (type) {
      case FLOAT:
        writer.writeFloat(((Number) value).floatValue());
        break;
      case INT:
        writer.writeVarInt(((Number) value).intValue());
        break;
      case BOOLEAN:
        writer.writeBoolean((Boolean) value);
        break;
      case COLOR:
        float[] color;
        if (value instanceof float[]) {
          color = (float[]) value;
        } else if (value instanceof List) {
          List<?> list = (List<?>) value;
          color = new float[4];
          for (int i = 0; i < Math.min(4, list.size()); i++) {
            color[i] = ((Number) list.get(i)).floatValue();
          }
        } else {
          color = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
        }
        for (int i = 0; i < 4; i++) {
          writer.writeFloat(i < color.length ? color[i] : 1.0f);
        }
        break;
    }
  }
}
