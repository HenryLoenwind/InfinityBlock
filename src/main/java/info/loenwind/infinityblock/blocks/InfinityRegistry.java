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
import net.minecraftforge.common.UsernameCache;
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
        final NonNullList<TileInfinityBlock> channelList = NonNullList.create();
        channelList.add(te);
        registry.put(owner, new ChannelList());
      }
      final List<TeLink<TileInfinityBlock>> linkList = registry.get(owner).get(te.getChannel());
      final Iterator<TeLink<TileInfinityBlock>> iterator = linkList.iterator();
      while (iterator.hasNext()) {
        final TeLink<TileInfinityBlock> nextLink = iterator.next();
        if (nextLink.isDupe(te)) {
          return;
        }
        final TileInfinityBlock nextTe = nextLink.get();
        if (nextTe == null) {
          iterator.remove();
        }
      }
      final TeLink<TileInfinityBlock> teLink = new TeLink<>(te);
      linkList.add(teLink);
    }
  }

  public void unregister(final TileInfinityBlock te) {
    final UUID owner = te.getOwner();
    if (owner != null) {
      if (registry.containsKey(owner)) {
        boolean hasRemaining = false;
        for (final List<TeLink<TileInfinityBlock>> list : registry.get(owner).values()) {
          final Iterator<TeLink<TileInfinityBlock>> iterator = list.iterator();
          while (iterator.hasNext()) {
            final TeLink<TileInfinityBlock> nextLink = iterator.next();
            if (nextLink.isSame(te)) {
              iterator.remove();
            } else if (nextLink.get() == null) {
              iterator.remove();
            }
          }
          hasRemaining = !list.isEmpty() || hasRemaining;
        }
        if (!hasRemaining) {
          registry.remove(owner);
        }
      }
    }
  }

  protected void checkIsThere(final TileInfinityBlock te) {
    final UUID owner = te.getOwner();
    if (owner != null) {
      if (registry.containsKey(owner)) {
        final List<TeLink<TileInfinityBlock>> list = registry.get(owner).get(te.getChannel());
        final Iterator<TeLink<TileInfinityBlock>> iterator = list.iterator();
        while (iterator.hasNext()) {
          if (iterator.next().isSame(te)) {
            return;
          }
        }
      }
      register(te);
    }
  }

  public IItemHandler getInventory(final TileInfinityBlock te, final UUID owner, final EnumDyeColor channel) {
    checkIsThere(te);
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
        final List<TeLink<TileInfinityBlock>> list = registry.get(owner).get(channel);
        if (slot >= 0 && slot < list.size()) {
          final TileInfinityBlock te = list.get(slot).get();
          if (te != null) {
            return te.getInventory();
          } else {
            list.remove(slot);
            return getStackInSlot(slot);
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
        final List<TeLink<TileInfinityBlock>> list = registry.get(owner).get(channel);
        if (slot >= 0 && slot < list.size()) {
          final TileInfinityBlock te = list.get(slot).get();
          if (te != null) {
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
          } else {
            list.remove(slot);
            return insertItem(slot, stack, simulate);
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
          final List<TeLink<TileInfinityBlock>> list = registry.get(owner).get(channel);
          if (slot >= 0 && slot < list.size()) {
            final TileInfinityBlock te = list.get(slot).get();
            if (te != null) {
              final ItemStack stackInSlot = te.getInventory();
              if (!simulate) {
                final ItemStack result = stackInSlot.splitStack(amount);
                te.markDirty();
                return result;
              } else {
                return stackInSlot.copy().splitStack(amount);
              }
            } else {
              list.remove(slot);
              return extractItem(slot, amount, simulate);
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

  @Override
  public @Nonnull String toString() {
    String result = "";
    for (final UUID uuid : registry.keySet()) {
      final String username = UsernameCache.getLastKnownUsername(uuid);
      result += (username == null ? "(unknown)" : username) + " (" + uuid + ")\n" + registry.get(uuid);
    }
    return result;
  }

  public @Nonnull String toString(final UUID uuid) {
    String result = "";
    final String username = UsernameCache.getLastKnownUsername(uuid);
    result += (username == null ? "(unknown)" : username) + " (" + uuid + ")\n" + registry.get(uuid);
    return result;
  }

}
