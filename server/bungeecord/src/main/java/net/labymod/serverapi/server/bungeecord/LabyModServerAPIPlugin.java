package net.labymod.serverapi.server.bungeecord;

import net.md_5.bungee.api.plugin.Plugin;

public class LabyModServerAPIPlugin extends Plugin {

  @Override
  public void onEnable() {
    LabyModProtocolService.initialize(this);
  }
}
