package net.labymod.serverapi.core.model.moderation;

import java.util.List;

public class InstalledAddons {

  protected final List<String> enabledAddons;
  protected final List<String> disabledAddons;

  public InstalledAddons(List<String> enabledAddons, List<String> disabledAddons) {
    this.enabledAddons = enabledAddons;
    this.disabledAddons = disabledAddons;
  }

  public boolean isInstalled(String namespace) {
    return this.isEnabled(namespace) || this.isDisabled(namespace);
  }

  public boolean isEnabled(String namespace) {
    return this.enabledAddons.contains(namespace);
  }

  public boolean isDisabled(String namespace) {
    return this.disabledAddons.contains(namespace);
  }

  public List<String> getEnabled() {
    return this.enabledAddons;
  }

  public List<String> getDisabled() {
    return this.disabledAddons;
  }

  @Override
  public String toString() {
    return "InstalledAddons{" +
        "enabledAddons=" + this.enabledAddons +
        ", disabledAddons=" + this.disabledAddons +
        '}';
  }
}
