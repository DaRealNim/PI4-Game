package com.pi4.mgmtgame.blocks;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.pi4.mgmtgame.resources.Grain;
import com.pi4.mgmtgame.Map;
import com.pi4.mgmtgame.Popup;
import com.pi4.mgmtgame.blocks.Field;
import com.pi4.mgmtgame.ServerInteraction;


public class Lake extends Environment {

    public Lake(int x, int y) {
    	super(x, y);
    }

    @Override
    public void addViewController(final AssetManager manager, final ServerInteraction server) {
        super.addViewController(manager, server, "lake");
    }

    @Override
    public int getGrowingPenalty() {
        return 0;
    }

    @Override
    public boolean canSeedGrow(Grain seed) {
        return false;
    }

    @Override
    public boolean canBuild(Structure struct) {
        return false;
    }

    @Override
    public void passTurn() {
	}

    @Override
	public String toString() {
		return "Lake";
	}
}
