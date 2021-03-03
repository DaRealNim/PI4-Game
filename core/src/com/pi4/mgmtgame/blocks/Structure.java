package com.pi4.mgmtgame.blocks;

import com.badlogic.gdx.assets.AssetManager;

public abstract class Structure extends Block {
    //Skeleton

    public Structure(AssetManager manager) {
        super(manager);
    }

    abstract public int getConstructionCost();
    abstract public int getDestructionGain();
}
