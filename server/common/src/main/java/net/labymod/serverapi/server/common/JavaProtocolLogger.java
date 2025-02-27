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

package net.labymod.serverapi.server.common;

import net.labymod.serverapi.api.logger.ProtocolPlatformLogger;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaProtocolLogger implements ProtocolPlatformLogger {

  private final Logger logger;

  public JavaProtocolLogger(Logger logger) {
    this.logger = logger;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void info(@NotNull String message, Object... objects) {
    this.logger.log(Level.INFO, message, objects);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void info(@NotNull String message, Throwable throwable) {
    this.logger.log(Level.INFO, message, throwable);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void warn(@NotNull String message, Object... objects) {
    this.logger.log(Level.WARNING, message, objects);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void warn(@NotNull String message, Throwable throwable) {
    this.logger.log(Level.WARNING, message, throwable);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void error(@NotNull String message, Object... objects) {
    this.logger.log(Level.SEVERE, message, objects);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void error(@NotNull String message, Throwable throwable) {
    this.logger.log(Level.SEVERE, message, throwable);
  }

  @Override
  public void debug(@NotNull String message, Object... objects) {
    this.logger.log(Level.INFO, "DEBUG " + message, objects);
  }

  @Override
  public void debug(@NotNull String message, Throwable throwable) {
    this.logger.log(Level.INFO, "DEBUG " + message, throwable);
  }
}
