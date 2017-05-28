package info.loenwind.infinityblock.blocks;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.EnumDyeColor;

class ChannelList extends EnumMap<EnumDyeColor, List<TeLink<TileInfinityBlock>>> {

  private static final long serialVersionUID = -1362647401924927797L;

  public ChannelList() {
    super(EnumDyeColor.class);
  }

  @Override
  public @Nonnull List<TeLink<TileInfinityBlock>> get(final Object key) {
    List<TeLink<TileInfinityBlock>> list = super.get(key);
    if (list == null) {
      list = new ArrayList<TeLink<TileInfinityBlock>>();
      if (key instanceof EnumDyeColor) {
        put((EnumDyeColor) key, list);
      } else {
        throw new RuntimeException("bad map key");
      }
    }
    return list;
  }

  @Override
  public String toString() {
    String result = "";
    for (final EnumDyeColor channel : keySet()) {
      result += "  Channel " + channel + ":\n";
      final List<TeLink<TileInfinityBlock>> linkList = super.get(channel);
      if (linkList.isEmpty()) {
        result += "    (empty)\n";
      } else {
        for (final TeLink<TileInfinityBlock> link : linkList) {
          result += "    " + link + "\n";
        }
      }
    }
    return result;
  }

}