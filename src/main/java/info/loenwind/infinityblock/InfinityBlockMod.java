package info.loenwind.infinityblock;

import org.apache.logging.log4j.Logger;

import info.loenwind.infinityblock.blocks.BlockInfinityBlock;
import info.loenwind.infinityblock.blocks.InfinityRegistry;
import info.loenwind.infinityblock.config.ConfigHandler;
import info.loenwind.infinityblock.proxies.CommonProxy;
import info.loenwind.infinityblock.top.TOPUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = InfinityBlockMod.MODID, version = InfinityBlockMod.VERSION, name = InfinityBlockMod.MODID, guiFactory = "info.loenwind.infinityblock.config.gui.ConfigFactory")
public class InfinityBlockMod {

  public static final String MODID = "infinityblock";
  public static final String VERSION = "1.0.0";

  @SidedProxy(clientSide = "info.loenwind.infinityblock.proxies.ClientProxy", serverSide = "info.loenwind.infinityblock.proxies.CommonProxy")
  public static CommonProxy PROXY;

  public static Logger LOG;

  public static SimpleNetworkWrapper NETWORK;

  public static ConfigHandler CONFIGHANDLER;

  public static BlockInfinityBlock blockInfinityBlock;

  @EventHandler
  public void preinit(final FMLPreInitializationEvent event) {
    LOG = event.getModLog();
    NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(InfinityBlockMod.MODID);
    CONFIGHANDLER = new ConfigHandler(MODID, LOG, NETWORK);
    CONFIGHANDLER.init(event);
    blockInfinityBlock = BlockInfinityBlock.create();
    PROXY.init(event);
    TOPUtil.create();
  }

  @EventHandler
  public void init(final FMLInitializationEvent event) {
    PROXY.init(event);
  }

  @EventHandler
  public void onServerStart(final FMLServerAboutToStartEvent event) {
    InfinityRegistry.reset();
  }

  @EventHandler
  public void onServerStart(final FMLServerStoppingEvent event) {
    InfinityRegistry.reset();
  }

}
