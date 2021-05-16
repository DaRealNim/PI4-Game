package com.pi4.mgmtgame.blocks;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pi4.mgmtgame.Inventory;
import com.pi4.mgmtgame.ManagementGame;
import com.pi4.mgmtgame.Popup;
import com.pi4.mgmtgame.ServerInteraction;

public class HQ extends Structure{
private int researchStatus=1;

	public HQ(int x, int y) {
	        super(x, y);
	        setSpriteName("QG-1");
	    }


	@Override
    public void addViewController(final AssetManager manager, final ServerInteraction server, final Stage popupStage) {
		super.addViewController(manager, server, popupStage);
		this.manager = manager;
		Button button = new Button(manager.get("blocks/Blocks.json", Skin.class), getSpriteName());
		button.setX(getGridX() * ManagementGame.TILE_SIZE);
		button.setY(getGridY() * ManagementGame.TILE_SIZE);
		setButton(button);
    }

	@Override
	public int getConstructionCost() {
		return 0;
	}

	@Override
	public int getDestructionGain() {
		return 0;
	}

	@Override
	public int getMaintenanceCost() {
		return 10;
	}

	@Override
	public boolean canBuild(Inventory inv) {
		return false;
	}

	@Override
	public void doBuild(Inventory inv) {
	}

	@Override
	public String toString() {
		return "House";
	}

}
