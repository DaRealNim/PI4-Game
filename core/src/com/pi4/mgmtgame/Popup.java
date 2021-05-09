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
        Label label = new Label(popupText, new Label.LabelStyle(font, Color.WHITE));
        label.setY(155);
        label.setX(45);
        Table table = new Table();
        table.setY(95);
        table.setX(40);

        backgroundImage.setTransform(true);
        backgroundImage.setScale(0.77f);

        setX(x);
        setY(y);
        setScale(0.40f);
        int c = 0;
        for (Button button : buttons) {
            c++;
            table.add(button).padLeft(10).padBottom(10).fillY();
            if (c == 4) {
                table.row();
                table.setY(table.getY()-60);
                c = 0;
            }
        }
        Button closeButton = new Button(skin, "closeButton");
        table.add(closeButton).padLeft(10).padBottom(10);
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
