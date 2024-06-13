package net.labymod.serverapi.api.payload.exception;

public class PayloadException extends RuntimeException {

  public PayloadException() {
  }

  public PayloadException(String message) {
    super(message);
  }

  public PayloadException(String message, Throwable cause) {
    super(message, cause);
  }

  public PayloadException(Throwable cause) {
    super(cause);
  }

  public PayloadException(
      String message,
      Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace
  ) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
