package wily.ultimatefurnaces;

import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.ultimatefurnaces.init.ModObjectsUF;

// The value here should match an entry in the META-INF/mods.toml file

public class UltimateFurnaces
{


    public static final Logger LOGGER = LogManager.getLogger();
    public static final CreativeModeTab ITEM_GROUP = CreativeTabRegistry.create(new ResourceLocation(BetterFurnacesReforged.MOD_ID,"tab_ultimate"), ()-> ModObjectsUF.ULTIMATE_FURNACE.get().asItem().getDefaultInstance());

    public static void init() {

    }
}
