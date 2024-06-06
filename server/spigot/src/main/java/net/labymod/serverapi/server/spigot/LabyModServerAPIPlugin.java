package net.labymod.serverapi.server.spigot;

import org.bukkit.plugin.java.JavaPlugin;

public final class LabyModServerAPIPlugin extends JavaPlugin {

  @Override
  public void onEnable() {
    LabyModProtocolService.initialize(this);
  }
}
