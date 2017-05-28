package info.loenwind.infinityblock.blocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class TeLink<T extends TileEntity> {

  private final int dimension;
  private final @Nonnull BlockPos pos;
  private boolean isValid = true;
  private final int hashCode;

  public TeLink(final @Nonnull T te) {
    dimension = te.getWorld().provider.getDimension();
    pos = te.getPos();
    hashCode = te.hashCode();
  }

  @SuppressWarnings("unchecked")
  public @Nullable T get() {
    if (isValid) {
      final WorldServer world = DimensionManager.getWorld(dimension);
      if (world != null) {
        if (world.isBlockLoaded(pos)) {
          final TileEntity tileEntity = world.getTileEntity(pos);
          if (tileEntity != null) {
            if (tileEntity.hashCode() == hashCode) {
              return (T) tileEntity;
            }
          }
        }
      }
      isValid = false;
    }
    return null;
  }

  public boolean isDupe(final @Nonnull T te) {
    if (te.hashCode() == hashCode) {
      return true;
    }
    if (te.getWorld().provider.getDimension() == dimension && te.getPos().equals(pos)) {
      isValid = false;
    }
    return false;
  }

  public boolean isSame(final @Nonnull T te) {
    return te.hashCode() == hashCode;
  }

  @Override
  public String toString() {
    return (isValid ? "" : "Invalid ") + "Block in dimension=" + dimension + " at " + pos;
  }

}
