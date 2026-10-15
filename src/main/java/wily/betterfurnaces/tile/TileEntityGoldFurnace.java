package wily.betterfurnaces.tile;

import static wily.betterfurnaces.Config.goldTierSpeed;
import static wily.betterfurnaces.Config.goldParallel;


public class TileEntityGoldFurnace extends TileEntitySmeltingBase {

	@Override
	protected int getDefaultCookTime() {
		return goldTierSpeed;
	}

	@Override
	protected int getMaxParallel(int slotIndex) {
		return goldParallel;
	}
}
