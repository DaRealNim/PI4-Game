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

public abstract class Environment extends Block {

    public Environment(int x, int y) {
        super(x, y);
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

                if(getOwnerID() == -1) {
                    Button buttonBuyTerrain = new Button(manager.get("popupIcons/popup.json", Skin.class), "dollar_icon");
                    final Popup p = new Popup((getGridX() - 2) * ManagementGame.TILE_SIZE + ManagementGame.TILE_SIZE/2, (getGridY() + 1) * ManagementGame.TILE_SIZE, manager, buttonBuyTerrain);
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

                    Button buttonField = new Button(manager.get("popupIcons/popup.json", Skin.class), "hoe_icon");
                    Button buttonTree = new Button(manager.get("popupIcons/popup.json", Skin.class), "tree_icon");
                    Button buttonSprink = new Button(manager.get("popupIcons/popup.json", Skin.class), "sprinkler_icon");
                    final Field f = new Field(getGridX(), getGridY());
                    final TreeField g = new TreeField(getGridX(), getGridY());
                    final Sprinkler h = new Sprinkler(getGridX(), getGridY());
                    f.setOwnerID(server.getCurrentPlayer());
                    g.setOwnerID(server.getCurrentPlayer());
                    h.setOwnerID(server.getCurrentPlayer());

                    final Popup p = new Popup((getGridX() - 2) * ManagementGame.TILE_SIZE + ManagementGame.TILE_SIZE/2, (getGridY() + 1) * ManagementGame.TILE_SIZE, manager,buttonField, buttonTree,buttonSprink);

                    if (server.canBuildStructure(getGridX(), getGridY(), g)) {
                        buttonTree.addListener(new ClickListener(){
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                boolean res = server.requestBuildStructure(getGridX(), getGridY(), g);
                                System.out.println("Could build TreeF at "+getGridX()+", "+getGridY()+": "+res+"id :");
                                updateMap(manager, server);
                                p.remove();
                            }
                        });
                    } else {
                        buttonTree.getColor().a = (float)0.3;
                    }

                    if (server.canBuildStructure(getGridX(), getGridY(), h)) {
                    	buttonSprink.addListener(new ClickListener(){
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                boolean res = server.requestBuildStructure(getGridX(), getGridY(), h);
                                System.out.println("Could build Sprink at "+getGridX()+", "+getGridY()+": "+res+"id :");
                                updateMap(manager, server);
                                p.remove();
                            }
                        });

                    }else {
                        buttonSprink.getColor().a = (float)0.3;
                    }

                    if (server.canBuildStructure(getGridX(), getGridY(), f)) {
                        buttonField.addListener(new ClickListener(){
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                boolean res = server.requestBuildStructure(getGridX(), getGridY(), f);
                                System.out.println("Could build structure at "+getGridX()+", "+getGridY()+": "+res+"id :");
                                updateMap(manager, server);
                                p.remove();
                            }
                        });
                    } else {
                        buttonField.getColor().a = (float)0.3;
                    }

                    popupStage.addActor(p);
                }
            }
        });
        setButton(button);
    }

    @Override
    public void passTurn() {
	}
}
