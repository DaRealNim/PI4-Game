package com.pi4.mgmtgame.blocks;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import com.pi4.mgmtgame.resources.Grain;
import com.pi4.mgmtgame.resources.Plant;
import com.pi4.mgmtgame.Map;


public class Field extends Structure {
    private Grain plantedSeed;
    private int growingState;

    public Field(int x, int y, AssetManager manager) {
    	super(x, y, manager);
        Button button = new Button(manager.get("blocks/Blocks.json", Skin.class), "field_wheat");
        button.setX(x*16);
        button.setY(y*16);
        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	System.out.println("Clicked field at ("+getGridX()+", "+getGridY()+")");
                Map map = (Map)getParent();
                map.setStructAt(getGridX(), getGridY(), null);
                clear();
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
