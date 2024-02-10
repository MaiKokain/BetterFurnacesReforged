package wily.betterfurnaces;

import com.google.common.base.Suppliers;
import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.RegistrarManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wily.betterfurnaces.gitup.UpCheck;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.network.Messages;

import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file

public class BetterFurnacesReforged
{

    public static final String MOD_ID = "betterfurnacesreforged";
    public static final Supplier<String> VERSION =  Platform.getMod(MOD_ID)::getVersion;
    public static final String MC_VERSION = "1.20.1";

    public static final Supplier<RegistrarManager> REGISTRIES = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));
    public static final Logger LOGGER = LogManager.getLogger();
    public static void init(){

        Messages.registerMessages("betterfurnaces_network");

        Config.setupPlatformConfig();

        Registration.init();

        if (Config.checkUpdates.get()) {
            new UpCheck();
        } else {
            LOGGER.warn("You have disabled BetterFurnace's Update Checker, to re-enable: change the value of Update Checker in .minecraft->config->betterfurnacesreforged-client.toml to 'true'.");
        }

    }

}
