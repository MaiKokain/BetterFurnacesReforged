package wily.betterfurnaces;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ConfigGuiFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft minecraftInstance) {}

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        List<IConfigElement> elementList = new ArrayList<>();
        elementList.add(new ConfigElement(Config.cfg.getCategory("updates")));
        elementList.add(new ConfigElement(Config.cfg.getCategory("furnaces")));

        return new GuiConfig(
                parentScreen,
                elementList,
                BetterFurnacesReforged.MODID,
                false,
                false,
                BetterFurnacesReforged.MODNAME + " Config"
        );
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return Collections.emptySet();
    }
}
