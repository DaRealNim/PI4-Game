package com.pi4.mgmtgame.blocks;

import com.pi4.mgmtgame.resources.Grain;

public class Plain extends Environment {
    //Skeleton

    @Override
    public int getGrowingPenalty() {
        return 0;
    }

    @Override
    public boolean canSeedGrow(Grain seed) {
        return true;
    }

    @Override
    public boolean canBuild(Structure struct) {
        return true;
    }
}
