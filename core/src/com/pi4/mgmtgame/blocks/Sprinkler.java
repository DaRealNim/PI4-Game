package com.pi4.mgmtgame.blocks;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pi4.mgmtgame.Inventory;
import com.pi4.mgmtgame.Popup;
import com.pi4.mgmtgame.ServerInteraction;
import com.pi4.mgmtgame.ManagementGame;
import com.pi4.mgmtgame.Map;

public class Sprinkler extends Structure{

	public Sprinkler(int x, int y) {
        super(x, y);
		setSpriteName("treefarm");
	}

	 @Override
	    public void addViewController(final AssetManager manager, final ServerInteraction server) {
		 this.manager = manager;
			Button button = new Button(manager.get("blocks/Blocks.json", Skin.class), getSpriteName());
			button.setX(getGridX() * ManagementGame.TILE_SIZE);
			button.setY(getGridY() * ManagementGame.TILE_SIZE);
			button.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
	                System.out.println("Clicked Field!");
					if (testOwner(server.getInternalTurn())) {
						Button buttonDestroy = new Button(manager.get("popupIcons/popup.json", Skin.class), "bomb_icon");
						final Popup p = new Popup((getGridX() - 2) * ManagementGame.TILE_SIZE + ManagementGame.TILE_SIZE/2, (getGridY() + 1) * ManagementGame.TILE_SIZE, manager, buttonDestroy);
						buttonDestroy.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x, float y) {
								server.requestDestroyStructure(getGridX(), getGridY());
								updateMap(manager, server);
								p.remove();
							}
						});

						getStage().addActor(p);
					}
				}
			});
			setButton(button);
	    }

	@Override
	public int getConstructionCost() {
		return 350;
	}

	@Override
	public int getDestructionGain() {
		return 75;
	}

	@Override
	public boolean canBuild(Inventory inv) {
		return true;
	}

	@Override
	public void doBuild(Inventory inv) {}

	@Override
	public String toString() {
		return "Sprinkler";
	}

	@Override
    public void passTurn() {
	}
}
