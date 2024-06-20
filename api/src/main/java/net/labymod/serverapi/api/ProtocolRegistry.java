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
