package net.labymod.serverapi.protocol.payload.exception;

public class PayloadWriterException extends PayloadException {

  public PayloadWriterException() {
  }

  public PayloadWriterException(String message) {
    super(message);
  }

  public PayloadWriterException(String message, Throwable cause) {
    super(message, cause);
  }

  public PayloadWriterException(Throwable cause) {
    super(cause);
  }

  public PayloadWriterException(
      String message,
      Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace
  ) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
