package com.pi4.mgmtgame.blocks;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import com.pi4.mgmtgame.resources.Grain;
import com.pi4.mgmtgame.resources.Plant;
import com.pi4.mgmtgame.Map;
import com.pi4.mgmtgame.Popup;


public class Field extends Structure {
    private Grain plantedSeed;
    private int growingState;

    public Field(int x, int y, final AssetManager manager) {
    	super(x, y, manager);
        Button button = new Button(manager.get("blocks/Blocks.json", Skin.class), "field_wheat");
        button.setX(x*16);
        button.setY(y*16);
        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Button buttonPlant = new Button(manager.get("popupIcons/popup.json", Skin.class), "shovel_icon");
                Button buttonHarvest = new Button(manager.get("popupIcons/popup.json", Skin.class), "harvest_icon");
                Button buttonDestroy = new Button(manager.get("popupIcons/popup.json", Skin.class), "bomb_icon");
                final Popup p = new Popup(getGridX(), getGridY(), manager, buttonPlant, buttonHarvest, buttonDestroy);
                buttonPlant.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        p.remove();
                    }
                });
                buttonHarvest.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        p.remove();
                    }
                });
                buttonDestroy.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        p.remove();
                    }
                });

                getStage().addActor(p);
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
    }

    public boolean hasSeedGrown() {
        if (this.plantedSeed == null)
            return false;
        return (this.growingState >= this.plantedSeed.getGrowingTime());
    }

    public void growSeed() {
        this.growingState++;
    }

    public Plant harvest() {
        if (hasSeedGrown()) {
            return plantedSeed.getGrownPlant();
        } else {
            return null;
        }
    }


}
