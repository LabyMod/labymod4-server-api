package net.labymod.serverapi.core;

import net.labymod.serverapi.api.AbstractProtocolService;
import net.labymod.serverapi.core.integration.LabyModProtocolIntegration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

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

  private @NotNull ClassLoader getIntegrationClassLoader() {
    return this.getClass().getClassLoader();
  }

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
}
