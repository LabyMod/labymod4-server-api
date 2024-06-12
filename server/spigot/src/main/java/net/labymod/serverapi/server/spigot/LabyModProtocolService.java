package net.labymod.serverapi.server.spigot;

import net.labymod.serverapi.core.packet.serverbound.login.VersionLoginPacket;
import net.labymod.serverapi.protocol.logger.NoOpProtocolPlatformLogger;
import net.labymod.serverapi.protocol.logger.ProtocolPlatformLogger;
import net.labymod.serverapi.protocol.payload.PayloadChannelIdentifier;
import net.labymod.serverapi.protocol.payload.io.PayloadWriter;
import net.labymod.serverapi.server.common.AbstractServerLabyModProtocolService;
import net.labymod.serverapi.server.spigot.handler.DefaultVersionLoginPacketHandler;
import net.labymod.serverapi.server.spigot.listener.DefaultPlayerQuitListener;
import net.labymod.serverapi.server.spigot.listener.DefaultPluginMessageListener;
import net.labymod.serverapi.server.spigot.player.LabyModPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class LabyModProtocolService extends AbstractServerLabyModProtocolService<LabyModPlayer> {

  private static final LabyModProtocolService INSTANCE = new LabyModProtocolService();
  private ProtocolPlatformLogger logger;
  private JavaPlugin plugin;

  private LabyModProtocolService() {
    super(Side.SERVER);
    this.logger = NoOpProtocolPlatformLogger.get();
  }

  /**
   * @return the instance of the protocol service
   */
  public static LabyModProtocolService get() {
    return INSTANCE;
  }

  /**
   * Initializes the protocol services. Can only be done once.
   *
   * @param javaPlugin the plugin
   * @throws IllegalStateException if the plugin is already set
   * @throws NullPointerException  if the plugin is null
   */
  public static void initialize(@NotNull JavaPlugin javaPlugin) {
    LabyModProtocolService.get().initializePlugin(javaPlugin);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void send(
      @NotNull PayloadChannelIdentifier identifier,
      @NotNull UUID recipient,
      @NotNull PayloadWriter writer
  ) {
    Objects.requireNonNull(identifier, "Identifier cannot be null");
    Objects.requireNonNull(recipient, "Recipient cannot be null");
    Objects.requireNonNull(writer, "Writer cannot be null");
    if (this.plugin == null) {
      throw new IllegalStateException("The plugin instance is not set yet");
    }

    Player player = this.plugin.getServer().getPlayer(recipient);
    if (player == null) {
      return;
    }

    player.sendPluginMessage(
        this.plugin,
        identifier.toString(),
        writer.toByteArray()
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public @NotNull ProtocolPlatformLogger logger() {
    return this.logger;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInitialized() {
    return this.plugin != null;
  }

  private void initializePlugin(@NotNull JavaPlugin javaPlugin) {
    Objects.requireNonNull(javaPlugin, "JavaPlugin cannot be null");
    if (this.plugin != null) {
      throw new IllegalStateException("The plugin instance is already set");
    }

    this.plugin = javaPlugin;
    this.logger = new LabyModSpigotProtocolLogger(javaPlugin.getLogger());
    Messenger messenger = this.plugin.getServer().getMessenger();
    this.registry().addRegisterListener(protocol -> {
      String identifier = protocol.identifier().toString();
      messenger.registerOutgoingPluginChannel(this.plugin, identifier);
      messenger.registerIncomingPluginChannel(this.plugin, identifier,
          new DefaultPluginMessageListener(this));
    });

    this.initializeManaged();
  }

  private void initializeManaged() {
    this.labyModProtocol.registerHandler(
        VersionLoginPacket.class,
        new DefaultVersionLoginPacketHandler(this, this.players, this.plugin)
    );

    this.plugin.getServer().getPluginManager().registerEvents(
        new DefaultPlayerQuitListener(this.players),
        this.plugin
    );
  }
}
