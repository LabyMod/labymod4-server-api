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

import java.awt.*;

public class ServerBadge {

    private final int badgeId;
    private final Color color;
    private final String iconUrl;

    private ServerBadge(int badgeId, Color color, String iconUrl) {
        this.badgeId = badgeId;
        this.color = color;
        this.iconUrl = iconUrl;
    }

    public static ServerBadge create(
            int badgeId,
            Color color,
            String url
    ) {
        return new ServerBadge(badgeId, color, url);
    }

    public int getBadgeId() {
        return this.badgeId;
    }

    public Color getColor() {
        return this.color;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    @Override
    public String toString() {
        return "ServerBadge{" +
                "badgeId=" + this.badgeId +
                ", color=" + this.color +
                ", iconUrl='" + this.iconUrl + '\'' +
                '}';
    }
}