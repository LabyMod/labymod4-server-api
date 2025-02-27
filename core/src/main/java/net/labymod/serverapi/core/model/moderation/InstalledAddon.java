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

import java.util.Objects;

public class InstalledAddon {

  private final String namespace;
  private final AddonVersion version;
  private final boolean local;
  private boolean enabled;

  public InstalledAddon(String namespace, AddonVersion version, boolean enabled, boolean local) {
    this.namespace = namespace;
    this.version = version;
    this.enabled = enabled;
    this.local = local;
  }

  /**
   * @return the namespace of the addon
   */
  public String getNamespace() {
    return this.namespace;
  }

  /**
   * @return the version of the addon
   */
  public AddonVersion getVersion() {
    return this.version;
  }

  /**
   * @return whether the addon was downloaded from the addon store or not
   */
  public boolean isLocal() {
    return this.local;
  }

  /**
   * @return whether the addon is enabled or not
   */
  public boolean isEnabled() {
    return this.enabled;
  }

  /**
   * Sets the enabled state of the addon
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public String toString() {
    return "Addon{" +
        "namespace='" + this.namespace + '\'' +
        ", version=" + this.version +
        ", enabled=" + this.enabled +
        ", local=" + this.local +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof InstalledAddon)) {
      return false;
    }

    InstalledAddon that = (InstalledAddon) o;
    return Objects.equals(this.namespace, that.namespace);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.namespace);
  }

  public static class AddonVersion {

    private final int major;
    private final int minor;
    private final int patch;

    public AddonVersion(int major, int minor, int patch) {
      this.major = major;
      this.minor = minor;
      this.patch = patch;
    }

    public int getMajor() {
      return this.major;
    }

    public int getMinor() {
      return this.minor;
    }

    public int getPatch() {
      return this.patch;
    }

    @Override
    public String toString() {
      return this.major + "." + this.minor + "." + this.patch;
    }
  }
}
