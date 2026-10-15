package wily.betterfurnaces.tile;

import static wily.betterfurnaces.Config.diamondTierSpeed;
import static wily.betterfurnaces.Config.diamondParallel;

public class TileEntityDiamondFurnace extends TileEntitySmeltingBase {

	@Override
	protected int getDefaultCookTime() {
		return diamondTierSpeed;
	}

	@Override
	protected int getMaxParallel(int slotIndex) {
		return diamondParallel;
	}
}