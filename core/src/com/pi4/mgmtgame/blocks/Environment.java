package com.pi4.mgmtgame.blocks;

import com.badlogic.gdx.assets.AssetManager;

import com.pi4.mgmtgame.resources.Grain;
import com.pi4.mgmtgame.ServerInteraction;
import com.pi4.mgmtgame.Popup;
import com.pi4.mgmtgame.Map;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pi4.mgmtgame.ManagementGame;
import com.pi4.mgmtgame.screens.MainGameScreen;
import com.pi4.mgmtgame.HoverListener;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class Environment extends Block {

    private int terrainPrice;
    private String displayName;

    public Environment(int x, int y, String displayName) {
        super(x, y);
        this.displayName = displayName;
        terrainPrice = 500;
    }

    //Returns a growing penalty for fields in months
    abstract public int getGrowingPenalty();

    //Checks if a specific grain can be planted in fields on that terrain
    abstract public boolean canSeedGrow(Grain seed);

    //Checks if a specific building can be placed on that terrain
    abstract public boolean canBuild(Structure struct);

    @Override
    public void addViewController(final AssetManager manager, final ServerInteraction server, final Stage popupStage) {
        super.addViewController(manager, server, popupStage);
        this.manager = manager;
        Button button = new Button(manager.get("blocks/Blocks.json", Skin.class), getSpriteName());
        button.setX(getGridX() * ManagementGame.TILE_SIZE);
		button.setY(getGridY() * ManagementGame.TILE_SIZE);
        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	System.out.println("Clicked block at ("+getGridX()+", "+getGridY()+")");

                final Skin popupSkin = manager.get("popupIcons/popup.json", Skin.class);

                if(getOwnerID() == -1) {
                    Button buttonBuyTerrain = new Button(popupSkin, "dollar_icon");
                    buttonBuyTerrain.addListener(new HoverListener() {
                        @Override
                        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                            MainGameScreen.mouseLabelText = "Buy this terrain\nCost: $500";
                        }
                    });
                    final Popup p = new Popup((getGridX() - 2) * ManagementGame.TILE_SIZE + ManagementGame.TILE_SIZE/2, (getGridY() + 1) * ManagementGame.TILE_SIZE, manager, displayName, buttonBuyTerrain);
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
                } else {
                    Button buttonField = new Button(popupSkin, "hoe_icon");
                    Button buttonPasture = new Button(popupSkin, "pasture_icon");
                    Button buttonTree = new Button(popupSkin, "tree_icon");
                    Button buttonSprink = new Button(popupSkin, "sprinkler_icon");
                    final Field field = new Field(getGridX(), getGridY());
                    final TreeField tree = new TreeField(getGridX(), getGridY());
                    final Sprinkler sprinkler = new Sprinkler(getGridX(), getGridY());
                    final Pasture pasture = new Pasture(getGridX(), getGridY());
                    int id = server.getCurrentPlayer();
                    field.setOwnerID(id);
                    tree.setOwnerID(id);
                    sprinkler.setOwnerID(id);
                    pasture.setOwnerID(id);

                    buttonField.addListener(new HoverListener() {
                        @Override
                        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                            MainGameScreen.mouseLabelText = "Build a field\nThe field allows you to grow multiple types of seeds.\nCost: $"+field.getConstructionCost();
                        }
                    });
                    buttonPasture.addListener(new HoverListener() {
                        @Override
                        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                            MainGameScreen.mouseLabelText = "Build a pasture\nBreed sheep and cows to get meat, wool and leather.\nCost: $"+pasture.getConstructionCost();
                        }
                    });
                    buttonTree.addListener(new HoverListener() {
                        @Override
                        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                            MainGameScreen.mouseLabelText = "Plant a tree\nCost: 1 tree seed";
                        }
                    });
                    buttonSprink.addListener(new HoverListener() {
                        @Override
                        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                            MainGameScreen.mouseLabelText = "Build a sprinkler\nThe sprinkler gives a speed boost to all surrounding fields.\nCost: $"+sprinkler.getConstructionCost();
                        }
                    });

                    final Popup p = new Popup((getGridX() - 2) * ManagementGame.TILE_SIZE + ManagementGame.TILE_SIZE/2, (getGridY() + 1) * ManagementGame.TILE_SIZE, manager, displayName, buttonField, buttonPasture, buttonTree, buttonSprink);

                    if (server.canBuildStructure(getGridX(), getGridY(), tree)) {
                        buttonTree.addListener(new ClickListener(){
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                boolean res = server.requestBuildStructure(getGridX(), getGridY(), tree);
                                updateMap(manager, server);
                                p.remove();
                            }
                        });
                    } else {
                        buttonTree.getColor().a = (float)0.3;
                    }

                    if (server.canBuildStructure(getGridX(), getGridY(), sprinkler)) {
                    	buttonSprink.addListener(new ClickListener(){
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                boolean res = server.requestBuildStructure(getGridX(), getGridY(), sprinkler);
                                updateMap(manager, server);
                                p.remove();
                            }
                        });

                    }else {
                        buttonSprink.getColor().a = (float)0.3;
                    }

                    if (server.canBuildStructure(getGridX(), getGridY(), field)) {
                        buttonField.addListener(new ClickListener(){
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                boolean res = server.requestBuildStructure(getGridX(), getGridY(), field);
                                updateMap(manager, server);
                                p.remove();
                            }
                        });
                    } else {
                        buttonField.getColor().a = (float)0.3;
                    }

                    if (server.canBuildStructure(getGridX(), getGridY(), pasture)) {
                        buttonPasture.addListener(new ClickListener(){
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                boolean res = server.requestBuildStructure(getGridX(), getGridY(), pasture);
                                updateMap(manager, server);
                                p.remove();
                            }
                        });
                    } else {
                        buttonPasture.getColor().a = (float)0.3;
                    }

                    popupStage.addActor(p);
                }
            }
        });
        setButton(button);
    }

  public int getPrice() {
    return (terrainPrice);
  }

  @Override
  public String toString() {
      return displayName;
  }
}
