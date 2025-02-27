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
