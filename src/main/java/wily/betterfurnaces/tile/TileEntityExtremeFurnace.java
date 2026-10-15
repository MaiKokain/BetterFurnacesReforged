package wily.betterfurnaces.tile;

import static wily.betterfurnaces.Config.extremeTierSpeed;
import static wily.betterfurnaces.Config.extremeParallel;

public class TileEntityExtremeFurnace extends TileEntitySmeltingBase {
	@Override
	protected int getDefaultCookTime() {
		return extremeTierSpeed;
	}

	@Override
	protected int getMaxParallel(int slotIndex) {
		return extremeParallel;
	}
}