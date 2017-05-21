package info.loenwind.infinityblock.render;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import info.loenwind.infinityblock.InfinityBlockMod;
import info.loenwind.infinityblock.blocks.TileInfinityBlock;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class InfinityBlockColor implements IBlockColor, IItemColor {

  public static void create() {
    final InfinityBlockColor instance = new InfinityBlockColor();
    Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(instance, InfinityBlockMod.blockInfinityBlock);
    Minecraft.getMinecraft().getItemColors().registerItemColorHandler(instance, InfinityBlockMod.blockInfinityBlock);
  }

  @Override
  public int colorMultiplier(@Nonnull final IBlockState state, @Nullable final IBlockAccess world, @Nullable final BlockPos pos, final int tintIndex) {
    if (tintIndex == 0) {
      if (world != null && pos != null) {
        final TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileInfinityBlock) {
          final UUID owner = ((TileInfinityBlock) tileEntity).getOwner();
          if (owner != null) {
            final long bits = owner.getLeastSignificantBits();
            final int v1 = (int) (bits & 0x7F) + 64;
            final int v2 = (int) ((bits >>> 8) & 0x7F) + 64;
            final int v3 = (int) ((bits >>> 16) & 0x7F) + 64;
            final int color = v1 << 16 | v2 << 8 | v3;
            return color;
          }
        }
      }
      return state.getValue(BlockColored.COLOR).getMapColor().colorValue;
    } else if (tintIndex == 1) {
      return state.getValue(BlockColored.COLOR).getMapColor().colorValue;
    } else {
      return -1;
    }
  }

  @Override
  public int getColorFromItemstack(@Nonnull final ItemStack stack, final int tintIndex) {
    return EnumDyeColor.byMetadata(stack.getItemDamage()).getMapColor().colorValue;
  }

}
