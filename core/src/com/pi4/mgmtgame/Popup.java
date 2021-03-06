package com.pi4.mgmtgame;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;

public class Popup extends Group {

    public Popup(int x, int y, AssetManager manager, Button... buttons) {
        Skin skin = manager.get("popupIcons/popup.json", Skin.class);
        Button backgroundImage = new Button(skin, "popup");
        Table table = new Table();
        setX((x-2)*16 + 8);
        setY((y+1)*16);
        for (Button button : buttons) {
            table.add(button).padLeft(2).padBottom(19).fillY();
        }
        table.top();
        table.left();
        table.pack();
        addActor(backgroundImage);
        addActor(table);
    }

    public void close() {
        remove();
    }
}
