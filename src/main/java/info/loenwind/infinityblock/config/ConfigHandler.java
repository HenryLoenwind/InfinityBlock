package info.loenwind.infinityblock.config;

import java.io.File;

import org.apache.logging.log4j.Logger;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ConfigHandler {

  private static final int ID = 0;

  private final SimpleNetworkWrapper network;
  private final String modid;
  private final Logger log;

  static {
    loadAll();
  }

  // ****************************************************************************************

  public static Configuration configuration;
  public static File configDirectory;
  public static boolean configLockedByServer = false;
  public static boolean iAmTheServer = false;

  public ConfigHandler(final String modid, final Logger log, final SimpleNetworkWrapper network) {
    this.modid = modid;
    this.log = log;
    this.network = network;
  }

  public void init(final FMLPreInitializationEvent event) {
    network.registerMessage(new HandleConfigSync(), PacketConfigSync.class, ID, Side.CLIENT);
    MinecraftForge.EVENT_BUS.register(this);
    configDirectory = new File(event.getModConfigurationDirectory(), modid.toLowerCase());
    if (!configDirectory.exists()) {
      configDirectory.mkdir();
    }

    final File configFile = new File(configDirectory, modid + ".cfg");
    configuration = new Configuration(configFile);
    syncConfig(false);
  }

  private void syncConfig(final boolean load) {
    try {
      if (load) {
        configuration.load();
      }
      processConfig();
    } catch (final Exception e) {
      log.error(modid + " has a problem loading its configuration");
      e.printStackTrace();
    } finally {
      if (configuration.hasChanged()) {
        configuration.save();
      }
    }
  }

  @SuppressWarnings("static-method")
  @SubscribeEvent
  public void onConfigChanged(final OnConfigChangedEvent event) {
    if (event.getModID().equals(modid)) {
      log.info("Updating config...");
      syncConfig(false);
    }
  }

  private static void processConfig() {
    loadAll(configuration);
    if (configuration.hasChanged()) {
      configuration.save();
    }
  }

  public static void toBytes(final ByteBuf buf) {
    for (final Config value : Config.values()) {
      if (value.getSection().sync) {
        value.store(buf);
      }
    }
  }

  public static void fromBytes(final ByteBuf buf) {
    if (iAmTheServer) {
      return;
    }
    for (final Config value : Config.values()) {
      if (value.getSection().sync) {
        value.read(buf);
      }
    }
    configLockedByServer = true;
  }

  @SuppressWarnings("static-method")
  @SubscribeEvent
  public void onPlayerLoggon(final PlayerLoggedInEvent evt) {
    iAmTheServer = true;
    network.sendTo(new PacketConfigSync(), (EntityPlayerMP) evt.player);
  }

  @SuppressWarnings("static-method")
  @SubscribeEvent
  public void onPlayerLogout(final ClientDisconnectionFromServerEvent event) {
    syncConfig(false);
    configLockedByServer = false;
  }

  public static void loadAll(final Configuration config) {
    for (final Config value : Config.values()) {
      value.load(config);
    }
  }

  public static void loadAll() {
    for (final Config value : Config.values()) {
      value.resetToDefault();
    }
  }

}
