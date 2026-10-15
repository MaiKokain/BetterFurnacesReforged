package wily.betterfurnaces;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;

import java.io.File;

public class Config {
    public static Configuration cfg;

    public static boolean checkUpdates;
    public static int ironTierSpeed;
    public static int goldTierSpeed;
    public static int diamondTierSpeed;
    public static int netherhotTierSpeed;
    public static int extremeTierSpeed;
    public static int fuelEfficiencyStat;
    public static int forgeTierSpeed;

    public static int ironParallel;
    public static int goldParallel;
    public static int diamondParallel;
    public static int netherhotParallel;
    public static int extremeParallel;
    public static int forgeParallel;

    public Config(File config) {
        cfg = new Configuration(config);
        load();
    }

    public static void load() {
        cfg.load();

        checkUpdates = cfg.getBoolean("checkUpdates", "updates", true, "true = check for updates, false = don't check for updates.");
        ironTierSpeed = cfg.getInt("ironTierSpeed", "furnaces", 150, 0, 200, "Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.");
        goldTierSpeed = cfg.getInt("goldTierSpeed", "furnaces", 100, 0, 200, "Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.");
        diamondTierSpeed = cfg.getInt("diamondTierSpeed", "furnaces", 50, 0, 200, "Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.");
        netherhotTierSpeed = cfg.getInt("netherhotTierSpeed", "furnaces", 8, 0, 200, "Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.");
        extremeTierSpeed = cfg.getInt("extremeTierSpeed", "furnaces", 4, 0, 200, "Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.");
        forgeTierSpeed = cfg.getInt("forgeTierSpeed", "furnaces", 4, 0, 200, "Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.");
        fuelEfficiencyStat = (int) cfg.getFloat("fuelEfficiencyStat", "furnaces", 2, 1, Integer.MAX_VALUE, "Set the efficiency level for both efficiency upgrades.\n (fuelEfficiencyStat*furnaceTierSpeed)");
        ironParallel = cfg.getInt("ironParallel", "furnaces", 1, 1, Integer.MAX_VALUE, "Furnace smelting parallelness");
        goldParallel = cfg.getInt("goldParallel", "furnaces", 1, 1, Integer.MAX_VALUE, "Furnace smelting parallelness");
        diamondParallel = cfg.getInt("diamondParallel", "furnaces", 1, 1, Integer.MAX_VALUE, "Furnace smelting parallelness");
        netherhotParallel = cfg.getInt("netherhotParallel", "furnaces", 1, 1, Integer.MAX_VALUE, "Furnace smelting parallelness");
        extremeParallel = cfg.getInt("extremeParallel", "furnaces", 1, 1, Integer.MAX_VALUE, "Furnace smelting parallelness");
        forgeParallel = cfg.getInt("forgeParallel", "furnaces", 1, 1, Integer.MAX_VALUE, "Furnace smelting parallelness");
        if (cfg.hasChanged()) cfg.save();
    }

    @Mod.EventBusSubscriber(modid = BetterFurnacesReforged.MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(BetterFurnacesReforged.MODID)) {
                if (cfg.hasChanged()) {
                    cfg.save();
                }
                load();
            }
        }
    }
}