package net.labymod.serverapi.protocol.payload.io;

import net.labymod.serverapi.protocol.payload.exception.PayloadWriterException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.Buffer;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;

public class PayloadWriter {

  private ByteBuffer buffer;

  public PayloadWriter() {
    this(ByteBuffer.allocate(255));
  }

  public PayloadWriter(int capacity) {
    this(ByteBuffer.allocate(capacity));
  }

  public PayloadWriter(ByteBuffer buffer) {
    this.buffer = buffer;
  }

  public void writeVarInt(int value) {
    try {
      this.ensureSize(5);
      while ((value & -128) != 0) {
        this.buffer.put((byte) (value & 127 | 128));
        value >>>= 7;
      }
      this.buffer.put((byte) value);
    } catch (BufferOverflowException exception) {
      throw new PayloadWriterException("The buffer overflowed while writing.", exception);
    }
  }

  public void writeBytes(byte @NotNull [] bytes) {
    if (bytes.length == 0) {
      return;
    }
    this.ensureSize(bytes.length);
    this.buffer.put(bytes);
  }

  public void writeUuid(@NotNull UUID value) {
    this.writeString(value.toString());
  }

  public void writeString(@NotNull String value) {
    this.writeSizedString(value, Short.MAX_VALUE);
  }

  public void writeString(@NotNull Object value) {
    this.writeString(value.toString());
  }

  public void writeSizedString(@NotNull String value, int maximumLength) {
    byte[] valueData = value.getBytes(StandardCharsets.UTF_8);

    if (valueData.length > maximumLength) {
      throw new IllegalStateException(
          String.format(
              "String too big (was %s bytes encoded, maximum length %s)",
              valueData.length,
              maximumLength
          )
      );
    }

    this.writeVarInt(valueData.length);
    this.writeBytes(valueData);
  }

  public void writeInt(int value) {
    this.ensureSize(4);
    this.buffer.putInt(value);
  }

  public void writeBoolean(boolean value) {
    this.ensureSize(1);
    this.buffer.put((byte) (value ? 1 : 0));
  }

  public void writeShort(short value) {
    this.ensureSize(2);
    this.buffer.putShort(value);
  }

  public void writeLong(long value) {
    this.ensureSize(8);
    this.buffer.putLong(value);
  }

  public void writeFloat(float value) {
    this.ensureSize(4);
    this.buffer.putFloat(value);
  }

  public void writeDouble(double value) {
    this.ensureSize(8);
    this.buffer.putDouble(value);
  }

  public byte[] toByteArray() {
    this.bufferFlip();
    byte[] buffer = new byte[this.buffer.remaining()];
    this.buffer.get(buffer);
    return buffer;
  }

  public <T> void writeCollection(@NotNull Collection<T> list, @NotNull Consumer<T> writer) {
    this.writeVarInt(list.size());
    for (T element : list) {
      writer.accept(element);
    }
  }

  public <T> void writeArray(@Nullable T[] array, @NotNull Consumer<T> writer) {
    if (array == null) {
      this.writeVarInt(0);
      return;
    }

    this.writeVarInt(array.length);
    for (T element : array) {
      writer.accept(element);
    }
  }

  public void writeStringArray(@Nullable String[] array) {
    this.writeArray(array, this::writeString);
  }

  public <T> void writeOptional(@Nullable T value, @NotNull Consumer<T> writer) {
    this.writeBoolean(value != null);
    if (value != null) {
      writer.accept(value);
    }
  }

  public void writeOptionalString(@Nullable String value) {
    this.writeOptional(value, this::writeString);
  }

  private void ensureSize(int length) {
    int position = this.buffer.position();

    if (position + length >= this.buffer.limit()) {
      int newLength = (position + length) * 4;

      ByteBuffer copy = this.buffer.isDirect() ?
          ByteBuffer.allocateDirect(newLength) :
          ByteBuffer.allocate(newLength);
      this.bufferFlip();
      copy.put(this.buffer);

      this.buffer = copy;
    }
  }

  private void bufferFlip() {
    // In the older Java versions the method `java.nio.ByteBuffer.flip()Ljava/nio/ByteBuffer;` did
    // not exist.
    //noinspection RedundantCast
    ((Buffer) this.buffer).flip();
  }
}
