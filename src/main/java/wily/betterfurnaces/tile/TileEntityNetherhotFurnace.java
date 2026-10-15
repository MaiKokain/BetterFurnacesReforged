package wily.betterfurnaces.tile;

import static wily.betterfurnaces.Config.netherhotTierSpeed;
import static wily.betterfurnaces.Config.netherhotParallel;

public class TileEntityNetherhotFurnace extends TileEntitySmeltingBase {

	@Override
	protected int getDefaultCookTime() {
		return netherhotTierSpeed;
	}

	@Override
	protected int getMaxParallel(int slotIndex) {
		return netherhotParallel;
	}
}