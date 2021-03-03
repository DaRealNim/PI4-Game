package com.pi4.mgmtgame.blocks;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.assets.AssetManager;


public class Block extends Actor {
    private Button displayedButton;
    protected AssetManager manager;
    private int x, y;

    public Block(int x, int y, AssetManager manager) {
    	this.setGridX(x);
    	this.setGridY(y);
        this.manager = manager;
    }

    public void setButton(Button b) {
        this.displayedButton = b;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (displayedButton != null)
            displayedButton.draw(batch, parentAlpha);
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

}
