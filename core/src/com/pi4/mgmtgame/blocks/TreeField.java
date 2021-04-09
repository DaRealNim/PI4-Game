package com.pi4.mgmtgame.blocks;
import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pi4.mgmtgame.resources.Grain;
import com.pi4.mgmtgame.resources.Plant;
import com.pi4.mgmtgame.Map;
import com.pi4.mgmtgame.Popup;
import com.pi4.mgmtgame.ServerInteraction;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pi4.mgmtgame.Map;
import com.pi4.mgmtgame.Inventory;
public class TreeField extends Structure{
	private int growingState;

	public TreeField(int x, int y) {
		super(x, y);
		this.growingState = 0; 
	}
	
	public void addViewController(final AssetManager manager, final ServerInteraction server) {
		this.manager = manager;
		Button button = new Button(manager.get("blocks/Blocks.json", Skin.class), "field_empty");
		button.setX(getGridX() * 16);
		button.setY(getGridY() * 16);
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
                System.out.println("Clicked Field!");
				if (testOwner(server.getInternalTurn())) {
					Button buttonDestroy = new Button(manager.get("popupIcons/popup.json", Skin.class), "bomb_icon");
					Button buttonHarvest = new Button(manager.get("popupIcons/popup.json", Skin.class), "shovel_icon"); //mettre une hache svp
					final Popup p = new Popup((getGridX() - 2) * 16 + 8, (getGridY() + 1) * 16, manager,buttonDestroy, buttonHarvest);
					if(server.canHarvest(getGridX(), getGridY())) {
						buttonHarvest.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x, float y) {
								server.requestHarvest(getGridX(), getGridY());
								updateMap(manager, server);
								p.remove();
							}
						});
					} else {
						buttonHarvest.getColor().a = (float)0.3;
					}

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

	protected boolean hasTreeSeed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getConstructionCost() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDestructionGain() {
		// TODO Auto-generated method stub
		return 0;
	}

}
