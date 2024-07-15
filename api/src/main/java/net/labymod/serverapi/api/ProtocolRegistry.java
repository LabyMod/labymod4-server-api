/*
 * MIT License
 *
 * Copyright (c) 2024 LabyMedia GmbH
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

package net.labymod.serverapi.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Represents the registry for all protocols.
 */
public class ProtocolRegistry {

  private final Set<Protocol> protocols;
  private final Set<Protocol> unmodifiableProtocols;
  private final Set<Consumer<Protocol>> registerListeners;

  protected ProtocolRegistry() {
    this.protocols = new HashSet<>();
    this.unmodifiableProtocols = Collections.unmodifiableSet(this.protocols);
    this.registerListeners = new HashSet<>();
  }

  /**
   * Registers a protocol to the registry.
   *
   * @param protocol The protocol to register.
   * @throws NullPointerException If the param protocol is null.
   */
  public void registerProtocol(@NotNull Protocol protocol) {
    Objects.requireNonNull(protocol, "Protocol cannot be null");
    this.protocols.add(protocol);
    System.out.println("Registered protocol: " + protocol.identifier());

    for (Consumer<Protocol> registerListener : this.registerListeners) {
      registerListener.accept(protocol);
    }
  }

  /**
   * Unregisters a protocol from the registry.
   *
   * @param protocol The protocol to unregister.
   * @throws NullPointerException If the param protocol is null.
   */
  public void unregisterProtocol(@NotNull Protocol protocol) {
    Objects.requireNonNull(protocol, "Protocol cannot be null");
    this.protocols.remove(protocol);
  }

  /**
   * Adds a listener that will be called when a new protocol is registered. Also calls the
   * listener for all currently registered protocols.
   *
   * @param listener The listener to add.
   * @throws NullPointerException If the param listener is null.
   */
  public void addRegisterListener(@NotNull Consumer<Protocol> listener) {
    Objects.requireNonNull(listener, "Listener cannot be null");
    this.registerListeners.add(listener);
    for (Protocol protocol : this.protocols) {
      listener.accept(protocol);
    }
  }

  /**
   * @return An unmodifiable set of all registered protocols.
   */
  public @Unmodifiable Set<Protocol> getProtocols() {
    return this.unmodifiableProtocols;
  }
}
