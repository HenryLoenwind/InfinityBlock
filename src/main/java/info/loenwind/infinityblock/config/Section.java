package info.loenwind.infinityblock.config;

import javax.annotation.Nonnull;

public enum Section {
  CLIENT("client", false),
  RECIPE("recipe", true),
  SERVER("server", true);

  @Nonnull
  public final String name;
  public final boolean sync;

  private Section(@Nonnull final String name) {
    this.name = name;
    this.sync = true;
  }

  private Section(@Nonnull final String name, final boolean sync) {
    this.name = name;
    this.sync = sync;
  }

}
