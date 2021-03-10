package com.pi4.mgmtgame.blocks;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Group;


public class Block extends Group {
    private Button displayedButton;
    protected AssetManager manager;
    private int x, y;

    public Block(int x, int y, AssetManager manager) {
    	this.setGridX(x);
    	this.setGridY(y);
      this.manager = manager;
    }

    public void setButton(Button b) {
        clearChildren();
        addActor(b);
        this.displayedButton = b;
    }

    //Commented for now because just adding blocks as group children work, but may need
    //later in case something goes horribly wrong
    //
    // @Override
    // public void draw(Batch batch, float parentAlpha) {
    //     if (displayedButton != null)
    //         displayedButton.draw(batch, parentAlpha);
    // }

    // @Override
    // public void act(float delta) {
    //     if (displayedButton != null)
    //         displayedButton.act(delta);
    // }

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
}
