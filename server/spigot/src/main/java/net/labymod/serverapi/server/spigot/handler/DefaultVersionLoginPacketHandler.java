package net.labymod.serverapi.server.spigot.handler;

import net.labymod.serverapi.api.packet.PacketHandler;
import net.labymod.serverapi.core.packet.serverbound.login.VersionLoginPacket;
import net.labymod.serverapi.server.spigot.LabyModProtocolService;
import net.labymod.serverapi.server.spigot.event.LabyModPlayerJoinEvent;
import net.labymod.serverapi.server.spigot.player.LabyModPlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class DefaultVersionLoginPacketHandler implements PacketHandler<VersionLoginPacket> {

  private final LabyModProtocolService protocolService;
  private final Map<UUID, LabyModPlayer> players;
  private final JavaPlugin javaPlugin;

  public DefaultVersionLoginPacketHandler(
      LabyModProtocolService protocolService,
      Map<UUID, LabyModPlayer> players,
      JavaPlugin javaPlugin
  ) {
    this.protocolService = protocolService;
    this.players = players;
    this.javaPlugin = javaPlugin;
  }

  @Override
  public void handle(@NotNull UUID uuid, @NotNull VersionLoginPacket versionLoginPacket) {
    final LabyModPlayer existingLabyModPlayer = this.players.get(uuid);
    if (existingLabyModPlayer != null) {
      // warn
      return;
    }

    Server server = this.javaPlugin.getServer();
    final Player player = server.getPlayer(uuid);
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
    server.getPluginManager().callEvent(new LabyModPlayerJoinEvent(
        this.protocolService,
        labyModPlayer
    ));
  }
}
