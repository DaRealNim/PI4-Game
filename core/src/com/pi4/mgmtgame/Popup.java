package com.pi4.mgmtgame;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Popup extends Group {

    public Popup(int x, int y, AssetManager manager, String popupText, Button... buttons) {
        Skin skin = manager.get("popupIcons/popup.json", Skin.class);
        Button backgroundImage = new Button(skin, "popup");
        BitmapFont font = manager.get("PixelOperator40");
        Label label = new Label(popupText, new Label.LabelStyle(font, Color.BLACK));
        Table table = new Table();
        label.setY(130);
        label.setX(15);
        table.setY(60);
        setX(x);
        setY(y);
        setScale(0.40f);
        for (Button button : buttons) {
            table.add(button).padLeft(15).padBottom(19).fillY();
        }
        Button closeButton = new Button(skin, "closeButton");
        table.add(closeButton).padLeft(15).padBottom(19);
        closeButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
            }
        });
        table.pack();
        addActor(backgroundImage);
        addActor(table);
        addActor(label);
    }
}
