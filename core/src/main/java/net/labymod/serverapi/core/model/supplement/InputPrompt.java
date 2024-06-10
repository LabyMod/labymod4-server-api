package net.labymod.serverapi.core.model.supplement;

import net.labymod.serverapi.protocol.model.component.ServerAPIComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class InputPrompt {

  private final ServerAPIComponent title;
  private final ServerAPIComponent placeholder;
  private final String defaultValue;
  private final int maxLength;

  //todo: maybe also add minlength?

  protected InputPrompt(
      @NotNull ServerAPIComponent title,
      @Nullable ServerAPIComponent placeholder,
      @Nullable String defaultValue,
      int maxLength
  ) {
    Objects.requireNonNull(title, "Title cannot be null");
    this.title = title;
    this.placeholder = placeholder;
    this.defaultValue = defaultValue;
    if (maxLength < 1) {
      maxLength = 100;
    }

    this.maxLength = maxLength;
  }

  /**
   * Creates a new input prompt instance.
   *
   * @param title        the title of the input prompt
   * @param placeholder  the placeholder of the input prompt text field
   * @param defaultValue the default value of the input prompt text field
   * @param maxLength    the maximum length of the input prompt text field. If negative or zero, the
   *                     max length will be set to 100.
   * @return a new input prompt instance
   */
  public static InputPrompt create(
      @NotNull ServerAPIComponent title,
      @Nullable ServerAPIComponent placeholder,
      @Nullable String defaultValue,
      int maxLength
  ) {
    return new InputPrompt(title, placeholder, defaultValue, maxLength);
  }

  /**
   * @return a new {@link InputPromptBuilder} instance
   */
  public static InputPromptBuilder builder() {
    return new InputPromptBuilder();
  }

  /**
   * @return the title of the input prompt
   */
  public @NotNull ServerAPIComponent title() {
    return this.title;
  }

  /**
   * @return the placeholder of the input prompt text field
   */
  public @Nullable ServerAPIComponent getPlaceholder() {
    return this.placeholder;
  }

  /**
   * @return the default value of the input prompt text field.
   */
  public @Nullable String getDefaultValue() {
    return this.defaultValue;
  }

  /**
   * @return the maximum length of the input prompt text field.
   */
  public int getMaxLength() {
    return this.maxLength;
  }

  @Override
  public String toString() {
    return "InputPrompt{" +
        "title=" + this.title +
        ", placeholder=" + this.placeholder +
        ", defaultValue='" + this.defaultValue + '\'' +
        ", maxLength=" + this.maxLength +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof InputPrompt that)) {
      return false;
    }

    return this.maxLength == that.maxLength
        && Objects.equals(this.title, that.title)
        && Objects.equals(this.placeholder, that.placeholder)
        && Objects.equals(this.defaultValue, that.defaultValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.title, this.placeholder, this.defaultValue, this.maxLength);
  }

  public static class InputPromptBuilder {

    private ServerAPIComponent title;
    private ServerAPIComponent placeholder;
    private String defaultValue;
    private int maxLength;

    private InputPromptBuilder() {
      // should only be created via InputPrompt.builder()
    }

    /**
     * Sets the title of the input prompt.
     *
     * @param title the title of the input prompt
     * @return the current builder instance
     */
    public InputPromptBuilder title(@NotNull ServerAPIComponent title) {
      this.title = title;
      return this;
    }

    /**
     * Sets the placeholder of the input prompt text field.
     *
     * @param placeholder the placeholder of the input prompt text field
     * @return the current builder instance
     */
    public InputPromptBuilder placeholder(@Nullable ServerAPIComponent placeholder) {
      this.placeholder = placeholder;
      return this;
    }

    /**
     * Sets the default value of the input prompt text field.
     *
     * @param defaultValue the default value of the input prompt text field
     * @return the current builder instance
     */
    public InputPromptBuilder defaultValue(@Nullable String defaultValue) {
      this.defaultValue = defaultValue;
      return this;
    }

    /**
     * Sets the maximum length of the input prompt text field. If negative or zero, the max
     * length will be set to 100.
     *
     * @param maxLength the maximum length of the input prompt text field
     * @return the current builder instance
     */
    public InputPromptBuilder maxLength(int maxLength) {
      this.maxLength = maxLength;
      return this;
    }

    /**
     * @return a new {@link InputPrompt} instance, based on the provided values
     */
    public @NotNull InputPrompt build() {
      return new InputPrompt(this.title, this.placeholder, this.defaultValue, this.maxLength);
    }
  }
}
