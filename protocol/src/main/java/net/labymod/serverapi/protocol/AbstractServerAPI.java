package net.labymod.serverapi.protocol;

import net.labymod.serverapi.protocol.protocol.AbstractProtocolService;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class AbstractServerAPI<T extends AbstractProtocolService> {

  protected final T protocolService;

  protected AbstractServerAPI(@NotNull T protocolService) {
    Objects.requireNonNull(protocolService, "Protocol service cannot be null");
    this.protocolService = protocolService;
  }

  public T protocolService() {
    return this.protocolService;
  }
}
