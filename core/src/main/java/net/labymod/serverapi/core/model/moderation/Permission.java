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

package net.labymod.serverapi.core.model.moderation;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Permission {

  private static final Map<String, Permission> PERMISSIONS = new HashMap<>();
  public static final Permission CHAT_AUTOTEXT = Permission.of("chat_autotext");

  private final String identifier;
  private StatedPermission allowedPermission;
  private StatedPermission deniedPermission;

  protected Permission(@NotNull String identifier) {
    this.identifier = identifier;
  }

  /**
   * Creates a new permission with the given identifier.
   *
   * @param identifier The identifier of the permission.
   * @return The created permission.
   */
  public static Permission of(@NotNull String identifier) {
    Objects.requireNonNull(identifier, "Identifier");
    return PERMISSIONS.computeIfAbsent(identifier, Permission::new);
  }

  /**
   * @return The identifier of the permission.
   */
  public @NotNull String getIdentifier() {
    return this.identifier;
  }

  public StatedPermission allow() {
    if (this.allowedPermission == null) {
      this.allowedPermission = new StatedPermission(this, true);
    }

    return this.allowedPermission;
  }

  public StatedPermission deny() {
    if (this.deniedPermission == null) {
      this.deniedPermission = new StatedPermission(this, false);
    }

    return this.deniedPermission;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Permission)) {
      return false;
    }

    Permission that = (Permission) o;
    return this.identifier.equals(that.identifier);
  }

  @Override
  public int hashCode() {
    return this.identifier.hashCode();
  }

  @Override
  public String toString() {
    return "Permission{" + "identifier='" + this.identifier + '\'' + '}';
  }

  public static class StatedPermission {

    private final Permission permission;
    private final boolean allowed;

    private StatedPermission(@NotNull Permission permission, boolean allowed) {
      this.permission = permission;
      this.allowed = allowed;
    }

    public @NotNull Permission permission() {
      return this.permission;
    }

    public boolean isAllowed() {
      return this.allowed;
    }

    /**
     * @deprecated use {@link #isAllowed()}
     */
    @Deprecated
    public boolean allowed() {
      return this.allowed;
    }

    @Override
    public String toString() {
      return "StatedPermission{" +
          "permission=" + this.permission +
          ", allowed=" + this.allowed +
          '}';
    }
  }
}
