package net.labymod.serverapi.core;

import net.labymod.serverapi.api.AbstractProtocolService;
import net.labymod.serverapi.api.Protocol;
import net.labymod.serverapi.api.payload.PayloadChannelIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AddonProtocol extends Protocol {

  private final String namespace;

  /**
   * Creates a new addon protocol.
   *
   * @param protocolService The protocol service.
   * @param namespace       The namespace of the addon.
   * @throws NullPointerException If the namespace or protocol service is null.
   */
  public AddonProtocol(
      @NotNull AbstractProtocolService protocolService,
      @NotNull String namespace
  ) {
    super(
        protocolService,
        PayloadChannelIdentifier.create("labymod", "neo/addon/" + namespace)
    );
    Objects.requireNonNull(namespace, "Namespace cannot be null");
    this.namespace = namespace;
  }

  public @NotNull String getNamespace() {
    return this.namespace;
  }
}
