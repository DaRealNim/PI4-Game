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
	private int constructionCost;
	private int salvageBenefit;

	public Pasture (int x, int y) {
		super(x, y);
		this.growingState = 1;
		setSpriteName("pasture_empty");

		constructionCost = 600;
		salvageBenefit = 400;
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
				System.out.println("Clicked Pasture !");
				if (testOwner(server.getInternalTurn())) {
					Button breedCow = new Button(manager.get("popupIcons/popup.json", Skin.class), "breed_cow_icon");
					Button breedSheep = new Button(manager.get("popupIcons/popup.json", Skin.class), "breed_sheep_icon");
					Button buttonDestroy = new Button(manager.get("popupIcons/popup.json", Skin.class), "bomb_icon");

					breedCow.addListener(new HoverListener() {
						@Override
						public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
							MainGameScreen.mouseLabelText = "Breed cows in pasture";
						}
					});
					breedSheep.addListener(new HoverListener() {
						@Override
						public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
							MainGameScreen.mouseLabelText = "Breed sheeps in pasture";
						}
					});
					buttonDestroy.addListener(new HoverListener() {
                        @Override
                        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                            MainGameScreen.mouseLabelText = "Destroy this building and sell the ressources\nRefund: $"+getDestructionGain();
                        }
                    });

					Inventory inv = server.getInventory();
					final Popup p = new Popup((getGridX() - 2) * ManagementGame.TILE_SIZE + ManagementGame.TILE_SIZE/2, (getGridY() + 1) * ManagementGame.TILE_SIZE, manager, "Pasture", breedCow, breedSheep, buttonDestroy);

					if (inv.hasAnimal(0) && !hasAnimal()) {
						breedCow.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x, float y) {
								attemptToBreed(0, server);
								p.remove();
							}
						});
					} else {
						breedCow.getColor().a = (float)0.3;
					}

					if (inv.hasAnimal(1) && !hasAnimal()) {
						breedSheep.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x, float y) {
								attemptToBreed(1, server);
								p.remove();
							}
						});
					} else {
						breedSheep.getColor().a = (float)0.3;
					}
					popupStage.addActor(p);
				}
			}
		});
		setButton(button);
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
		} else if ( growRatio < 1) {
			changeStyle(residentAnimal.getPastureMiddleSpriteName());
		} else {
			changeStyle(residentAnimal.getPastureSpriteName());
		}
	}

	@Override
	public int getConstructionCost() {
		return constructionCost;
	}

	@Override
	public int getDestructionGain() {
		return salvageBenefit;
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

	public void growPopulation(Inventory inv) {
		if (residentAnimal != null) {
			System.out.println("Population grew form Pasture in (" + super.getGridX() + "," + super.getGridY() + ")");
			if (inv.getPlants()[0].getVolume() >= (int)(1+1*growingState/2)) {
				inv.getPlants()[0].subVolume((int)(1+1*growingState/2));
				growingState += (int)1+1*growingState/2;
				if (growingState > residentAnimal.getGrowingMax()) growingState = residentAnimal.getGrowingMax();
			} else {
				growingState--;
			}

			if (growingState <= 0) {
				residentAnimal = null;
				changeStyle("pasture_empty");
				return;
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
		boolean res = server.requestBreed(getGridX(), getGridY(), i);
		updateMap(manager, server);
	}

	@Override
	public void passTurn(Inventory inv) {
		this.growPopulation(inv);
	}

	@Override
	public boolean canBuild(Inventory inv) {
		return (inv.getPlants()[3].getVolume() >= 4);
	}

	@Override
	public void doBuild(Inventory inv) {
		inv.removePlant(3, 4);
	}

	@Override
	public String toString() {
		return "Pasture";
	}
}
