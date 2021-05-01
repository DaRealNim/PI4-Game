package com.pi4.mgmtgame.blocks;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pi4.mgmtgame.resources.Crickets;
import com.pi4.mgmtgame.resources.Grain;
import com.pi4.mgmtgame.resources.Item;
import com.pi4.mgmtgame.resources.Plant;
import com.pi4.mgmtgame.Map;
import com.pi4.mgmtgame.Popup;
import com.pi4.mgmtgame.ServerInteraction;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pi4.mgmtgame.Map;
import com.pi4.mgmtgame.Inventory;
import com.pi4.mgmtgame.ManagementGame;
import com.pi4.mgmtgame.blocks.Lake;
import com.pi4.mgmtgame.resources.Crickets;

public class Field extends Structure {
	private Grain plantedSeed;
	private float growingState;
	private Item usedItem;
	private int turnsSinceCrickets;
	private float growFactor;

	public Field(int x, int y) {
        super(x, y);
		this.growingState = 0;
		this.growFactor = 3;
		setSpriteName("field_empty");
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
                System.out.println("Clicked Field!");
				if (testOwner(server.getInternalTurn())) {
					Button buttonPlant = new Button(manager.get("popupIcons/popup.json", Skin.class), "shovel_icon");
					Button buttonHarvest = new Button(manager.get("popupIcons/popup.json", Skin.class), "harvest_icon");
					Button buttonDestroy = new Button(manager.get("popupIcons/popup.json", Skin.class), "bomb_icon");
					Button buttonRepulsive = new Button(manager.get("popupIcons/popup.json", Skin.class), "closeButton");

					final Popup p = new Popup((getGridX() - 2) * ManagementGame.TILE_SIZE + ManagementGame.TILE_SIZE/2, (getGridY() + 1) * ManagementGame.TILE_SIZE, manager, buttonPlant,
							buttonHarvest, buttonDestroy, buttonRepulsive);
					if (!hasSeed()) {
						buttonPlant.getColor().a = (float)1;
						buttonPlant.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x, float y) {
								Button[] buttons = new Button[5];
								Button plantWheat = new Button(manager.get("popupIcons/popup.json", Skin.class),
										"wheatseeds_icon");
								Button plantPotato = new Button(manager.get("popupIcons/popup.json", Skin.class),
										"potatoseeds_icon");
								Button plantCarrot = new Button(manager.get("popupIcons/popup.json", Skin.class),
										"carrotseeds_icon");
								Inventory inv = server.getInventory();
								if (inv.hasGrain(0))
									buttons[0] = plantWheat;

								if (inv.hasGrain(1))
									buttons[1] = plantPotato;

								if (inv.hasGrain(2))
									buttons[2] = plantCarrot;

								final Popup d = new Popup((getGridX() - 2) * ManagementGame.TILE_SIZE - ManagementGame.TILE_SIZE/4, (getGridY() + 1) * ManagementGame.TILE_SIZE + ManagementGame.TILE_SIZE/4, manager,buttons);
								popupStage.addActor(d);

								plantWheat.addListener(new ClickListener() {
									@Override
									public void clicked(InputEvent event, float x, float y) {
										attemptToPlant(0, server);
										d.remove();
		                                p.remove();
									}
								});

								plantPotato.addListener(new ClickListener() {
									@Override
									public void clicked(InputEvent event, float x, float y) {
										attemptToPlant(1, server);
										d.remove();
		                                p.remove();
									}
								});

								plantCarrot.addListener(new ClickListener() {
									@Override
									public void clicked(InputEvent event, float x, float y) {
										attemptToPlant(2, server);
										d.remove();
		                                p.remove();
									}
								});

							}
						});
					} else {
						buttonPlant.getColor().a = (float)0.3;
					}

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
					if(usedItem instanceof Crickets&&server.getInventory().hasItem(1)) {
						buttonRepulsive.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x, float y) {
								removeCrickets();
								updateMap(manager, server);
								p.remove();
							}
						});					}
					buttonDestroy.addListener(new ClickListener() {
						@Override
						public void clicked(InputEvent event, float x, float y) {
							server.requestDestroyStructure(getGridX(), getGridY());
							updateMap(manager, server);
							p.remove();
						}
					});

					popupStage.addActor(p);
				} else {
					Button buttonCricket = new Button(manager.get("popupIcons/popup.json", Skin.class), "bomb_icon");
					final Popup c = new Popup((getGridX() - 2) * ManagementGame.TILE_SIZE + ManagementGame.TILE_SIZE/2, (getGridY() + 1) * ManagementGame.TILE_SIZE, manager, buttonCricket);
					if(!(usedItem instanceof Crickets) && server.getInventory().hasItem(0)) {
						buttonCricket.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x, float y) {
								server.requestUseItem(getGridX(), getGridY(), new Crickets());
								updateMap(manager, server);
								c.remove();
							}
						});
					} else {
						buttonCricket.getColor().a = (float)0.3;
					}
					popupStage.addActor(c);
				}
			}
		});

		setButton(button);
	}

	@Override
	public void updateActors() { //besoin d'un sprite a superposer si item appliqué
		super.updateActors();
		double growRatio = 0;
		if (plantedSeed != null) {
			growRatio = ((double)this.growingState / (double)this.plantedSeed.getGrowingTime());
		}
		if (plantedSeed == null) {
			changeStyle("field_empty");
		} else if (growRatio >= 0.5 && growRatio < 1) {
			changeStyle(plantedSeed.getFieldMiddleSpriteName());
		} else if (!hasSeedGrown()) {
			changeStyle("field_planted");
		} else {
			changeStyle(plantedSeed.getFieldSpriteName());
		}
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

    public boolean hasSeed() {
        return (this.plantedSeed != null);
    }

    public boolean hasItem() {
    	return (this.usedItem!=null);
    }

    public Item usedItem() {
    	return (this.usedItem);
    }

	public void growSeed() {
		if (plantedSeed != null && !hasSeedGrown()) {
			System.out.println("Grew field for block (" + super.getGridX() + "," + super.getGridY() + ")");
			this.growingState+=growFactor+getNearbyBoosts()*0.3f;
			if (hasSeedGrown()) {
				changeStyle(plantedSeed.getFieldSpriteName());
			}
		}
	}


	private void attemptToPlant(int i, ServerInteraction server) {
		// ERREUR (renvoie tjrs 0)
		Inventory inv = server.getInventory();
		boolean res = server.requestPlantSeed(getGridX(), getGridY(), inv.getSeeds()[i]);
		System.out.println(inv.getSeeds()[i].getId());
		// Todo Fix cette merde
		System.out.println("Could plant seed of id " + i + " at " + getGridX() + ", " + getGridY() + ": " + res);
		updateMap(manager, server);
	}

	public Plant harvest() {
		if (hasSeedGrown()) {
			this.growingState = 0;
			Plant grown = plantedSeed.getGrownPlant();
			plantedSeed = null;
			return grown;
		}
		return null;
	}

	@Override
	public void passTurn() {
		this.growSeed();

		if(usedItem != null && usedItem.getId()==1)
			this.turnsSinceCrickets++;

		this.growFactor -= this.turnsSinceCrickets*0.2;

		if(this.growingState<0)
			this.growingState=0;

		if(this.turnsSinceCrickets>=3) {
			cricketSpread();
		}

	}

	private void cricketSpread() {
		Random rand = new Random();
		Structure temp = getAdjacentStruct().get(rand.nextInt(getAdjacentStruct().size()));
		if(temp instanceof Field)
			((Field) temp).addCrickets(this.usedItem);
	}

	@Override
	public boolean canBuild(Inventory inv) {
		return true;
	}

	@Override
	public void doBuild(Inventory inv) {}

	@Override
	public String toString() {
		return "Field";
	}

	public void UseItem(Item item) {
		switch(item.getId()) {
		case 1 :
			addCrickets(item);
			break;
		}

	}

	private void addCrickets(Item crickets) {
		this.usedItem=crickets;
		if (this.growingState>0)
			this.growingState--;

	}

	private void removeCrickets(){
		this.usedItem=null;
		this.turnsSinceCrickets=0;
		//mettre qqc pour virer le sprite
	}



}
