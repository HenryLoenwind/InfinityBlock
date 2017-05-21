package info.loenwind.infinityblock.config;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HandleConfigSync implements IMessageHandler<PacketConfigSync, IMessage> {

  public HandleConfigSync() {
  }

  @Override
  public IMessage onMessage(final PacketConfigSync message, final MessageContext ctx) {
    ConfigHandler.fromBytes(message.bufferCopy);
    return null;
  }

}
