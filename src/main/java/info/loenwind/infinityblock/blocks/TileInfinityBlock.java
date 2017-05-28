package info.loenwind.infinityblock.blocks;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileInfinityBlock extends TileEntity {

  private UUID owner;
  private @Nonnull ItemStack inventory = ItemStack.EMPTY;

  public TileInfinityBlock() {
  }

  @Override
  public void readFromNBT(@Nonnull final NBTTagCompound compound) {
    final String suuid = compound.getString("owner");
    try {
      owner = UUID.fromString(suuid);
    } catch (final IllegalArgumentException e) {
      owner = null;
    }
    inventory = new ItemStack(compound.getCompoundTag("inventory"));
    super.readFromNBT(compound);
  }

  @Override
  public @Nonnull NBTTagCompound writeToNBT(@Nonnull final NBTTagCompound compound) {
    if (owner != null) {
      final String string = owner.toString();
      if (string != null) {
        compound.setString("owner", string);
      }
    }
    compound.setTag("inventory", inventory.writeToNBT(new NBTTagCompound()));
    return super.writeToNBT(compound);
  }

  @Override
  public @Nonnull NBTTagCompound getUpdateTag() {
    final NBTTagCompound updateTag = super.getUpdateTag();
    if (owner != null) {
      final String string = owner.toString();
      if (string != null) {
        updateTag.setString("owner", string);
      }
    }
    return updateTag;
  }

  @Override
  public void invalidate() {
    super.invalidate();
    disconnect();
  }

  @Override
  public void onChunkUnload() {
    super.onChunkUnload();
    disconnect();
  }

  @Override
  public void onLoad() {
    super.onLoad();
    connect();
  }

  @Override
  public boolean hasCapability(@Nonnull final Capability<?> capability, @Nullable final EnumFacing facing) {
    return (owner != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) || super.hasCapability(capability, facing);
  }

  @Override
  @Nullable
  public <T> T getCapability(@Nonnull final Capability<T> capability, @Nullable final EnumFacing facing) {
    if (owner != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(InfinityRegistry.instance.getInventory(this, owner, getChannel()));
    }
    return super.getCapability(capability, facing);
  }

  public void setOwner(final EntityLivingBase placer) {
    if (placer instanceof EntityPlayer) {
      owner = ((EntityPlayer) placer).getGameProfile().getId();
      connect();
    } else {
      owner = null;
      disconnect();
    }
  }

  public void breakBlock() {
    InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), inventory);
    inventory = ItemStack.EMPTY;
  }

  public UUID getOwner() {
    return owner;
  }

  public String getOwnerName() {
    if (owner == null) {
      return "(lost block)";
    }
    final String lastKnownName = UsernameCache.getLastKnownUsername(owner);
    if (lastKnownName == null) {
      return "(" + owner.toString() + ")";
    }
    return lastKnownName;
  }

  private void disconnect() {
    if (!getWorld().isRemote) {
      InfinityRegistry.instance.unregister(this);
    }
  }

  private void connect() {
    if (!getWorld().isRemote) {
      InfinityRegistry.instance.register(this);
    }
  }

  public @Nonnull ItemStack getInventory() {
    return inventory;
  }

  public void setInventory(@Nonnull final ItemStack inventory) {
    this.inventory = inventory;
    markDirty();
  }

  public @Nonnull EnumDyeColor getChannel() {
    return getWorld().getBlockState(getPos()).getValue(BlockColored.COLOR);
  }

  @Override
  public boolean shouldRefresh(@Nonnull final World world1, @Nonnull final BlockPos pos1, @Nonnull final IBlockState oldState,
      @Nonnull final IBlockState newSate) {
    return oldState.getBlock() != newSate.getBlock();
  }

  public void channelchanged() {
    disconnect();
    connect();
  }

  @Override
  public String toString() {
    try {
      return "TileInfinityBlock [getOwner()=" + getOwner() + ", getChannel()=" + getChannel() + ", getPos()=" + getPos() + "]";
    } catch (final Exception e) {
      return "TileInfinityBlock [getOwner()=" + getOwner() + ", getChannel()=???, getPos()=" + getPos() + "]";
    }
  }

}
