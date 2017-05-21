package info.loenwind.infinityblock.top;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.mojang.authlib.GameProfile;

import info.loenwind.infinityblock.blocks.TileInfinityBlock;
import mcjty.theoneprobe.api.IProbeHitData;

class TOPData {
  GameProfile owner = null;
  String ownerName = null;

  public TOPData(final TileInfinityBlock tileEntity, final IProbeHitData hitData) {
    ownerName = tileEntity.getOwnerName();
    final UUID ownerUUID = tileEntity.getOwner();
    if (ownerUUID == null && StringUtils.isBlank(ownerName)) {
      ownerName = "(unknown)";
    }
    owner = new GameProfile(ownerUUID, ownerName);
  }

}