package wily.betterfurnaces.network;

import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.factoryapi.base.Storages;

import java.util.function.Supplier;

public class PacketSyncFluid {
    private final FluidStack stack;
    private final Direction direction;
    private final BlockPos pos;

    public PacketSyncFluid(FriendlyByteBuf buf) {
        this(buf.readBlockPos(),Direction.values()[buf.readInt()], FluidStack.read(buf) );

    }

    public PacketSyncFluid(BlockPos pos, Direction direction, FluidStack stack) {
        this.pos = pos;
        this.direction = direction;
        this.stack = stack;

    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(direction.ordinal());
        stack.write(buf);
    }

    public void apply(Supplier<NetworkManager.PacketContext> ctx) {
        ctx.get().queue(() -> {
            Player player = ctx.get().getPlayer();
            BlockEntity be = player.level.getBlockEntity(pos);
            if (player.level.isLoaded(pos)) {
                ((SmeltingBlockEntity)be).getStorage(Storages.FLUID,direction).ifPresent(t-> t.setFluid(stack));
                be.setChanged();
            }
        });
    }
}
