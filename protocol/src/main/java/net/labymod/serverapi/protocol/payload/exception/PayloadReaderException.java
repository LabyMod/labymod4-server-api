package net.labymod.serverapi.protocol.payload.exception;

public class PayloadReaderException extends PayloadException {

  public PayloadReaderException(String message) {
    super(message);
  }

  public PayloadReaderException(String message, Throwable cause) {
    super(message, cause);
  }
}
