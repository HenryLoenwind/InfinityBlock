package info.loenwind.infinityblock.render;

import javax.annotation.Nonnull;

import info.loenwind.infinityblock.InfinityBlockMod;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraftforge.client.model.ModelLoader;

public class InfinityBlockStateMapper extends StateMapperBase {

  public static void create() {
    final InfinityBlockStateMapper mapper = new InfinityBlockStateMapper();
    ModelLoader.setCustomStateMapper(InfinityBlockMod.blockInfinityBlock, mapper);
  }

  @Override
  protected @Nonnull ModelResourceLocation getModelResourceLocation(@Nonnull final IBlockState state) {
    return new ModelResourceLocation(Block.REGISTRY.getNameForObject(state.getBlock()), "normal");
  }

}
