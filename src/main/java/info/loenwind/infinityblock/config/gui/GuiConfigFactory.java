package info.loenwind.infinityblock.config.gui;

import java.util.ArrayList;
import java.util.List;

import info.loenwind.infinityblock.InfinityBlockMod;
import info.loenwind.infinityblock.config.ConfigHandler;
import info.loenwind.infinityblock.config.Section;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class GuiConfigFactory extends GuiConfig {

  public GuiConfigFactory(final GuiScreen parentScreen) {
    super(parentScreen, getConfigElements(parentScreen), InfinityBlockMod.MODID, false, false, I18n.translateToLocal(InfinityBlockMod.MODID + ".config.title"));
  }

  private static List<IConfigElement> getConfigElements(final GuiScreen parent) {
    final List<IConfigElement> list = new ArrayList<IConfigElement>();
    final String prefix = InfinityBlockMod.MODID + ".config.";

    for (final Section section : Section.values()) {
      if (!section.sync || !ConfigHandler.configLockedByServer) {
        list.add(new ConfigElement(ConfigHandler.configuration.getCategory(section.name).setLanguageKey(prefix + section.name)));
      }
    }

    return list;
  }
}
