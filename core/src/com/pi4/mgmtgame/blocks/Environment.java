package com.pi4.mgmtgame.blocks;

import com.badlogic.gdx.assets.AssetManager;

import com.pi4.mgmtgame.resources.Grain;
import com.pi4.mgmtgame.ServerInteraction;
import com.pi4.mgmtgame.Popup;
import com.pi4.mgmtgame.Map;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


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

    public void addViewController(final AssetManager manager, final ServerInteraction server, final String name) {
        this.manager = manager;
        Button button = new Button(manager.get("blocks/Blocks.json", Skin.class), name);
        button.setX(getGridX() * 16);
		button.setY(getGridY() * 16);
        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	System.out.println("Clicked block at ("+getGridX()+", "+getGridY()+")");
                Button buttonField = new Button(manager.get("popupIcons/popup.json", Skin.class), "hoe_icon");
                Button buttonTree = new Button(manager.get("popupIcons/popup.json", Skin.class), "tree_icon");
                final Field f = new Field(getGridX(), getGridY());
                final TreeField g = new TreeField(getGridX(), getGridY());
                f.setOwnerID(server.getCurrentPlayer());
                g.setOwnerID(server.getCurrentPlayer());
                final Popup p = new Popup((getGridX() - 2) * 16 + 8, (getGridY() + 1) * 16, manager,buttonField, buttonTree);
                if (server.canBuildStructure(getGridX(), getGridY(), g)) {
                    buttonTree.addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            Map map = (Map)getParent();
                            boolean res = server.requestBuildStructure(getGridX(), getGridY(), g);
                            System.out.println("Could build TreeF at "+getGridX()+", "+getGridY()+": "+res+"id :");
                            updateMap(manager, server);
                            p.remove();
                        }
                    });
                } else {
                    buttonTree.getColor().a = (float)0.3;
                }
                if (server.canBuildStructure(getGridX(), getGridY(), f)) {
                    buttonField.addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            Map map = (Map)getParent();
                            boolean res = server.requestBuildStructure(getGridX(), getGridY(), f);
                            System.out.println("Could build structure at "+getGridX()+", "+getGridY()+": "+res+"id :");
                            updateMap(manager, server);
                            p.remove();
                        }
                    });
                } else {
                    buttonField.getColor().a = (float)0.3;
                }
                getStage().addActor(p);
            }
        });
        setButton(button);
    }

    @Override
    public void passTurn() {
	}
}
