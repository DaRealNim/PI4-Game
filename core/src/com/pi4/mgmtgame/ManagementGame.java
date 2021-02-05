package com.pi4.mgmtgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.pi4.mgmtgame.screens.MainMenuScreen;

public class ManagementGame extends Game {

	public SpriteBatch batch;
	public static int WIDTH = 1366;
	public static int HEIGHT = 768;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}
