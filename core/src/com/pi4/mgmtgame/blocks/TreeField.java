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
import com.pi4.mgmtgame.resources.TreeSeeds;
import com.pi4.mgmtgame.Map;
import com.pi4.mgmtgame.Popup;
import com.pi4.mgmtgame.ServerInteraction;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pi4.mgmtgame.Map;
import com.pi4.mgmtgame.Inventory;
import com.pi4.mgmtgame.ManagementGame;
import com.pi4.mgmtgame.screens.MainGameScreen;
import com.pi4.mgmtgame.HoverListener;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TreeField extends Field{
	private float growingState;
	final private Grain plantedSeed = new TreeSeeds();
	public TreeField(int x, int y) {
        super(x, y);
		this.growingState = 0;
		setSpriteName("treefarm");
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
                System.out.println("Clicked TreeField!");
                if(getOwnerID() == -1) {
                    Button buttonBuyTerrain = new Button(manager.get("popupIcons/popup.json", Skin.class), "dollar_icon");
					buttonBuyTerrain.addListener(new HoverListener() {
                        @Override
                        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                            MainGameScreen.mouseLabelText = "Buy this terrain\nCost: $500";
                        }
                    });
                    final Popup p = new Popup((getGridX() - 2) * ManagementGame.TILE_SIZE + ManagementGame.TILE_SIZE/2, (getGridY() + 1) * ManagementGame.TILE_SIZE, manager, "Tree", buttonBuyTerrain);
                    if (server.canBuyTerrain(getGridX(), getGridY())) {
                        buttonBuyTerrain.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                boolean res = server.requestBuyTerrain(getGridX(), getGridY());
                                updateMap(manager, server);
                                p.remove();
                            }
                        });
                    } else {
                        buttonBuyTerrain.getColor().a = 0.3f;
                    }
                    popupStage.addActor(p);
                }
				if (testOwner(server.getInternalTurn())) {
					Button buttonDestroy = new Button(manager.get("popupIcons/popup.json", Skin.class), "bomb_icon");
					Button buttonHarvest = new Button(manager.get("popupIcons/popup.json", Skin.class), "axe_icon");

					buttonDestroy.addListener(new HoverListener() {
                        @Override
                        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                            MainGameScreen.mouseLabelText = "Destroy this building and sell the ressources\nRefund: $"+getDestructionGain();
                        }
                    });
					buttonHarvest.addListener(new HoverListener() {
						@Override
						public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
							MainGameScreen.mouseLabelText = "Cut this tree\nCut this tree and get 4 units of wood";
						}
					});

					final Popup p = new Popup((getGridX() - 2) * ManagementGame.TILE_SIZE + ManagementGame.TILE_SIZE/2, (getGridY() + 1) * ManagementGame.TILE_SIZE, manager, "Tree", buttonHarvest, buttonDestroy);
					if(server.canHarvest(getGridX(), getGridY())) {
						buttonHarvest.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x, float y) {
								if(server.requestHarvest(getGridX(), getGridY())==true)
									server.requestDestroyStructure(getGridX(), getGridY());
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

					popupStage.addActor(p);
				}
			}
		});

		setButton(button);
	}

	protected boolean hasTreeSeed() {
		return true;
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
	public void growSeed() {
		if (plantedSeed != null && !hasSeedGrown()) {
			System.out.println("Grew field for block (" + super.getGridX() + "," + super.getGridY() + ")");
			this.growingState++;
			if (hasSeedGrown()) {
				changeStyle(plantedSeed.getFieldSpriteName());
			}
		}
	}

	@Override
	public void passTurn(Inventory inv) {
		this.growSeed();
	}

	@Override
	public void updateActors() {
		super.updateActors();
		if (plantedSeed == null) {
			changeStyle("none");
		} else if (!hasSeedGrown()) {
			changeStyle("treefarm");
		} else {
			changeStyle("treefarm_grew");
		}
	}

	@Override
	public boolean hasSeedGrown() {
		return (this.growingState >= this.plantedSeed.getGrowingTime());
	}

	@Override
	public boolean canBuild(Inventory inv) {
		if(inv.hasGrain(plantedSeed)) {
			return true;
		}
		return false;
	}

	@Override
	public void doBuild(Inventory inv) {
		inv.removeGrain(plantedSeed.getId(), 1);
	}

	@Override
	public Plant harvest() {
		if (hasSeedGrown()) {
			Plant grown = plantedSeed.getGrownPlant();
			return grown;
		}
		return null;
	}

	public void growSeedCompletely() {
		this.growingState = this.plantedSeed.getGrowingTime();
	}

	@Override
	public String toString() {
		return "Tree";
	}


}
