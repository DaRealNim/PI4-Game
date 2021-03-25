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

public class Field extends Structure {
	private Grain plantedSeed;
	private int growingState;

	public Field(int x, int y, final AssetManager manager, final ServerInteraction server) {

		super(x, y, manager);
		Button button = new Button(manager.get("blocks/Blocks.json", Skin.class), "field_empty");
		button.setX(x * 16);
		button.setY(y * 16);
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
                System.out.println("Clicked Field!");
				if (testOwner(server.getInternalTurn())) {
					Button buttonPlant = new Button(manager.get("popupIcons/popup.json", Skin.class), "shovel_icon");
					Button buttonHarvest = new Button(manager.get("popupIcons/popup.json", Skin.class), "harvest_icon");
					Button buttonDestroy = new Button(manager.get("popupIcons/popup.json", Skin.class), "bomb_icon");
					final Popup p = new Popup((getGridX() - 2) * 16 + 8, (getGridY() + 1) * 16, manager, buttonPlant,
							buttonHarvest, buttonDestroy);
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
								if (server.getInventory().hasGrain(0))
									buttons[0] = plantWheat;

								if (server.getInventory().hasGrain(1))
									buttons[1] = plantPotato;

								if (server.getInventory().hasGrain(2))
									buttons[2] = plantCarrot;

								final Popup d = new Popup((getGridX() - 2) * 16 - 12, (getGridY() + 1) * 16 + 12, manager,buttons);
								getStage().addActor(d);

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
								changeStyle("field_empty");
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
							remove();
							p.remove();
						}
					});

					getStage().addActor(p);
				}
			}
		});

		setButton(button);
		this.growingState = 0;
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
        changeStyle(seed.getFieldSpriteName());
	}

	public boolean hasSeedGrown() {
		if (this.plantedSeed == null)
			return false;
		return (this.growingState >= this.plantedSeed.getGrowingTime());
	}

    public boolean hasSeed() {
        return (this.plantedSeed != null);
    }

	public void growSeed() {
		if (plantedSeed != null && !hasSeedGrown()) {
			System.out.println("Grew field for block (" + super.getGridX() + "," + super.getGridY() + ")");
			this.growingState++;
			if (hasSeedGrown()) {
				changeStyle(plantedSeed.getFieldSpriteName() + "_grew");
			}
		}
	}

	private void attemptToPlant(int i, ServerInteraction server) {
		Map map = (Map) getParent();
		// ERREUR (renvoie tjrs 0)
		boolean res = server.requestPlantSeed(getGridX(), getGridY(), server.getInventory().getSeeds()[i]);
		System.out.println(server.getInventory().getSeeds()[i].getId());
		// Todo Fix cette merde
		System.out.println("Could plant seed of id " + i + " at " + getGridX() + ", " + getGridY() + ": " + res);
		Map serverMap = server.getMap();
		serverMap.updateActors();
		Stage stage = getStage();
		map.remove();
		stage.addActor(serverMap);
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
	}

}
