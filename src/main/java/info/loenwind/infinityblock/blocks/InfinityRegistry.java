package info.loenwind.infinityblock.blocks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class InfinityRegistry {

  public static InfinityRegistry instance = new InfinityRegistry();

  private InfinityRegistry() {
  }

  public static void reset() {
    instance = new InfinityRegistry();
  }

  protected final Map<UUID, ChannelList> registry = new HashMap<UUID, ChannelList>();

  public void register(final TileInfinityBlock te) {
    final UUID owner = te.getOwner();
    if (owner != null) {
      if (!registry.containsKey(owner)) {
        final NonNullList<TileInfinityBlock> list = NonNullList.create();
        list.add(te);
        registry.put(owner, new ChannelList());
      } else {
        final List<TileInfinityBlock> list = registry.get(owner).get(te.getChannel());
        final Iterator<TileInfinityBlock> iterator = list.iterator();
        while (iterator.hasNext()) {
          final TileInfinityBlock next = iterator.next();
          if (!isValid(next)) {
            iterator.remove();
          } else if (isSame(next, te)) {
            return;
          }
        }
        list.add(te);
      }
    }
  }

  public void unregister(final TileInfinityBlock te) {
    final UUID owner = te.getOwner();
    if (owner != null) {
      if (registry.containsKey(owner)) {
        boolean hasRemaining = false;
        for (final List<TileInfinityBlock> list : registry.get(owner).values()) {
          final Iterator<TileInfinityBlock> iterator = list.iterator();
          while (iterator.hasNext()) {
            final TileInfinityBlock next = iterator.next();
            if (isSame(next, te) || !isValid(next)) {
              iterator.remove();
            }
          }
          hasRemaining |= !list.isEmpty();
        }
        if (!hasRemaining) {
          registry.remove(owner);
        }
      }
    }
  }

  protected boolean isValid(final TileInfinityBlock te) {
    return !te.isInvalid() && te.hasWorld() && te.getWorld().isBlockLoaded(te.getPos()) && te.getWorld().getTileEntity(te.getPos()) == te;
  }

  private boolean isSame(final TileInfinityBlock te1, final TileInfinityBlock te2) {
    return te1 == te2 || (te1.getWorld() == te2.getWorld() && te1.getPos().equals(te2.getPos()));
  }

  public IItemHandler getInventory(final UUID owner, final EnumDyeColor channel) {
    return new ItemHandler(owner, channel);
  }

  private class ItemHandler implements IItemHandler {

    private final UUID owner;
    private final EnumDyeColor channel;

    ItemHandler(final UUID owner, final EnumDyeColor channel) {
      this.owner = owner;
      this.channel = channel;
    }

    @Override
    public int getSlots() {
      if (registry.containsKey(owner)) {
        return registry.get(owner).get(channel).size();
      } else {
        return 0;
      }
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(final int slot) {
      if (registry.containsKey(owner)) {
        final List<TileInfinityBlock> list = registry.get(owner).get(channel);
        if (slot >= 0 && slot < list.size()) {
          final TileInfinityBlock te = list.get(slot);
          if (isValid(te)) {
            return te.getInventory();
          }
        }
      }
      return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public ItemStack insertItem(final int slot, @Nonnull final ItemStack stack, final boolean simulate) {
      if (stack.isEmpty()) {
        return ItemStack.EMPTY;
      }

      if (registry.containsKey(owner)) {
        final List<TileInfinityBlock> list = registry.get(owner).get(channel);
        if (slot >= 0 && slot < list.size()) {
          final TileInfinityBlock te = list.get(slot);
          if (isValid(te)) {
            final ItemStack stackInSlot = te.getInventory();
            if (stackInSlot.isEmpty()) {
              if (!simulate) {
                te.setInventory(stack.copy());
              }
              return ItemStack.EMPTY;
            } else {
              if (!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot)) {
                return stack;
              }
              final int m = Math.min(stack.getMaxStackSize(), getSlotLimit(slot)) - stackInSlot.getCount();
              if (stack.getCount() <= m) {
                if (!simulate) {
                  final ItemStack copy = stack.copy();
                  copy.grow(stackInSlot.getCount());
                  te.setInventory(copy);
                }
                return ItemStack.EMPTY;
              } else {
                final ItemStack result = stack.copy();
                if (!simulate) {
                  final ItemStack copy = result.splitStack(m);
                  copy.grow(stackInSlot.getCount());
                  te.setInventory(copy);
                  return result;
                } else {
                  result.shrink(m);
                  return result;
                }
              }
            }
          }
        }
      }
      return stack;
    }

    @Override
    @Nonnull
    public ItemStack extractItem(final int slot, final int amount, final boolean simulate) {
      if (amount > 0) {
        if (registry.containsKey(owner)) {
          final List<TileInfinityBlock> list = registry.get(owner).get(channel);
          if (slot >= 0 && slot < list.size()) {
            final TileInfinityBlock te = list.get(slot);
            if (isValid(te)) {
              final ItemStack stackInSlot = te.getInventory();
              if (!simulate) {
                final ItemStack result = stackInSlot.splitStack(amount);
                list.get(slot).markDirty();
                return result;
              } else {
                return stackInSlot.copy().splitStack(amount);
              }
            }
          }
        }
      }
      return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(final int slot) {
      return 64;
    }

  }

}
