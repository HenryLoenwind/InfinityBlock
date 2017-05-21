package info.loenwind.infinityblock.proxies;

import info.loenwind.infinityblock.InfinityBlockMod;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class CommonProxy {

  public void init(final FMLPreInitializationEvent event) {
  }

  public void init(final FMLInitializationEvent event) {
    if (InfinityBlockMod.blockInfinityBlock != null) {
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(InfinityBlockMod.blockInfinityBlock, 1, 0), "AAA", "ABA", "CCC", 'A', "blockGlassColorless", 'B',
          "chestEnder", 'C', "ingotIron"));
    }
  }

}
