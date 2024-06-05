package net.labymod.serverapi.protocol.logger;

import org.jetbrains.annotations.NotNull;

/**
 * Represents the logger of the server api
 */
public interface ProtocolPlatformLogger {

  /**
   * Logs the supplied message at the info level.
   *
   * @param message the message to log
   * @param objects the objects to replace in the message
   * @throws NullPointerException if the message is null
   */
  void info(@NotNull String message, Object... objects);

  /**
   * Logs the supplied message at the info level.
   *
   * @param message   the message to log
   * @param throwable the throwable to log
   * @throws NullPointerException if the message is null
   */
  void info(@NotNull String message, Throwable throwable);

  /**
   * Logs the supplied message at the warn level.
   *
   * @param message the message to log
   * @param objects the objects to replace in the message
   * @throws NullPointerException if the message is null
   */
  void warn(@NotNull String message, Object... objects);

  /**
   * Logs the supplied message at the warn level.
   *
   * @param message   the message to log
   * @param throwable the throwable to log
   * @throws NullPointerException if the message is null
   */
  void warn(@NotNull String message, Throwable throwable);

  /**
   * Logs the supplied message at the error level.
   *
   * @param message the message to log
   * @param objects the objects to replace in the message
   * @throws NullPointerException if the message is null
   */
  void error(@NotNull String message, Object... objects);

  /**
   * Logs the supplied message at the error level.
   *
   * @param message   the message to log
   * @param throwable the throwable to log
   * @throws NullPointerException if the message is null
   */
  void error(@NotNull String message, Throwable throwable);

  /**
   * Logs the supplied message at the debug level. Has no default implementation.
   *
   * @param message the message to log
   * @param objects the objects to replace in the message
   */
  default void debug(@NotNull String message, Object... objects) {
    // NO-OP
  }

  /**
   * Logs the supplied message at the debug level. Has no default implementation.
   *
   * @param message   the message to log
   * @param throwable the throwable to log
   */
  default void debug(@NotNull String message, Throwable throwable) {
    // NO-OP
  }
}
