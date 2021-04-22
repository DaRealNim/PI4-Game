package com.pi4.mgmtgame.blocks;

import java.io.Serializable;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Group;

import com.pi4.mgmtgame.ServerInteraction;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pi4.mgmtgame.Map;


public class Block extends Group implements Serializable {
    private transient Button displayedButton;
    protected transient AssetManager manager;
    protected String spriteName;
    private int x, y;

    public Block(int x, int y) {
    	this.setGridX(x);
    	this.setGridY(y);
    }

    public void addViewController(final AssetManager manager, final ServerInteraction server) {
        System.out.println("nnooooOOOOOOO");
    }

    public void setButton(Button b) {
        clear();
        addActor(b);
        this.displayedButton = b;
    }

    public Button getButton() {
        return this.displayedButton;
    }

    public void updateActors() {
        clear();
        changeStyle(this.spriteName);
        addActor(this.displayedButton);
    }

    public void changeStyle(String styleName) {
        this.spriteName = styleName;
        if (this.displayedButton != null) {
            getButton().setStyle(getButton().getSkin().get(styleName, Button.ButtonStyle.class));
        }
    }

    protected void updateMap(AssetManager manager, ServerInteraction server) {
        Map map = (Map) getParent();
        Map serverMap = server.getMap();
        serverMap.updateActors(manager, server);
        Stage stage = getStage();
        map.remove();
        stage.addActor(serverMap);
    }

	public int getGridX() {
		return x;
	}

	public void setGridX(int x) {
		this.x = x;
	}

	public int getGridY() {
		return y;
	}

	public void setGridY(int y) {
		this.y = y;
	}

	public void passTurn() {
	}

    public void setSpriteName(String name) {
        this.spriteName = name;
    }

    public String getSpriteName() {
        return this.spriteName;
    }

}
