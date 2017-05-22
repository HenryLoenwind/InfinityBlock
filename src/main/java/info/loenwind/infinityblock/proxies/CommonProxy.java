package info.loenwind.infinityblock.proxies;

import static info.loenwind.infinityblock.config.Config.useIronBlocks;

import info.loenwind.infinityblock.InfinityBlockMod;
import info.loenwind.infinityblock.blocks.BlockInfinityBlock;
import info.loenwind.infinityblock.config.Config;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class CommonProxy {

  public void init(final FMLPreInitializationEvent event) {
  }

  public void init(final FMLInitializationEvent event) {
    final BlockInfinityBlock blockInfinityBlock = InfinityBlockMod.blockInfinityBlock;
    if (blockInfinityBlock != null && Config.registerRecipe.getBoolean()) {
      final String iron = useIronBlocks.getBoolean() ? "blockIron" : "ingotIron";
      final String glass = Config.useBlueGlass.getBoolean() ? "blockGlassBlue" : "blockGlassColorless";

      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockInfinityBlock, 1, 0), "AAA", "ABA", "CCC", 'A', glass, 'B', "chestEnder", 'C', iron));
    }
  }

}
