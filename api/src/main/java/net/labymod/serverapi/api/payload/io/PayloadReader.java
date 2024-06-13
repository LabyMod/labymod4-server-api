package net.labymod.serverapi.api.payload.io;

import net.labymod.serverapi.api.model.component.ServerAPIComponent;
import net.labymod.serverapi.api.model.component.ServerAPITextColor;
import net.labymod.serverapi.api.model.component.ServerAPITextDecoration;
import net.labymod.serverapi.api.payload.exception.PayloadReaderException;
import org.jetbrains.annotations.NotNull;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

public class PayloadReader {

  private final ByteBuffer buffer;

  public PayloadReader() {
    this(ByteBuffer.allocate(255));
  }

  public PayloadReader(int capacity) {
    this(ByteBuffer.allocate(capacity));
  }

  public PayloadReader(byte @NotNull [] payload) {
    this(ByteBuffer.wrap(payload));
  }

  public PayloadReader(ByteBuffer buffer) {
    this.buffer = buffer;
  }

  public int readVarInt() {
    try {
      int result = 0;
      int bitOffset = 0;

      byte currentByte;
      do {
        currentByte = this.buffer.get();
        result |= (currentByte & 127) << bitOffset++ * 7;
        if (bitOffset > 5) {
          throw new PayloadReaderException("VarInt is too big");
        }
      } while ((currentByte & 128) == 128);

      return result;
    } catch (BufferUnderflowException exception) {
      throw new PayloadReaderException(
          "The current position of the buffer is smaller than the limit.",
          exception
      );
    }
  }

  public String readString() {
    return this.readSizedString(Short.MAX_VALUE);
  }

  public String readSizedString(int maximumLength) {

    int length = this.readVarInt();

    if (length > maximumLength * 4) {
      throw new PayloadReaderException(
          String.format(
              "The received encoded string buffer length is longer than maximum allowed (%s > %s)",
              length,
              maximumLength * 4
          )
      );
    } else if (length < 0) {
      throw new PayloadReaderException(
          "The received encoded string buffer length is less than zero.");
    }

    byte[] data = new byte[length];

    try {
      this.buffer.get(data);
    } catch (BufferUnderflowException exception) {
      throw new PayloadReaderException(
          String.format(
              "Could not read %s, %s remaining.",
              length,
              this.buffer.remaining()
          )
      );
    }

    return new String(data, StandardCharsets.UTF_8);
  }

  public boolean readBoolean() {
    return this.buffer.get() == 1;
  }

  public int readInt() {
    return this.buffer.getInt();
  }

  public long readLong() {
    return this.buffer.getLong();
  }

  public byte[] readBytes(int length) {
    byte[] data = new byte[length];
    this.buffer.get(data);
    return data;
  }

  public byte readByte() {
    return this.buffer.get();
  }

  public short readShort() {
    return this.buffer.getShort();
  }

  public float readFloat() {
    return this.buffer.getFloat();
  }

  public double readDouble() {
    return this.buffer.getDouble();
  }

  public <T> List<T> readList(Supplier<T> reader) {
    int length = this.readVarInt();
    List<T> list = new ArrayList<>(length);
    for (int i = 0; i < length; i++) {
      T value = reader.get();
      if (value == null) {
        continue;
      }

      list.add(value);
    }

    return list;
  }

  public <T> Set<T> readSet(Supplier<T> reader) {
    int length = this.readVarInt();
    Set<T> set = new HashSet<>(length);
    for (int i = 0; i < length; i++) {
      set.add(reader.get());
    }

    return set;
  }

  public <T> T[] readArray(Supplier<T> reader) {
    int length = this.readVarInt();
    T[] array = (T[]) new Object[length];
    for (int i = 0; i < length; i++) {
      array[i] = reader.get();
    }

    return array;
  }

  public UUID readUUID() {
    return new UUID(this.readLong(), this.readLong());
  }

  public <T> T readOptional(Supplier<T> reader) {
    return this.readBoolean() ? reader.get() : null;
  }

  public String[] readStringArray() {
    return this.readArray(this::readString);
  }

  public String readOptionalString() {
    return this.readOptional(this::readString);
  }

  public String[] readArray() {
    return this.readArray(this::readString);
  }

  public ServerAPIComponent readComponent() {
    byte id = this.readByte();
    ServerAPIComponent component;
    if (id == 1) {
      component = ServerAPIComponent.text(this.readString());
    } else {
      component = ServerAPIComponent.empty();
    }

    if (this.readBoolean()) {
      component.color(this.readOptional(() -> ServerAPITextColor.of(this.readInt())));

      List<ServerAPITextDecoration> decorations = ServerAPITextDecoration.getValues();
      int setDecorations = this.readVarInt();
      for (int i = 0; i < setDecorations; i++) {
        int ordinal = this.readByte();
        if (ordinal < 0 || ordinal >= decorations.size()) {
          continue;
        }

        ServerAPITextDecoration decoration = decorations.get(ordinal);
        if (this.readBoolean()) {
          component.decorate(decoration);
        } else {
          component.undecorate(decoration);
        }
      }
    }

    component.setChildren(this.readList(this::readComponent));
    return component;
  }

  public void reset() {
    this.buffer.position(0);
  }
}
