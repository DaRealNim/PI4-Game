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
private int researchStatus;

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
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (testOwner(server.getInternalTurn())) {
					Button buttonResearch = new Button();
					final Popup p = new Popup((getGridX() - 2) * ManagementGame.TILE_SIZE + ManagementGame.TILE_SIZE/2, (getGridY() + 1) * ManagementGame.TILE_SIZE, manager, "House", buttonResearch);
					buttonResearch = getResearchButton(p,server);
					popupStage.addActor(p);
					updateMap(manager, server);
				}
			}
		});
		setButton(button);
    }
	private Button getResearchButton(final Popup p,final ServerInteraction server) {
		Button buttonResearch = new Button();
		switch(this.researchStatus) {
		  case 0:
			  buttonResearch = new Button(manager.get("popupIcons/popup.json", Skin.class), "closeButton");
			  buttonResearch.addListener(new ClickListener(){
                  @Override
                  public void clicked(InputEvent event, float x, float y) {
                	  research(server);
                      p.remove();
                  }
              });
		    break;
		}
		return buttonResearch;

	}
	private void research(ServerInteraction server) {
		switch(this.researchStatus) {
		  case 0:
			  server.getInventory().addItem(2,1);
			  researchStatus++;
		  case 1:
			  if((!server.testRod())&&server.getInventory().hasPlant(3))
					  server.fixRod();	  
		break;
		}
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
	public String toString() {
		return "House";
	}

}
