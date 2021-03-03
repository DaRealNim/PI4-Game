package com.pi4.mgmtgame.blocks;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.assets.AssetManager;


public class Block extends Actor {
    private Button displayedButton;
    protected AssetManager manager;

    public Block(AssetManager manager) {
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

}
