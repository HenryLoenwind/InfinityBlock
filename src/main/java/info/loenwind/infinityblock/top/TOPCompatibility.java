package info.loenwind.infinityblock.top;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.mojang.authlib.GameProfile;

import info.loenwind.infinityblock.InfinityBlockMod;
import info.loenwind.infinityblock.blocks.TileInfinityBlock;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.ILayoutStyle;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class TOPCompatibility implements Function<ITheOneProbe, Void>, IProbeInfoProvider {

  @Nullable
  @Override
  public Void apply(@Nullable final ITheOneProbe theOneProbe) {
    if (theOneProbe != null) {
      theOneProbe.registerProvider(this);
    }
    return null;
  }

  @Override
  public String getID() {
    return InfinityBlockMod.MODID + ":default";
  }

  @Override
  public void addProbeInfo(final ProbeMode mode, final IProbeInfo probeInfo, final EntityPlayer player, final World world, final IBlockState blockState,
      final IProbeHitData hitData) {
    if (probeInfo != null && world != null && blockState != null && hitData != null && blockState.getBlock() == InfinityBlockMod.blockInfinityBlock) {
      final BlockPos pos = hitData.getPos();
      if (pos != null) {
        final TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileInfinityBlock) {
          final EioBox eiobox = new EioBox(probeInfo);
          final TOPData data = new TOPData((TileInfinityBlock) tileEntity, hitData);
          mkOwner(mode, eiobox, data);
        }
      }
    }
  }

  private static class EioBox {
    private final IProbeInfo probeinfo;
    private IProbeInfo eiobox;

    public EioBox(final IProbeInfo probeinfo) {
      this.probeinfo = probeinfo;
    }

    public IProbeInfo getProbeinfo() {
      return probeinfo;
    }

    public IProbeInfo get() {
      if (eiobox == null) {
        eiobox = probeinfo.vertical(probeinfo.defaultLayoutStyle().borderColor(0xffff0000));
      }
      return eiobox;
    }

    public ILayoutStyle center() {
      return probeinfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER);
    }

    @SuppressWarnings("unused")
    public ILayoutStyle right() {
      return probeinfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_BOTTOMRIGHT);
    }

  }

  private void mkOwner(final ProbeMode mode, final EioBox eiobox, final TOPData data) {
    if (data.owner != null || data.ownerName != null) {
      final ItemStack skull = new ItemStack(Items.SKULL, 1, 2);
      final GameProfile owner_nullchecked = data.owner;
      if (owner_nullchecked != null) {
        final NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), owner_nullchecked));
        skull.setTagCompound(nbt);
      }
      eiobox.get().horizontal(eiobox.center()).item(skull).vertical(eiobox.getProbeinfo().defaultLayoutStyle().spacing(-1))
          .text(TextFormatting.YELLOW + I18n.translateToLocal("top.owner.header")).text(data.ownerName);
    }
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(@Nullable final Object obj) {
    return super.equals(obj);
  }

}
