package info.loenwind.infinityblock.config.gui;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class ConfigFactory implements IModGuiFactory {

  @Override
  public void initialize(final Minecraft minecraftInstance) {
  }

  @Override
  public Class<? extends GuiScreen> mainConfigGuiClass() {
    return GuiConfigFactory.class;
  }

  @Override
  public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
    return null;
  }

  @Override
  public RuntimeOptionGuiHandler getHandlerFor(final RuntimeOptionCategoryElement element) {
    return null;
  }

  @Override
  public boolean hasConfigGui() {
    return true;
  }

  @Override
  public GuiScreen createConfigGui(final GuiScreen parentScreen) {
    return new GuiConfigFactory(parentScreen);
  }
}
