package info.loenwind.infinityblock.render;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class EasterEgg {

  private static final String[] NAMES = { "doesntknowvanilla20", "direwolf02", "9by9", "direderp20", "02flowerid", "direworf20", "irewolf20", "direwolf19",
      "direwolf20", "direwolf20", "direwolf20", "direwolf20", "hey soaryn!" };

  private static Random rand = new Random();
  private static int idx = -1;
  private static long nextChange = 0L;

  public static void render(final PlayerEvent.NameFormat event) {
    if (event.getEntityPlayer().getName().equals("direwolf20") && event.getEntityPlayer() != Minecraft.getMinecraft().player) {
      if (idx < 0 || nextChange > Minecraft.getSystemTime()) {
        idx = rand.nextInt(NAMES.length);
        nextChange = Minecraft.getSystemTime() + 5 * 60 * 1000;
      }
      event.setDisplayname(NAMES[idx]);
    }
  }

}
