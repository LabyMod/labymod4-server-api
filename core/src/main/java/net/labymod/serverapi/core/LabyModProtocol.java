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

package net.labymod.serverapi.core;

import net.labymod.serverapi.api.Protocol;
import net.labymod.serverapi.api.packet.Direction;
import net.labymod.serverapi.api.payload.PayloadChannelIdentifier;
import net.labymod.serverapi.core.packet.clientbound.game.display.EconomyDisplayPacket;
import net.labymod.serverapi.core.packet.clientbound.game.display.SubtitlePacket;
import net.labymod.serverapi.core.packet.clientbound.game.display.TabListBannerPacket;
import net.labymod.serverapi.core.packet.clientbound.game.display.TabListFlagPacket;
import net.labymod.serverapi.core.packet.clientbound.game.feature.DiscordRPCPacket;
import net.labymod.serverapi.core.packet.clientbound.game.feature.EmotePacket;
import net.labymod.serverapi.core.packet.clientbound.game.feature.InteractionMenuPacket;
import net.labymod.serverapi.core.packet.clientbound.game.feature.PlayingGameModePacket;
import net.labymod.serverapi.core.packet.clientbound.game.feature.marker.AddMarkerPacket;
import net.labymod.serverapi.core.packet.clientbound.game.feature.marker.MarkerPacket;
import net.labymod.serverapi.core.packet.clientbound.game.moderation.AddonRecommendationPacket;
import net.labymod.serverapi.core.packet.clientbound.game.moderation.PermissionPacket;
import net.labymod.serverapi.core.packet.clientbound.game.supplement.InputPromptPacket;
import net.labymod.serverapi.core.packet.clientbound.game.supplement.ServerSwitchPromptPacket;
import net.labymod.serverapi.core.packet.serverbound.game.feature.marker.ClientAddMarkerPacket;
import net.labymod.serverapi.core.packet.serverbound.game.moderation.AddonRecommendationResponsePacket;
import net.labymod.serverapi.core.packet.serverbound.game.supplement.InputPromptResponsePacket;
import net.labymod.serverapi.core.packet.serverbound.game.supplement.ServerSwitchPromptResponsePacket;
import net.labymod.serverapi.core.packet.serverbound.login.VersionLoginPacket;

public class LabyModProtocol extends Protocol {

  protected LabyModProtocol(AbstractLabyModProtocolService protocolService) {
    super(protocolService, PayloadChannelIdentifier.create("labymod", "neo"));
    this.registerPackets();
  }

  private void registerPackets() {
    this.registerPacket(0, VersionLoginPacket.class, Direction.SERVERBOUND);
    //this.registerPacket(1, AddonLoginPacket.class, Direction.SERVERBOUND);
    //this.registerPacket(2, ProtocolLoginPacket.class, Direction.SERVERBOUND);

    this.registerPacket(10, PermissionPacket.class, Direction.CLIENTBOUND);
    this.registerPacket(11, SubtitlePacket.class, Direction.CLIENTBOUND);
    this.registerPacket(12, PlayingGameModePacket.class, Direction.CLIENTBOUND);
    this.registerPacket(13, EconomyDisplayPacket.class, Direction.CLIENTBOUND);
    this.registerPacket(14, TabListFlagPacket.class, Direction.CLIENTBOUND);
    this.registerPacket(15, TabListBannerPacket.class, Direction.CLIENTBOUND);

    this.registerPacket(16, EmotePacket.class, Direction.CLIENTBOUND);
    //this.registerPacket(17, SprayPacket.class, Direction.CLIENTBOUND);
    this.registerPacket(18, DiscordRPCPacket.class, Direction.CLIENTBOUND);
    this.registerPacket(19, InteractionMenuPacket.class, Direction.CLIENTBOUND);

    this.registerPacket(20, ServerSwitchPromptPacket.class, Direction.CLIENTBOUND);
    this.registerPacket(21, ServerSwitchPromptResponsePacket.class, Direction.SERVERBOUND);
    this.registerPacket(22, InputPromptPacket.class, Direction.CLIENTBOUND);
    this.registerPacket(23, InputPromptResponsePacket.class, Direction.SERVERBOUND);
    //this.registerPacket(24, AddonDisablePacket.class, Direction.CLIENTBOUND);
    this.registerPacket(25, AddonRecommendationPacket.class, Direction.CLIENTBOUND);
    this.registerPacket(26, AddonRecommendationResponsePacket.class, Direction.SERVERBOUND);
    this.registerPacket(27, MarkerPacket.class, Direction.CLIENTBOUND);
    this.registerPacket(28, AddMarkerPacket.class, Direction.CLIENTBOUND);
    this.registerPacket(29, ClientAddMarkerPacket.class, Direction.SERVERBOUND);
    //this.registerPacket(30, ChatFilterPacket.class, Direction.CLIENTBOUND);
    //this.registerPacket(31, ChatFilterRemovePacket.class, Direction.CLIENTBOUND);
    //this.registerPacket(32, ServerEventPacket.class, Direction.CLIENTBOUND);
  }
}
