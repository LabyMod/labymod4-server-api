package net.labymod.serverapi.server.bukkit;

import net.labymod.serverapi.api.logger.ProtocolPlatformLogger;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

class LabyModBukkitProtocolLogger implements ProtocolPlatformLogger {

  private final Logger logger;

  protected LabyModBukkitProtocolLogger(Logger logger) {
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
