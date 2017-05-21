package info.loenwind.infinityblock.proxies;

import javax.annotation.Nonnull;

import info.loenwind.infinityblock.InfinityBlockMod;
import info.loenwind.infinityblock.blocks.BlockInfinityBlock;
import info.loenwind.infinityblock.render.InfinityBlockColor;
import info.loenwind.infinityblock.render.InfinityBlockStateMapper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

  @Override
  public void init(final FMLPreInitializationEvent event) {
    super.init(event);
    InfinityBlockStateMapper.create();
    final BlockInfinityBlock blockInfinityBlock = InfinityBlockMod.blockInfinityBlock;
    if (blockInfinityBlock != null) {
      for (int i = 0; i < 16; i++) {
        regRenderer(blockInfinityBlock, i);
      }
    }
  }

  public static void regRenderer(@Nonnull final Block block, final int meta) {
    final Item item = Item.getItemFromBlock(block);
    final ResourceLocation resourceLocation = block.getRegistryName();
    if (resourceLocation == null) {
      throw new NullPointerException("Cannot register renderers for block " + block + ": Block is not registered with the block registry");
    }
    final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(resourceLocation, "inventory");
    ModelLoader.setCustomModelResourceLocation(item, meta, modelResourceLocation);
  }

  @Override
  public void init(final FMLInitializationEvent event) {
    super.init(event);
    InfinityBlockColor.create();
  }

}
