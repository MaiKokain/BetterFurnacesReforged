package wily.betterfurnaces.inventory;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.items.ItemUpgrade;
import wily.betterfurnaces.tile.TileEntitySmeltingBase;

public class SlotHeater extends SlotItemHandler {
	TileEntitySmeltingBase te;
	public SlotHeater(TileEntitySmeltingBase te, int slotIndex, int xPosition, int yPosition) {
		super(te.getInventory(), slotIndex, xPosition, yPosition);
		this.te = te;
	}
	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem() instanceof ItemUpgrade  && ((ItemUpgrade) stack.getItem()).upgradeType == 1;
	}

}