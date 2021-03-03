package com.pi4.mgmtgame.blocks;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import com.pi4.mgmtgame.resources.Grain;


public class Plain extends Environment {
    //Skeleton

    public Plain(AssetManager manager) {
        super(manager);
        Button button = new Button(manager.get("blocks/Blocks.json", Skin.class), "plain");
        setButton(button);
    }

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
