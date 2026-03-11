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

package net.labymod.serverapi.core.model.feature;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Feature {

  private static final Map<String, Feature> FEATURES = new HashMap<>();
  public static final Feature FANCY_FONT = Feature.of("fancy_font");

  private final String identifier;
  private StatedFeature enabledFeature;
  private StatedFeature disabledFeature;

  protected Feature(@NotNull String identifier) {
    this.identifier = identifier;
  }

  /**
   * Gets or creates a feature with the given identifier.
   *
   * @param identifier The identifier of the feature.
   * @return The feature.
   */
  public static Feature of(@NotNull String identifier) {
    Objects.requireNonNull(identifier, "Identifier");
    return FEATURES.computeIfAbsent(identifier, Feature::new);
  }

  /**
   * @return The identifier of the feature.
   */
  public @NotNull String getIdentifier() {
    return this.identifier;
  }

  public StatedFeature enable() {
    if (this.enabledFeature == null) {
      this.enabledFeature = new StatedFeature(this, true);
    }

    return this.enabledFeature;
  }

  public StatedFeature disable() {
    if (this.disabledFeature == null) {
      this.disabledFeature = new StatedFeature(this, false);
    }

    return this.disabledFeature;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Feature)) {
      return false;
    }

    Feature that = (Feature) o;
    return this.identifier.equals(that.identifier);
  }

  @Override
  public int hashCode() {
    return this.identifier.hashCode();
  }

  @Override
  public String toString() {
    return "Feature{" + "identifier='" + this.identifier + '\'' + '}';
  }

  public static class StatedFeature {

    private final Feature feature;
    private final boolean enabled;

    private StatedFeature(@NotNull Feature feature, boolean enabled) {
      this.feature = feature;
      this.enabled = enabled;
    }

    public @NotNull Feature feature() {
      return this.feature;
    }

    public boolean isEnabled() {
      return this.enabled;
    }

    @Override
    public String toString() {
      return "StatedFeature{" +
          "feature=" + this.feature +
          ", enabled=" + this.enabled +
          '}';
    }
  }
}
