package wily.betterfurnaces.forge;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import wily.betterfurnaces.ForgeConfigCompat;

import static wily.betterfurnaces.ForgeConfigCompat.*;

@Mod.EventBusSubscriber
public class ForgeConfigCompatImpl {
    public static void registerConfig() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT,CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON,COMMON_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER,SERVER_CONFIG);
    }
    @SubscribeEvent
    public static void onServerConfigLoad(final ModConfigEvent event) {
       if(event.getConfig().getSpec().equals(SERVER_CONFIG))
           ForgeConfigCompat.onServerConfigLoad();
    }
    @SubscribeEvent
    public static void onLevelLoad(final WorldEvent.Load event) {
        ForgeConfigCompat.onServerConfigLoad();
    }
}
