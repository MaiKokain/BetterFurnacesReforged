package wily.betterfurnaces.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;

import java.util.List;

public class UpgradeItem extends Item {
private String tooltipName;
private Component tooltip;
public int upgradeType;
// Use a different int for each type of upgrade
    public UpgradeItem(Properties properties, int Type, String tooltipName ) {
        super(properties);
        upgradeType = Type;
        this.tooltipName = tooltipName;
    }
    public UpgradeItem(Properties properties, int Type) {
        super(properties);
        upgradeType = Type;
    }
    public UpgradeItem(Properties properties, int Type, Component tooltip ) {
        super(properties);
        upgradeType = Type;
        this.tooltip = tooltip;

    }
    public boolean isEnabled(){
        return true;
    }

    public boolean isSameType(UpgradeItem upg){
        return upgradeType == upg.upgradeType;
    }
    public boolean isValid(SmeltingBlockEntity blockEntity){
        return  isEnabled() && blockEntity.getUpgrades().stream().allMatch(this::isUpgradeCompatibleWith);
    }

    public boolean isUpgradeCompatibleWith(UpgradeItem upg){
        return true;
    }
    public Component getDisabledMessage(){
        return new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.disabled").setStyle(Style.EMPTY.applyFormat((ChatFormatting.RED)));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (!isEnabled())
            tooltip.add(getDisabledMessage());
        tooltip.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade_right_click").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GOLD).withItalic(true)));
        if (this.tooltip != null || tooltipName != null)
        tooltip.add( this.tooltip == null ? new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade." + this.tooltipName).setStyle(Style.EMPTY.applyFormat((ChatFormatting.GRAY))) : this.tooltip);

    }

}
