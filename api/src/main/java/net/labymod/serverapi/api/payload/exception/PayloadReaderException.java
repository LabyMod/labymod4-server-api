package net.labymod.serverapi.api.payload.exception;

public class PayloadReaderException extends PayloadException {

  public PayloadReaderException(String message) {
    super(message);
  }

  public PayloadReaderException(String message, Throwable cause) {
    super(message, cause);
  }
}
