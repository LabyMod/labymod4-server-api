package net.labymod.serverapi.api.logger;

import org.jetbrains.annotations.NotNull;

public class NoOpProtocolPlatformLogger implements ProtocolPlatformLogger {

  private static final NoOpProtocolPlatformLogger INSTANCE = new NoOpProtocolPlatformLogger();

  private NoOpProtocolPlatformLogger() {
    // private constructor
  }

  public static NoOpProtocolPlatformLogger get() {
    return INSTANCE;
  }

  @Override
  public void info(@NotNull String message, Object... objects) {
    // NO-OP
  }

  @Override
  public void info(@NotNull String message, Throwable throwable) {
    // NO-OP
  }

  @Override
  public void warn(@NotNull String message, Object... objects) {
    // NO-OP
  }

  @Override
  public void warn(@NotNull String message, Throwable throwable) {
    // NO-OP
  }

  @Override
  public void error(@NotNull String message, Object... objects) {
    // NO-OP
  }

  @Override
  public void error(@NotNull String message, Throwable throwable) {
    // NO-OP
  }
}
