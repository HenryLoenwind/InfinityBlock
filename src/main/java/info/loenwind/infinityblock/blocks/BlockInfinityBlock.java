package info.loenwind.infinityblock.blocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import info.loenwind.infinityblock.InfinityBlockMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class BlockInfinityBlock extends Block {

  public static BlockInfinityBlock create() {
    final BlockInfinityBlock result = new BlockInfinityBlock("infinityblock");
    GameRegistry.register(result);
    GameRegistry.register(new ItemCloth(result).setRegistryName(InfinityBlockMod.MODID, "infinityblock"));
    GameRegistry.registerTileEntity(TileInfinityBlock.class, "tileinfinityblock");
    return result;
  }

  @Nonnull
  protected final String name;

  @SuppressWarnings("null")
  public BlockInfinityBlock(@Nonnull final String name) {
    super(Material.IRON);
    this.name = name;
    setSoundType(SoundType.METAL);
    setCreativeTab(CreativeTabs.REDSTONE);
    setUnlocalizedName(name);
    setRegistryName(InfinityBlockMod.MODID, name);
    setLightLevel(4F / 16F);
    setHardness(11.25F);
    setResistance(500.0F);
    setHarvestLevel("pickaxe", 3);
    setDefaultState(this.blockState.getBaseState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE));
  }

  @Override
  public @Nonnull IBlockState getStateFromMeta(final int meta) {
    return getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.byMetadata(meta));
  }

  @Override
  public int getMetaFromState(@Nonnull final IBlockState state) {
    return state.getValue(BlockColored.COLOR).getMetadata();
  }

  @Override
  protected @Nonnull BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { BlockColored.COLOR });
  }

  @Override
  public boolean isOpaqueCube(@Nonnull final IBlockState state) {
    return false;
  }

  @Override
  public void getSubBlocks(@Nonnull final Item itemIn, @Nonnull final CreativeTabs tab, @Nonnull final NonNullList<ItemStack> list) {
    for (int i = 0; i <= 15; i++) {
      list.add(new ItemStack(itemIn, 1, i));
    }
  }

  @Override
  public boolean isFullCube(@Nonnull final IBlockState state) {
    return false;
  }

  @Override
  public @Nonnull BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean shouldSideBeRendered(@Nonnull final IBlockState blockStateIn, @Nonnull final IBlockAccess blockAccess, @Nonnull final BlockPos pos,
      @Nonnull final EnumFacing side) {
    return side != EnumFacing.DOWN || super.shouldSideBeRendered(blockStateIn, blockAccess, pos, side);
  }

  @Override
  public boolean hasTileEntity(@Nonnull final IBlockState state) {
    return true;
  }

  @Override
  @Nullable
  public TileEntity createTileEntity(@Nonnull final World world, @Nonnull final IBlockState state) {
    return new TileInfinityBlock();
  }

  @Override
  public boolean canPlaceTorchOnTop(@Nonnull final IBlockState state, @Nonnull final IBlockAccess world, @Nonnull final BlockPos pos) {
    return false;
  }

  @Override
  public boolean canRenderInLayer(@Nonnull final IBlockState state, @Nonnull final BlockRenderLayer layer) {
    return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.TRANSLUCENT;
  }

  @Override
  public void onBlockPlacedBy(@Nonnull final World worldIn, @Nonnull final BlockPos pos, @Nonnull final IBlockState state,
      @Nonnull final EntityLivingBase placer, @Nonnull final ItemStack stack) {
    final TileEntity tileentity = worldIn.getTileEntity(pos);

    if (tileentity instanceof TileInfinityBlock) {
      ((TileInfinityBlock) tileentity).setOwner(placer);
    }
  }

  @Override
  public void breakBlock(@Nonnull final World worldIn, @Nonnull final BlockPos pos, @Nonnull final IBlockState state) {
    final TileEntity tileentity = worldIn.getTileEntity(pos);

    if (tileentity instanceof TileInfinityBlock) {
      ((TileInfinityBlock) tileentity).breakBlock();
    }

    super.breakBlock(worldIn, pos, state);
  }

  public static final String[] DYE_ORE_NAMES = { "dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray",
      "dyePink", "dyeLime", "dyeYellow", "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite" };

  public static EnumDyeColor getColorFromDye(final @Nonnull ItemStack dye) {
    if (!dye.isEmpty()) {
      final int[] oreIDs = OreDictionary.getOreIDs(dye);
      for (int i = 0; i < DYE_ORE_NAMES.length; i++) {
        final int dyeID = OreDictionary.getOreID(DYE_ORE_NAMES[i]);
        for (final int oreId : oreIDs) {
          if (dyeID == oreId) {
            return EnumDyeColor.byDyeDamage(i);
          }
        }
      }
    }
    return null;
  }

  @Override
  public boolean onBlockActivated(@Nonnull final World worldIn, @Nonnull final BlockPos pos, @Nonnull final IBlockState state,
      @Nonnull final EntityPlayer player, @Nonnull final EnumHand hand, @Nonnull final EnumFacing facing, final float hitX, final float hitY,
      final float hitZ) {
    final ItemStack heldItem = player.getHeldItem(hand);
    final EnumDyeColor col = getColorFromDye(heldItem);
    if (col != null && col != state.getValue(BlockColored.COLOR)) {
      if (!worldIn.isRemote) {
        if (recolorBlock(worldIn, pos, facing, col) && !player.capabilities.isCreativeMode) {
          heldItem.shrink(1);
        }
      }
      return true;
    }
    if (!worldIn.isRemote && heldItem.getItem() == Items.FLINT) {
      player.sendMessage(new TextComponentString(InfinityRegistry.instance.toString()));
    }
    if (!worldIn.isRemote && heldItem.getItem() == Items.STICK) {
      final TileEntity tileentity = worldIn.getTileEntity(pos);
      if (tileentity instanceof TileInfinityBlock) {
        player.sendMessage(new TextComponentString(InfinityRegistry.instance.toString(((TileInfinityBlock) tileentity).getOwner())));
      }
    }
    return false;
  }

  @Override
  public boolean recolorBlock(@Nonnull final World world, @Nonnull final BlockPos pos, @Nonnull final EnumFacing side, @Nonnull final EnumDyeColor color) {
    if (super.recolorBlock(world, pos, side, color)) {
      final TileEntity tileentity = world.getTileEntity(pos);
      if (tileentity instanceof TileInfinityBlock) {
        ((TileInfinityBlock) tileentity).channelchanged();
      }
      return true;
    }
    return false;
  }

  @Override
  public int damageDropped(@Nonnull final IBlockState state) {
    return state.getValue(BlockColored.COLOR).getMetadata();
  }

}