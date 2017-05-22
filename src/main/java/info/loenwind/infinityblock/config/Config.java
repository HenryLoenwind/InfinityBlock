package info.loenwind.infinityblock.config;

import java.nio.charset.Charset;

import javax.annotation.Nonnull;

import info.loenwind.infinityblock.InfinityBlockMod;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public enum Config {

  // section, defaultValue, description, requiresWorldRestart, requiresGameRestart

  usePlayerColors(Section.CLIENT, true, "Should rotor be colored according by the player's UUID or the channel.", false, false),
  registerRecipe(Section.RECIPE, false, "Should the recipe be registered. If not, you need to add it to the game in an alternative way. (For modpack makers)",
      true, true),
  useIronBlocks(Section.RECIPE, false, "Should the recipe use iron blocks instead of iron ingot.", true, true),
  useBlueGlass(Section.RECIPE, false, "Should the recipe use blue glass instead of uncolored glass.", true, true),

  ;

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Nothing to see beyond this point. End of configuration values.
  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Nonnull
  private final Section section;
  @Nonnull
  private final Object defaultValue;
  @Nonnull
  private final String description;
  @Nonnull
  private Object currentValue;
  private final boolean requiresWorldRestart;
  private final boolean requiresGameRestart;

  private Config(@Nonnull final Section section, @Nonnull final Object defaultValue, @Nonnull final String description, final boolean requiresWorldRestart,
      final boolean requiresGameRestart) {
    this.section = section;
    this.description = description;
    this.currentValue = this.defaultValue = defaultValue;
    this.requiresWorldRestart = requiresWorldRestart;
    this.requiresGameRestart = requiresGameRestart;
  }

  private Config(@Nonnull final Section section, @Nonnull final Integer defaultValue, @Nonnull final String description, final boolean requiresWorldRestart,
      final boolean requiresGameRestart) {
    this(section, (Object) defaultValue, description, requiresWorldRestart, requiresGameRestart);
  }

  private Config(@Nonnull final Section section, @Nonnull final Double defaultValue, @Nonnull final String description, final boolean requiresWorldRestart,
      final boolean requiresGameRestart) {
    this(section, (Object) defaultValue, description, requiresWorldRestart, requiresGameRestart);
  }

  private Config(@Nonnull final Section section, @Nonnull final Boolean defaultValue, @Nonnull final String description, final boolean requiresWorldRestart,
      final boolean requiresGameRestart) {
    this(section, (Object) defaultValue, description, requiresWorldRestart, requiresGameRestart);
  }

  private Config(@Nonnull final Section section, @Nonnull final String defaultValue, @Nonnull final String description, final boolean requiresWorldRestart,
      final boolean requiresGameRestart) {
    this(section, (Object) defaultValue, description, requiresWorldRestart, requiresGameRestart);
  }

  private Config(@Nonnull final Section section, @Nonnull final Integer defaultValue, @Nonnull final String description) {
    this(section, (Object) defaultValue, description, false, false);
  }

  private Config(@Nonnull final Section section, @Nonnull final Double defaultValue, @Nonnull final String description) {
    this(section, (Object) defaultValue, description, false, false);
  }

  private Config(@Nonnull final Section section, @Nonnull final Boolean defaultValue, @Nonnull final String description) {
    this(section, (Object) defaultValue, description, false, false);
  }

  private Config(@Nonnull final Section section, @Nonnull final String defaultValue, @Nonnull final String description) {
    this(section, (Object) defaultValue, description, false, false);
  }

  void load(final Configuration config) {
    Object value = null;
    if (defaultValue instanceof Integer) {
      value = setPropertyData(config.get(section.name, name(), (Integer) defaultValue, description)).getInt((Integer) defaultValue);
    } else if (defaultValue instanceof Double) {
      value = setPropertyData(config.get(section.name, name(), (Double) defaultValue, description)).getDouble((Double) defaultValue);
    } else if (defaultValue instanceof Boolean) {
      value = setPropertyData(config.get(section.name, name(), (Boolean) defaultValue, description)).getBoolean((Boolean) defaultValue);
    } else if (defaultValue instanceof String) {
      value = setPropertyData(config.get(section.name, name(), (String) defaultValue, description)).getString();
    }

    setField(value);
  }

  private Property setPropertyData(final Property property) {
    property.setRequiresWorldRestart(requiresWorldRestart);
    property.setRequiresMcRestart(requiresGameRestart);
    property.setLanguageKey(InfinityBlockMod.MODID + ".config." + name());
    return property;
  }

  private void setField(final Object value) {
    if (value != null) {
      currentValue = value;
    }
  }

  void store(final ByteBuf buf) {
    if (defaultValue instanceof Integer) {
      buf.writeInt(getInt());
    } else if (defaultValue instanceof Double) {
      buf.writeDouble(getDouble());
    } else if (defaultValue instanceof Boolean) {
      buf.writeBoolean(getBoolean());
    } else if (defaultValue instanceof String) {
      final String value = getString();
      final byte[] bytes = value.getBytes(Charset.forName("UTF-8"));
      buf.writeInt(bytes.length);
      buf.writeBytes(bytes);
    }
  }

  void read(final ByteBuf buf) {
    Object value = null;
    if (defaultValue instanceof Integer) {
      value = buf.readInt();
    } else if (defaultValue instanceof Double) {
      value = buf.readDouble();
    } else if (defaultValue instanceof Boolean) {
      value = buf.readBoolean();
    } else if (defaultValue instanceof String) {
      final int len = buf.readInt();
      final byte[] bytes = new byte[len];
      buf.readBytes(bytes, 0, len);
      value = new String(bytes, Charset.forName("UTF-8"));
    }
    setField(value);
  }

  protected void resetToDefault() {
    setField(defaultValue);
  }

  public Section getSection() {
    return section;
  }

  //

  private class DataTypeErrorInConfigException extends RuntimeException {
    private static final long serialVersionUID = -7077690323202964355L;
  }

  public int getDefaultInt() {
    if (defaultValue instanceof Integer) {
      return (Integer) defaultValue;
    } else if (defaultValue instanceof Double) {
      return ((Double) defaultValue).intValue();
    } else if (defaultValue instanceof Boolean) {
      return (Boolean) defaultValue ? 1 : 0;
    } else if (defaultValue instanceof String) {
      throw new DataTypeErrorInConfigException();
    } else {
      throw new DataTypeErrorInConfigException();
    }
  }

  public double getDefaultDouble() {
    if (defaultValue instanceof Integer) {
      return (Integer) defaultValue;
    } else if (defaultValue instanceof Double) {
      return (Double) defaultValue;
    } else if (defaultValue instanceof Boolean) {
      return (Boolean) defaultValue ? 1 : 0;
    } else if (defaultValue instanceof String) {
      throw new DataTypeErrorInConfigException();
    } else {
      throw new DataTypeErrorInConfigException();
    }
  }

  public float getDefaultFloat() {
    if (defaultValue instanceof Integer) {
      return (Integer) defaultValue;
    } else if (defaultValue instanceof Double) {
      return ((Double) defaultValue).floatValue();
    } else if (defaultValue instanceof Boolean) {
      return (Boolean) defaultValue ? 1 : 0;
    } else if (defaultValue instanceof String) {
      throw new DataTypeErrorInConfigException();
    } else {
      throw new DataTypeErrorInConfigException();
    }
  }

  public boolean getDefaultBoolean() {
    if (defaultValue instanceof Integer) {
      throw new DataTypeErrorInConfigException();
    } else if (defaultValue instanceof Double) {
      throw new DataTypeErrorInConfigException();
    } else if (defaultValue instanceof Boolean) {
      return (Boolean) defaultValue;
    } else if (defaultValue instanceof String) {
      throw new DataTypeErrorInConfigException();
    } else {
      throw new DataTypeErrorInConfigException();
    }
  }

  @SuppressWarnings("null")
  @Nonnull
  public String getDefaultString() {
    if (defaultValue instanceof Integer) {
      return ((Integer) defaultValue).toString();
    } else if (defaultValue instanceof Double) {
      return ((Double) defaultValue).toString();
    } else if (defaultValue instanceof Boolean) {
      return ((Boolean) defaultValue).toString();
    } else if (defaultValue instanceof String) {
      return (String) defaultValue;
    } else {
      throw new DataTypeErrorInConfigException();
    }
  }

  //

  public int getInt() {
    if (defaultValue instanceof Integer) {
      return (Integer) currentValue;
    } else if (defaultValue instanceof Double) {
      return ((Double) currentValue).intValue();
    } else if (defaultValue instanceof Boolean) {
      return (Boolean) currentValue ? 1 : 0;
    } else if (defaultValue instanceof String) {
      throw new DataTypeErrorInConfigException();
    } else {
      throw new DataTypeErrorInConfigException();
    }
  }

  public double getDouble() {
    if (defaultValue instanceof Integer) {
      return (Integer) currentValue;
    } else if (defaultValue instanceof Double) {
      return (Double) currentValue;
    } else if (defaultValue instanceof Boolean) {
      return (Boolean) currentValue ? 1 : 0;
    } else if (defaultValue instanceof String) {
      throw new DataTypeErrorInConfigException();
    } else {
      throw new DataTypeErrorInConfigException();
    }
  }

  public float getFloat() {
    if (defaultValue instanceof Integer) {
      return (Integer) currentValue;
    } else if (defaultValue instanceof Double) {
      return ((Double) currentValue).floatValue();
    } else if (defaultValue instanceof Boolean) {
      return (Boolean) currentValue ? 1 : 0;
    } else if (defaultValue instanceof String) {
      throw new DataTypeErrorInConfigException();
    } else {
      throw new DataTypeErrorInConfigException();
    }
  }

  public boolean getBoolean() {
    if (defaultValue instanceof Integer) {
      throw new DataTypeErrorInConfigException();
    } else if (defaultValue instanceof Double) {
      throw new DataTypeErrorInConfigException();
    } else if (defaultValue instanceof Boolean) {
      return (Boolean) currentValue;
    } else if (defaultValue instanceof String) {
      throw new DataTypeErrorInConfigException();
    } else {
      throw new DataTypeErrorInConfigException();
    }
  }

  @SuppressWarnings("null")
  @Nonnull
  public String getString() {
    if (defaultValue instanceof Integer) {
      return ((Integer) currentValue).toString();
    } else if (defaultValue instanceof Double) {
      return ((Double) currentValue).toString();
    } else if (defaultValue instanceof Boolean) {
      return ((Boolean) currentValue).toString();
    } else if (defaultValue instanceof String) {
      return (String) currentValue;
    } else {
      throw new DataTypeErrorInConfigException();
    }
  }
}
