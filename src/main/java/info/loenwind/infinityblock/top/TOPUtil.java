package info.loenwind.infinityblock.top;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class TOPUtil {

  private TOPUtil() {
  }

  public static void create() {
    if (Loader.isModLoaded("theoneprobe")) {
      FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "info.loenwind.infinityblock.top.TOPCompatibility");
    }
  }

}
