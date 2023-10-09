package wily.betterfurnaces.network;

import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;

import java.util.function.Supplier;

public class PacketShowSettingsButton {

private final BlockPos pos;
	private final int set;

	public PacketShowSettingsButton(FriendlyByteBuf buf) {
		this(buf.readBlockPos(),buf.readInt());
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
		buf.writeInt(set);
	}

	public PacketShowSettingsButton(BlockPos pos, int set) {
		this.pos = pos;
		this.set = set;
	}

	public void handle(Supplier<NetworkManager.PacketContext> ctx) {
		ctx.get().queue(() -> {
			ServerPlayer player = (ServerPlayer) ctx.get().getPlayer();
			SmeltingBlockEntity be = (SmeltingBlockEntity) player.getLevel().getBlockEntity(pos);
			if (player.level.isLoaded(pos)) {
				be.showInventorySettings = set;
				be.getLevel().setBlock(pos, be.getLevel().getBlockState(pos), 2, 3);
				be.setChanged();
			}
		});
	}
}
