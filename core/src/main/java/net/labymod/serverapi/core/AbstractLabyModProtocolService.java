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

package net.labymod.serverapi.core;

import net.labymod.serverapi.api.AbstractProtocolService;
import net.labymod.serverapi.core.integration.LabyModProtocolIntegration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

public abstract class AbstractLabyModProtocolService extends AbstractProtocolService {

  protected final LabyModProtocol labyModProtocol;
  protected final Set<LabyModProtocolIntegration> integrations = new HashSet<>();

  /**
   * Creates a new labymod protocol service.
   *
   * @param side the side the protocol service is running on. {@link Side#CLIENT} for client-side
   *             and {@link Side#SERVER} for server-side
   */
  protected AbstractLabyModProtocolService(@NotNull Side side) {
    super(side);
    this.labyModProtocol = new LabyModProtocol(this);
    this.registry().registerProtocol(this.labyModProtocol);
    this.loadLabyModProtocolIntegrations(null);
  }

  /**
   * @return the LabyMod protocol
   */
  public final LabyModProtocol labyModProtocol() {
    return this.labyModProtocol;
  }

  /**
   * @return the class loader to load the integrations from if no class loader was specified
   */
  private @NotNull ClassLoader getIntegrationClassLoader() {
    return this.getClass().getClassLoader();
  }

  /**
   * Gets the integration implementation of the given class.
   *
   * @param integrationClass the integration class
   * @param <T>              the type of the integration
   * @return the integration implementation or null if not found
   */
  public final @Nullable <T extends LabyModProtocolIntegration> T getIntegration(
      @NotNull Class<T> integrationClass
  ) {
    for (LabyModProtocolIntegration integration : this.integrations) {
      if (integration.getClass() == integrationClass) {
        return (T) integration;
      }
    }

    return null;
  }

  /**
   * Gets the integration implementation of the given class or registers a new one if not found.
   *
   * @param integrationClass    the integration class
   * @param integrationSupplier the supplier to create a new integration
   * @param <T>                 the type of the integration
   * @return the integration implementation or null if not found
   */
  public final @NotNull <T extends LabyModProtocolIntegration> T getOrRegisterIntegration(
      @NotNull Class<T> integrationClass,
      @NotNull Supplier<@NotNull T> integrationSupplier
  ) {
    T integration = this.getIntegration(integrationClass);
    if (integration != null) {
      return integration;
    }

    integration = integrationSupplier.get();
    Objects.requireNonNull(integration, "integrationSupplier must not return null");

    integration.initialize(this);
    this.integrations.add(integration);
    return integration;
  }

  /**
   * Loads integrations from the specified class loader.
   *
   * @param classLoader the class loader to load the integrations from. If {@code null}, the
   *                    class loader returned by {@link #getIntegrationClassLoader()} is used.
   */
  protected void loadLabyModProtocolIntegrations(@Nullable ClassLoader classLoader) {
    System.out.println("Loading LabyMod Protocol Integrations...");
    ServiceLoader<LabyModProtocolIntegration> serviceLoader;
    if (classLoader == null) {
      serviceLoader = ServiceLoader.load(
          LabyModProtocolIntegration.class,
          this.getIntegrationClassLoader()
      );
    } else {
      serviceLoader = ServiceLoader.load(LabyModProtocolIntegration.class, classLoader);
    }

    int found = 0;
    for (LabyModProtocolIntegration labyModProtocolIntegration : serviceLoader) {
      try {
        System.out.println(
            "Loading LabyModProtocolIntegration: " + labyModProtocolIntegration.getClass()
                .getName());
        labyModProtocolIntegration.initialize(this);
        this.integrations.add(labyModProtocolIntegration);
        System.out.println(
            "Loaded LabyModProtocolIntegration: " + labyModProtocolIntegration.getClass()
                .getName());
        found++;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    System.out.println("Found and loaded" + found + " Integrations.");
  }

  /**
   * Gets the player by the supplied unique id.
   *
   * @param uniqueId the unique id of the player
   * @return the player or null if not found
   */
  public @Nullable AbstractLabyModPlayer getPlayer(@NotNull UUID uniqueId) {
    return null;
  }

  /**
   * Evaluates whether the player with the supplied unique id is using LabyMod.
   *
   * @param uniqueId the unique id of the player
   * @return {@code true} if the player is using LabyMod, otherwise {@code false}
   */
  public boolean isUsingLabyMod(@NotNull UUID uniqueId) {
    return false;
  }

  /**
   * @return a collection of all players
   */
  public Collection<? extends AbstractLabyModPlayer> getPlayers() {
    return Collections.emptyList();
  }
}
