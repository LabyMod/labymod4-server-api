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

package net.labymod.serverapi.server.bungeecord.listener;

import net.labymod.serverapi.api.Protocol;
import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class DefaultPluginMessageListener implements Listener {

  private final Protocol protocol;

  public DefaultPluginMessageListener(Protocol protocol) {
    this.protocol = protocol;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPluginMessage(PluginMessageEvent event) {
    if (!(event.getSender() instanceof ProxiedPlayer player)) {
      return;
    }

    String channel = event.getTag();
    if (!channel.equals(this.protocol.identifier().toString())) {
      return;
    }

    try {
      PayloadReader reader = new PayloadReader(event.getData());
      Packet packet = this.protocol.handleIncomingPayload(player.getUniqueId(), reader);
      if (packet != null) {
        event.setCancelled(true);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
