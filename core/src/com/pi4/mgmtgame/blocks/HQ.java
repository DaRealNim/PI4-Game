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

	public HQ(int x, int y) {
	        super(x, y);
	        setSpriteName("HQ");
	    }


	@Override
    public void addViewController(final AssetManager manager, final ServerInteraction server, final Stage popupStage) {
		super.addViewController(manager, server, popupStage);
		this.manager = manager;
		Button button = new Button(manager.get("blocks/Blocks.json", Skin.class), getSpriteName());
		button.setX(getGridX() * ManagementGame.TILE_SIZE);
		button.setY(getGridY() * ManagementGame.TILE_SIZE);
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (testOwner(server.getInternalTurn())) {
					Button buttonResearch = new Button(manager.get("popupIcons/popup.json", Skin.class), "closeButton");
					final Popup p = new Popup((getGridX() - 2) * ManagementGame.TILE_SIZE + ManagementGame.TILE_SIZE/2, (getGridY() + 1) * ManagementGame.TILE_SIZE, manager, "House", buttonResearch);
					buttonResearch.addListener(new ClickListener() {
						@Override
						public void clicked(InputEvent event, float x, float y) {
							Research();
							updateMap(manager, server);
							p.remove();
						}
					});

					popupStage.addActor(p);
				}
			}
		});
		setButton(button);
    }

	private void Research() {
		//TBD
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
	public boolean canBuild(Inventory inv) {
		return false;
	}

	@Override
	public void doBuild(Inventory inv) {
	}

	@Override
    public void passTurn() {
	}

	@Override
	public String toString() {
		return "House";
	}

}
