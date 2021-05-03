package com.pi4.mgmtgame;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.pi4.mgmtgame.screens.MainGameScreen;

public class HoverListener extends ClickListener {
    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        MainGameScreen.mouseLabelText = "";
    }
}
