package net.labymod.serverapi.core.model.supplement;

import net.labymod.serverapi.protocol.model.component.ServerAPIComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ServerSwitchPrompt {

  private final ServerAPIComponent title;
  private final String address;
  private final boolean showPreview;

  protected ServerSwitchPrompt(
      @NotNull ServerAPIComponent title,
      @NotNull String address,
      boolean showPreview
  ) {
    this.title = title;
    this.address = address;
    this.showPreview = showPreview;
  }

  /**
   * Creates a new server switch prompt instance.
   *
   * @param title       the title of the server switch prompt
   * @param address     the address of the suggested server
   * @param showPreview whether the server switch prompt should contain a live preview of the server
   * @return a new server switch prompt instance
   */
  public static ServerSwitchPrompt create(
      @NotNull ServerAPIComponent title,
      @NotNull String address,
      boolean showPreview
  ) {
    return new ServerSwitchPrompt(title, address, showPreview);
  }

  /**
   * Creates a new server switch prompt instance with the live preview set to true.
   *
   * @param title   the title of the server switch prompt
   * @param address the address of the suggested server
   * @return a new server switch prompt instance
   */
  public static ServerSwitchPrompt create(
      @NotNull ServerAPIComponent title,
      @NotNull String address
  ) {
    return new ServerSwitchPrompt(title, address, true);
  }

  /**
   * @return the title of the server switch prompt
   */
  public @NotNull ServerAPIComponent title() {
    return this.title;
  }

  /**
   * @return the address of the suggested server
   */
  public @NotNull String getAddress() {
    return this.address;
  }

  /**
   * @return whether the server switch prompt should contain a live preview of the server
   */
  public boolean isShowPreview() {
    return this.showPreview;
  }

  @Override
  public String toString() {
    return "ServerSwitchPrompt{" +
        "title=" + this.title +
        ", address='" + this.address + '\'' +
        ", showPreview=" + this.showPreview +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ServerSwitchPrompt that)) {
      return false;
    }

    return this.showPreview == that.showPreview && Objects.equals(this.title, that.title)
        && Objects.equals(this.address, that.address);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.title, this.address, this.showPreview);
  }
}