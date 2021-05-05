package com.pi4.mgmtgame.blocks;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pi4.mgmtgame.HoverListener;
import com.pi4.mgmtgame.Inventory;
import com.pi4.mgmtgame.ManagementGame;
import com.pi4.mgmtgame.Popup;
import com.pi4.mgmtgame.ServerInteraction;
import com.pi4.mgmtgame.resources.Animal;
import com.pi4.mgmtgame.resources.Cow;
import com.pi4.mgmtgame.resources.Item;
import com.pi4.mgmtgame.resources.Sheep;
import com.pi4.mgmtgame.screens.MainGameScreen;

public class Pasture extends Structure{
	private Animal residentAnimal;
	private float growingState;
	
	public Pasture (int x, int y) {
		super(x, y);
		this.growingState = 1;
		setSpriteName("");
	}
	
	@Override
	public void addViewController(final AssetManager manager, final ServerInteraction server, final Stage popupStage) {
		super.addViewController(manager, server, popupStage);
		this.manager = manager;
		Button button = new Button(manager.get("blocks/Blocksjson", Skin.class), getSpriteName());
		button.setX(getGridX() * ManagementGame.TILE_SIZE);
		button.setY(getGridY() * ManagementGame.TILE_SIZE);
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Clicked Pasture !");
				if (testOwner(server.getInternalTurn())) {
					Button buttonBreed = new Button(manager.get("popupIcons/popup.json", Skin.class), "breed_icon");
					Button buttonDestroy = new Button(manager.get("popupIcons/popup.json", Skin.class), "bomb_icon");
					
					buttonBreed.addListener(new HoverListener() {
						@Override
						public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
							MainGameScreen.mouseLabelText = "Breed animals in pasture";
						}
					});
					buttonDestroy.addListener(new HoverListener() {
                        @Override
                        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                            MainGameScreen.mouseLabelText = "Destroy this building and sell the ressources\nRefund: $"+getDestructionGain();
                        }
                    });
					
					final Popup p = new Popup((getGridX() - 2) * ManagementGame.TILE_SIZE + ManagementGame.TILE_SIZE/2, (getGridY() + 1) * ManagementGame.TILE_SIZE, manager, "Enclot", buttonBreed, buttonDestroy);
					if (!hasAnimal()) {
						buttonBreed.getColor().a = (float)1;
						buttonBreed.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x, float y) {
								Button[] buttons = new Button[5];
								Button breedCow = new Button(manager.get("popupIcons/popup.json", Skin.class), "breed_icon");
								Button breedSheep = new Button(manager.get("popupIcons/popup.json", Skin.class), "breed_icon");
								Inventory inv = server.getInventory();
								if (inv.hasAnimal(0))
									buttons[0] = breedCow;
								
								if (inv.hasAnimal(1))
									buttons[1] = breedSheep;
								
								final Popup d = new Popup((getGridX() - 2) * ManagementGame.TILE_SIZE - ManagementGame.TILE_SIZE/4, (getGridY() + 1 ) * ManagementGame.TILE_SIZE + ManagementGame.TILE_SIZE/4, manager, "Enclot", buttons);
								
								breedCow.addListener(new ClickListener() {
									@Override
									public void clicked(InputEvent event, float x, float y) {
										attemptToBreed(0, server);
										d.remove();
										p.remove();
									}
								});
								
								breedSheep.addListener(new ClickListener() {
									@Override
									public void clicked(InputEvent event, float x, float y) {
										attemptToBreed(1, server);
										d.remove();
										p.remove();
									}
								});
							}
						});
					}
				}
			}		
		});
	}
	
	@Override
	public void updateActors() {
		super.updateActors();
		double growRatio = 0;
		if (residentAnimal != null) {
			growRatio = ((double)this.growingState / (double)this.residentAnimal.getGrowingMax());
		}
		if (residentAnimal == null) {
			changeStyle("pasture_empty");
		} else if (growRatio >= 0.5 && growRatio < 1) {
			changeStyle(residentAnimal.getPastureMiddleSpriteName());
		} else if (!hasPopulationGrown()) {
			changeStyle("pasture_planted");
		} else {
			changeStyle(residentAnimal.getPastureSpriteName());
		}
	}
	
	@Override
	public int getConstructionCost() {
		return 1000;
	}

	@Override
	public int getDestructionGain() {
		return 330;
	}
	
	public void breedAnimal(Animal animal) {
		this.residentAnimal = animal;
	}
	
	public boolean hasAnimal() {
		return (this.residentAnimal != null);
	}
	
	public boolean hasPopulationGrown() {
		if (this.residentAnimal == null)
			return false;
		return (this.growingState >= this.residentAnimal.getGrowingMax());
	}
	
	public void growPopulation(ServerInteraction server) {
		Inventory inv = server.getInventory();
		
		if (residentAnimal != null) {
			System.out.println("Population grew form Pasture in (" + super.getGridX() + "," + super.getGridY() + ")");
			if (inv.getPlants()[0].getVolume() >= (int)(1+1*growingState/2)) {
				inv.getPlants()[0].subVolume((int)(1+1*growingState/2));
				growingState += (int)1+1*growingState/2;
				if (growingState > residentAnimal.getGrowingMax()) growingState = residentAnimal.getGrowingMax();
			} else {
				growingState--;
			}
			
			if (hasPopulationGrown()) {
				changeStyle(residentAnimal.getPastureSpriteName());
			}
			
			inv.getProduct()[0].addVolume((int)(2+1*growingState/2));
			if (residentAnimal instanceof Sheep) {
				inv.getProduct()[2].addVolume((int)(1+1*growingState/2));
			} else if (residentAnimal instanceof Cow) {
				inv.getProduct()[1].addVolume((int)(1+1*growingState/2));
			}
		}
	}
	
	private void attemptToBreed(int i, ServerInteraction server) {
		Inventory inv = server.getInventory();
		//boolean res = server.requestBreedAnimal();
		System.out.println(inv.getAnimals()[i].getId());
		System.out.println("Could breed animal of id " + i + " at " + getGridX() + ", " + getGridY() + ": "/* + res*/);
		updateMap(manager, server);
	}
	
	public void passTurn(ServerInteraction server) {
		this.growPopulation(server);

	}
	
	@Override
	public boolean canBuild(Inventory inv) {
		return true;
	}

	@Override
	public void doBuild(Inventory inv) {}
	
	@Override
	public String toString() {
		return "Pasture";
	}
}
