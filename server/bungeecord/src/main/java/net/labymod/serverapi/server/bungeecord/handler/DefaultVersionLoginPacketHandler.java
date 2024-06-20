package net.labymod.serverapi.server.bungeecord.handler;

import net.labymod.serverapi.api.packet.PacketHandler;
import net.labymod.serverapi.core.packet.serverbound.login.VersionLoginPacket;
import net.labymod.serverapi.server.bungeecord.LabyModPlayer;
import net.labymod.serverapi.server.bungeecord.LabyModProtocolService;
import net.labymod.serverapi.server.bungeecord.event.LabyModPlayerJoinEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class DefaultVersionLoginPacketHandler implements PacketHandler<VersionLoginPacket> {

  private final LabyModProtocolService protocolService;
  private final Map<UUID, LabyModPlayer> players;
  private final Plugin plugin;

  public DefaultVersionLoginPacketHandler(
      LabyModProtocolService protocolService,
      Map<UUID, LabyModPlayer> players,
      Plugin plugin
  ) {
    this.protocolService = protocolService;
    this.players = players;
    this.plugin = plugin;
  }

  @Override
  public void handle(@NotNull UUID uuid, @NotNull VersionLoginPacket versionLoginPacket) {
    final LabyModPlayer existingLabyModPlayer = this.players.get(uuid);
    if (existingLabyModPlayer != null) {
      // warn
      return;
    }

    ProxyServer proxy = this.plugin.getProxy();
    final ProxiedPlayer player = proxy.getPlayer(uuid);
    if (player == null) {
      return;
    }

    final LabyModPlayer labyModPlayer = new LabyModPlayer(
        this.protocolService,
        uuid,
        player,
        versionLoginPacket.getVersion()
    );

    this.players.put(uuid, labyModPlayer);
    proxy.getPluginManager().callEvent(new LabyModPlayerJoinEvent(
        this.protocolService,
        labyModPlayer
    ));
  }
}
