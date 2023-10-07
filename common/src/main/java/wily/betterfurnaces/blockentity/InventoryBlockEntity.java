package wily.betterfurnaces.blockentity;

import dev.architectury.registry.menu.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wily.betterfurnaces.network.Messages;
import wily.betterfurnaces.network.PacketSyncAdditionalInt;
import wily.factoryapi.FactoryAPIPlatform;
import wily.factoryapi.base.Bearer;
import wily.factoryapi.base.IFactoryStorage;
import wily.factoryapi.base.IPlatformItemHandler;

import java.util.ArrayList;
import java.util.List;

public abstract class InventoryBlockEntity extends BlockEntity implements IInventoryBlockEntity, ExtendedMenuProvider, Nameable, IFactoryStorage {

    protected Component name;

    public IPlatformItemHandler<?> inventory;

    public List<Bearer<Integer>> additionalSyncInts = new ArrayList<>();

    public InventoryBlockEntity(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
        inventory = FactoryAPIPlatform.getItemHandlerApi(getInventorySize(),this);
        inventory.setExtractableSlots(this::IcanExtractItem);
        inventory.setInsertableSlots(this::IisItemValidForSlot);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        this.setChanged();
        return ClientboundBlockEntityDataPacket.create(this);
    }
    public void handleUpdateTag(CompoundTag tag){
        if (tag != null)
            load(tag);
        setChanged();


    }
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt){
        CompoundTag tag = pkt.getTag();
        this.load(tag);
        this.setChanged();
        level.setBlock(worldPosition, level.getBlockState(worldPosition), 2, 3);
    }
    public void updateBlockState(){
        level.sendBlockUpdated(getBlockPos(), level.getBlockState(getBlockPos()), getBlockState(), 2);
    }
    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }
    @Override
    public void saveExtraData(FriendlyByteBuf buf) {
        buf.writeBlockPos(getBlockPos());
    }

    public void setCustomName(Component name) {
        this.name = name;
    }

    @Override
    public Component getName() {
        return (this.name != null ? this.name : getDisplayName());
    }

    @Override
    public boolean IisItemValidForSlot(int index, ItemStack stack) {
        return getSlots(null).get(index).mayPlace(stack);
    }

    public void breakDurabilityItem(ItemStack stack){
        if (!stack.isEmpty() && stack.isDamageableItem()) {
            stack.hurt(1, level.random, null);
            if (stack.getDamageValue() >= stack.getMaxDamage()) {
                stack.shrink(1);
                this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }
    public void syncAdditionalMenuData(AbstractContainerMenu menu, Player player){
        if (player instanceof ServerPlayer sp){
            additionalSyncInts.forEach(i-> Messages.INSTANCE.sendToPlayer(sp,new PacketSyncAdditionalInt(getBlockPos(),additionalSyncInts,i,i.get())));
        }
    }
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (!additionalSyncInts.isEmpty()) {
            int[] ints = tag.getIntArray("additionalInts");
            for (int i = 0; i < ints.length; i++) additionalSyncInts.get(i).set(ints[i]);
        }
        inventory.deserializeTag(tag.getCompound("inventory"));
        if (tag.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(tag.getString("CustomName"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!additionalSyncInts.isEmpty())
            tag.putIntArray("additionalInts",additionalSyncInts.stream().map(Bearer::get).toList());
        tag.put("inventory", inventory.serializeTag());
        if (this.name != null) {
            tag.putString("CustomName", Component.Serializer.toJson(this.name));
        }
    }

    @Override
    public Component getDisplayName() {
        return getBlockState().getBlock().getName();
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


    public @NotNull IPlatformItemHandler getInv() {
        return inventory;
    }


    public abstract boolean IcanExtractItem(int index, ItemStack stack);
}
