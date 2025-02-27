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

package net.labymod.serverapi.api.logger;

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
