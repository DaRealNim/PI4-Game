package com.pi4.mgmtgame.blocks;
import com.pi4.mgmtgame.Inventory;

import java.io.Serializable;

import com.badlogic.gdx.assets.AssetManager;
import com.pi4.mgmtgame.Map;

public abstract class Structure extends Block implements Serializable {


	public Structure(int x, int y) {
        super(x, y);
	}

	abstract public int getConstructionCost();

	abstract public int getDestructionGain();

	abstract public boolean canBuild(Inventory inv);
	abstract public void doBuild(Inventory inv);

	abstract public int getMaintenanceCost();

	@Override
	public void passTurn(Inventory inv) {
		if (inv != null)
			inv.giveMoney(getMaintenanceCost());
	}
}
