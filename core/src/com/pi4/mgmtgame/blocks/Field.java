package com.pi4.mgmtgame.blocks;

import com.pi4.mgmtgame.resources.Grain;

public class Field extends Structure {
    private Grain plantedSeed;
    private int growingState;

    public Field() {
        this.growingState = 0;
    }

    @Override
    public int getConstructionCost() {
        return 300;
    }

    @Override
    public int getDestructionGain() {
        return 100;
    }

    public void plantSeed(Grain seed) {
        this.plantedSeed = seed;
    }

    public boolean hasSeedGrown() {
        if (this.plantedSeed == null)
            return false;
        return (this.growingState >= this.plantedSeed.getGrowingTime());
    }

    public void growSeed() {
        this.growingState++;
    }

    public Grain harvest() {
        if (hasSeedGrown()) {
            Grain ret = plantedSeed;
            this.plantedSeed = null;
            this.growingState = 0;
            return ret;
        } else {
            return null;
        }
    }


}
