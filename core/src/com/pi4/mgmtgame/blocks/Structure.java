package com.pi4.mgmtgame.blocks;

import java.io.Serializable;

import com.badlogic.gdx.assets.AssetManager;

public abstract class Structure extends Block implements Serializable {
	private int ownerID;

	public Structure(int x, int y) {
		super(x, y);
	}

	public boolean testOwner(int x) {
		if (x == ownerID)
			return true;
		return false;
	}

	public int getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(int x) {
		ownerID = x;
	}

	abstract public int getConstructionCost();

	abstract public int getDestructionGain();

	@Override
	public void passTurn() {
	}
}
