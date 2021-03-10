package com.pi4.mgmtgame.blocks;

import com.badlogic.gdx.assets.AssetManager;

public abstract class Structure extends Block {

    public Structure(int x, int y, AssetManager manager) {
    	super(x, y, manager);
    }

    abstract public int getConstructionCost();
    abstract public int getDestructionGain();
    
    @Override
    public void passTurn() {
    	System.out.println("Nothing to do at (" + super.getGridX() + "," + super.getGridY() + ")");
	}
}
