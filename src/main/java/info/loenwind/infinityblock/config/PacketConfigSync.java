package info.loenwind.infinityblock.config;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketConfigSync implements IMessage {

  protected ByteBuf bufferCopy;

  @Override
  public void fromBytes(final ByteBuf buf) {
    bufferCopy = buf.copy();
  }

  @Override
  public void toBytes(final ByteBuf buf) {
    ConfigHandler.toBytes(buf);
  }

}
