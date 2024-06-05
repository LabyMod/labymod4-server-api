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

  private Permission(@NotNull String identifier) {
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

    if (!(o instanceof Permission that)) {
      return false;
    }

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

  public record StatedPermission(Permission permission, boolean allowed) {

  }
}
