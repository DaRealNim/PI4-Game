package com.pi4.mgmtgame.blocks;

import java.io.Serializable;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Group;

import com.pi4.mgmtgame.ServerInteraction;


public class Block extends Group implements Serializable {
    private Button displayedButton;
    protected AssetManager manager;
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
        addActor(this.displayedButton);
    }

    public void changeStyle(String styleName) {
        if (this.displayedButton != null) {
            getButton().setStyle(getButton().getSkin().get(styleName, Button.ButtonStyle.class));
        }
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
