package wily.betterfurnaces.inventory;

import me.shedaniel.architectury.event.events.PlayerEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.blockentity.InventoryBlockEntity;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;

import java.util.function.Predicate;

public class SlotOutput extends HideableSlot {

    private final Player player;
    private int removeCount;

    public SlotOutput(Player player,InventoryBlockEntity be, int slotIndex, int xPosition, int yPosition) {
        super(be, slotIndex, xPosition, yPosition);
        this.player = player;
        this.be = be;
    }
    public SlotOutput(Player player,InventoryBlockEntity be, int slotIndex, int xPosition, int yPosition, Predicate<Slot> isActive) {
        this(player,be, slotIndex, xPosition, yPosition);
        this.isActive = isActive;
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    public ItemStack onTake(Player thePlayer, ItemStack stack) {
        this.checkTakeAchievements(stack);
        return super.onTake(thePlayer, stack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
     * internal count then calls onCrafting(item).
     */
    protected void onCrafting(ItemStack stack, int amount) {
        this.removeCount += amount;
        this.onCrafting(stack);
    }

    @Override
    protected void onQuickCraft(ItemStack stack, int p_75210_2_) {
        if (player != null) {
            stack.onCraftedBy(this.player.level, this.player, this.removeCount);
            if (!this.player.level.isClientSide && this.be instanceof SmeltingBlockEntity) {
                ((SmeltingBlockEntity)be).unlockRecipes(this.player);

            }

            this.removeCount = 0;
            PlayerEvent.SMELT_ITEM.invoker().smelt(this.player, stack);
        }
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    protected void onCrafting(ItemStack stack) {

    }

}
