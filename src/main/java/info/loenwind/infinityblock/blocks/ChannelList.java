package info.loenwind.infinityblock.blocks;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.EnumDyeColor;

class ChannelList extends EnumMap<EnumDyeColor, List<TileInfinityBlock>> {

  private static final long serialVersionUID = -1362647401924927797L;

  public ChannelList() {
    super(EnumDyeColor.class);
  }

  @Override
  public @Nonnull List<TileInfinityBlock> get(final Object key) {
    List<TileInfinityBlock> list = super.get(key);
    if (list == null) {
      list = new ArrayList<TileInfinityBlock>();
      if (key instanceof EnumDyeColor) {
        put((EnumDyeColor) key, list);
      }
    }
    return list;
  }

}