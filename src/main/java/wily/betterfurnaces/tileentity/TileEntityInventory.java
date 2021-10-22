package wily.betterfurnaces.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public abstract class TileEntityInventory extends BlockEntity implements ITileInventory, WorldlyContainer, MenuProvider, Nameable {

    public NonNullList<ItemStack> inventory;
    protected Component name;

    public TileEntityInventory(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state, int sizeInventory) {
        super(tileEntityTypeIn, pos, state);
        inventory = NonNullList.withSize(sizeInventory, ItemStack.EMPTY);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        this.setChanged();
        return new ClientboundBlockEntityDataPacket(getBlockPos(), -1, getUpdateTag());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt){
        CompoundTag tag = pkt.getTag();
        this.load(tag);
        this.setChanged();
        level.markAndNotifyBlock(worldPosition, level.getChunkAt(worldPosition), level.getBlockState(worldPosition).getBlock().defaultBlockState(), level.getBlockState(worldPosition), 2, 3);
    }

    @Override
    public CompoundTag getUpdateTag() {

        CompoundTag tag = new CompoundTag();
        return this.save(tag);
    }


    public void setCustomName(Component name) {
        this.name = name;
    }

    @Override
    public Component getName() {
        return (this.name != null ? this.name : new TranslatableComponent(IgetName()));
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return this.IgetSlotsForFace(side);
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return IisItemValidForSlot(i, itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return IcanExtractItem(i, itemStack, direction);
    }

    @Override
    public int getContainerSize() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.inventory) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    public void breakDurabilityItem(ItemStack stack){
        if (stack.isDamageableItem()) {
            stack.hurt(1, null, null);
        }
            if (stack.isDamageableItem() && stack.getDamageValue() >= stack.getMaxDamage()) {
                stack.shrink(1);
                this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.inventory.get(slot);
    }

    @Override
    public ItemStack removeItem(int i, int i1) {
        return ContainerHelper.removeItem(this.inventory, i, i1);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return ContainerHelper.takeItem(this.inventory, i);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        ItemStack itemstack = this.inventory.get(index);
        boolean flag = !stack.isEmpty() && stack.sameItem(itemstack) && ItemStack.tagMatches(stack, itemstack);
        this.inventory.set(index, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
    }

    @Override
    public int getMaxStackSize() {
        return WorldlyContainer.super.getMaxStackSize();
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.inventory = NonNullList.withSize(this.getMaxStackSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.inventory);
        if (nbt.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(nbt.getString("CustomName"));
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        super.save(nbt);
        ContainerHelper.saveAllItems(nbt, this.inventory);
        if (this.name != null) {
            nbt.putString("CustomName", Component.Serializer.toJson(this.name));
        }
        return nbt;
    }

    @Override
    public boolean stillValid(Player playerEntity) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return !(playerEntity.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) > 64.0D);
        }
    }

    @Override
    public boolean hasCustomName() {
        return this.name != null;
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return this.name;
    }

    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return IcreateMenu(i, playerInventory, playerEntity);
    }

    @Override
    public void clearContent() {
        this.inventory.clear();
    }
}
