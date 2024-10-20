package wily.betterfurnaces.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.net.MessageSync;
import wily.betterfurnaces.tile.TileEntityForge;

import javax.annotation.Nullable;

public class FContainerBF extends Container {
	public static int GUIID = 2;
	public static final int SLOTS_TE = 0;
	public static final int SLOTS_TE_SIZE = 14;
	public static final int SLOTS_INVENTORY = SLOTS_TE_SIZE;
	public static final int SLOTS_HOTBAR = SLOTS_INVENTORY + 3 * 9;

	private final TileEntityForge tf;
	private final EntityPlayerMP player;

	public FContainerBF(InventoryPlayer playerInv, TileEntityForge tf) {
		this.tf = tf;
		this.addSlotToContainer(new SlotInput(tf.getInventory(), 0, 27, 62));
		this.addSlotToContainer(new SlotInput(tf.getInventory(), 1, 45, 62));
		this.addSlotToContainer(new SlotInput(tf.getInventory(), 2, 63, 62));
		this.addSlotToContainer(new SlotFuel(tf, 3, 8, 100));
		this.addSlotToContainer(new SlotFurnaceOutput(playerInv.player, tf.getInventory(), 4, 108, 80));
		this.addSlotToContainer(new SlotFurnaceOutput(playerInv.player, tf.getInventory(), 5, 126, 80));
		this.addSlotToContainer(new SlotFurnaceOutput(playerInv.player, tf.getInventory(), 6, 144, 80));
		this.addSlotToContainer(new SlotUpgrade(tf, 11, 115, 5));
		this.addSlotToContainer(new SlotUpgrade(tf, 12, 133, 5));
		this.addSlotToContainer(new SlotUpgrade(tf, 13, 151, 5));
		this.addSlotToContainer(new SlotUpgrade(tf, 7, 7, 5));
		this.addSlotToContainer(new SlotUpgrade(tf, 8, 25, 5));
		this.addSlotToContainer(new SlotUpgrade(tf, 9, 43, 5));
		this.addSlotToContainer(new SlotHeater(tf, 10, 79, 5));
		int si;
		int sj;
		for (si = 0; si < 3; ++si)
			for (sj = 0; sj < 9; ++sj)
				this.addSlotToContainer(new Slot(playerInv, sj + (si + 1) * 9, 0 + 8 + sj * 18, 42 + 84 + si * 18));
		for (si = 0; si < 9; ++si)
			this.addSlotToContainer(new Slot(playerInv, si, 0 + 8 + si * 18, 42 + 142));


		if (playerInv.player instanceof EntityPlayerMP) player = (EntityPlayerMP) playerInv.player;
		else player = null;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (player != null) BetterFurnacesReforged.NETWORK.sendTo(new MessageSync(tf), player);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.tf.isUsableByPlayer(player);
	}

	@Override
	@Nullable
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack slotStack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			slotStack = stack.copy();

			if (index >= SLOTS_INVENTORY && index <= SLOTS_HOTBAR + 9) {
				if (TileEntityFurnace.isItemFuel(stack)) {
					int s = tf.FUEL();
					if (!mergeItemStack(stack, s, s + 1, false)) { return ItemStack.EMPTY; }
				}
				if (!mergeItemStack(stack, SLOTS_TE, SLOTS_TE + SLOTS_TE_SIZE, false)) { return ItemStack.EMPTY; }
			} else if (index >= SLOTS_HOTBAR && index < SLOTS_HOTBAR + 9) {
				if (!mergeItemStack(stack, SLOTS_INVENTORY, SLOTS_INVENTORY + 3 * 9, false)) { return ItemStack.EMPTY; }
			} else if (!mergeItemStack(stack, SLOTS_INVENTORY, SLOTS_HOTBAR + 9, true)) { return ItemStack.EMPTY; }

			slot.onSlotChanged();
			if (stack.getCount() == slotStack.getCount()) { return ItemStack.EMPTY; }
			slot.onTake(player, stack);
		}
		return slotStack;
	}
}
