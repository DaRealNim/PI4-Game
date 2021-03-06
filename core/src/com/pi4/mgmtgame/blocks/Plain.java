package com.pi4.mgmtgame.blocks;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.pi4.mgmtgame.resources.Grain;
import com.pi4.mgmtgame.Map;
import com.pi4.mgmtgame.Popup;
import com.pi4.mgmtgame.blocks.Field;


public class Plain extends Environment {

    public Plain(int x, int y, final AssetManager manager) {
    	super(x, y, manager);
        Button button = new Button(manager.get("blocks/Blocks.json", Skin.class), "plain");
        button.setX(x*16);
        button.setY(y*16);
        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	System.out.println("Clicked block at ("+getGridX()+", "+getGridY()+")");
                Button buttonField = new Button(manager.get("popupIcons/popup.json", Skin.class), "hoe_icon");
                final Popup p = new Popup((getGridX() - 2) * 16 + 8, (getGridY() + 1) * 16, manager, buttonField);
                buttonField.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Map map = (Map)getParent();
                        Field f = new Field(getGridX(), getGridY(), manager);
                        map.setStructAt(getGridX(), getGridY(), f);
                        map.addActor(f);
                        p.remove();
                    }
                });
                getStage().addActor(p);
            }
        });
        setButton(button);
    }

    @Override
    public int getGrowingPenalty() {
        return 0;
    }

    @Override
    public boolean canSeedGrow(Grain seed) {
        return true;
    }

    @Override
    public boolean canBuild(Structure struct) {
        return true;
    }
}
