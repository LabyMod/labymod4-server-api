/*
 * MIT License
 *
 * Copyright (c) 2025 LabyMedia GmbH
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

package net.labymod.serverapi.core.packet.clientbound.game.feature;

import net.labymod.serverapi.api.packet.Packet;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import net.labymod.serverapi.api.payload.io.PayloadWriter;
import net.labymod.serverapi.core.model.display.ServerUserBadge;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ServerUserBadgePacket implements Packet {

    private List<ServerUserBadge> badges;

    public ServerUserBadgePacket(List<ServerUserBadge> flags) {
        this.badges = flags;
    }

    public ServerUserBadgePacket(ServerUserBadge... flags) {
        this.badges = Collections.unmodifiableList(Arrays.asList(flags));
    }

    @Override
    public void read(@NotNull PayloadReader reader) {
        this.badges = reader.readList(() -> {
            UUID uuid = reader.readUUID();
            int badgeCount = reader.readVarInt();
            int[] badgeIds = new int[badgeCount];
            for (int i = 0; i < badgeCount; i++) {
                badgeIds[i] = reader.readVarInt();
            }
            return ServerUserBadge.create(uuid, badgeIds);
        });
    }

    @Override
    public void write(@NotNull PayloadWriter writer) {
        writer.writeCollection(this.badges, (badge) -> {
            writer.writeUUID(badge.getUniqueId());

            int[] badgeIds = badge.getBadgeIds();
            writer.writeVarInt(badgeIds.length);
            for (int badgeId : badgeIds) {
                writer.writeVarInt(badgeId);
            }
        });
    }

    public @NotNull List<ServerUserBadge> getBadges() {
        return this.badges;
    }

    @Override
    public String toString() {
        return "ServerBadgePacket{" +
                "badges=" + this.badges +
                '}';
    }
}
