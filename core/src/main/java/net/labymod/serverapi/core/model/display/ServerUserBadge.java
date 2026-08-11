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

package net.labymod.serverapi.core.model.display;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class ServerUserBadge {

    private final UUID uniqueId;
    private final int[] badgeIds;

    private ServerUserBadge(@NotNull UUID uniqueId, int[] badgeIds) {
        Objects.requireNonNull(uniqueId, "Unique id cannot be null");
        this.uniqueId = uniqueId;
        this.badgeIds = badgeIds;
    }

    public static ServerUserBadge create(
            @NotNull UUID uniqueId,
            int... badgeIds
    ) {
        return new ServerUserBadge(uniqueId, badgeIds);
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public int[] getBadgeIds() {
        return this.badgeIds;
    }

    @Override
    public String toString() {
        return "ServerUserBadge{" +
                "uniqueId=" + this.uniqueId +
                ", badgeIds=" + Arrays.toString(this.badgeIds) +
                '}';
    }
}