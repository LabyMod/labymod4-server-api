package net.labymod.serverapi.server.minestom;

import net.labymod.serverapi.api.logger.ProtocolPlatformLogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class Slf4jPlatformLogger implements ProtocolPlatformLogger {

    private final Logger logger;

    public Slf4jPlatformLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(@NotNull String s, Object... objects) {
        this.logger.info(s, objects);
    }

    @Override
    public void info(@NotNull String s, Throwable throwable) {
        this.logger.info(s, throwable);
    }

    @Override
    public void warn(@NotNull String s, Object... objects) {
        this.logger.warn(s, objects);
    }

    @Override
    public void warn(@NotNull String s, Throwable throwable) {
        this.logger.warn(s, throwable);
    }

    @Override
    public void error(@NotNull String s, Object... objects) {
        this.logger.error(s, objects);
    }

    @Override
    public void error(@NotNull String s, Throwable throwable) {
        this.logger.error(s, throwable);
    }

    @Override
    public void debug(@NotNull String message, Object... objects) {
        this.logger.debug(message, objects);
    }

    @Override
    public void debug(@NotNull String message, Throwable throwable) {
        this.logger.debug(message, throwable);
    }
}
