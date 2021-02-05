package com.pi4.mgmtgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.pi4.mgmtgame.ManagementGame;

public class MainMenuScreen implements Screen {

	private static final int BUTTON_WIDTH = 600 / 3;
	private static final int BUTTON_HEIGHT = 200 / 3;
	private static final int BUTTON_XPOS = Gdx.graphics.getWidth() / 2 - BUTTON_WIDTH / 2;

	ManagementGame game;
	
	Texture quitButtonActive;
	Texture quitButtonInactive;
	Texture newGameButtonActive;
	Texture newGameButtonInactive;
	Texture loadButtonActive;
	Texture loadButtonInactive;
	
	
	public MainMenuScreen(ManagementGame game) {
		this.game = game;
		
		quitButtonActive = new Texture("coloredButtons/Quit_col_Button.png");
		quitButtonInactive = new Texture("buttons/Quit_Button.png");
		newGameButtonActive = new Texture("coloredButtons/New_Game_col_Button.png");
		newGameButtonInactive = new Texture("buttons/New_Game_Button.png");
		loadButtonActive = new Texture("coloredButtons/Load_col_Button.png");
		loadButtonInactive = new Texture("buttons/Load_Button.png");
	}
	
	@Override
	public void show() {

		
	}

	@Override
	public void render(float delta) {
	
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();
		
		if (Gdx.input.getX() < BUTTON_XPOS + BUTTON_WIDTH &&
			Gdx.input.getX() > BUTTON_XPOS && 
			Gdx.graphics.getHeight() - Gdx.input.getY() < 50 + BUTTON_HEIGHT && 
			Gdx.graphics.getHeight() - Gdx.input.getY() > 0) 
		{
			game.batch.draw(quitButtonActive, BUTTON_XPOS, 0, BUTTON_WIDTH, BUTTON_HEIGHT);
		}
		else {
			game.batch.draw(quitButtonInactive, BUTTON_XPOS, 0, BUTTON_WIDTH, BUTTON_HEIGHT);
		}
		
		/*
		if (Gdx.input.getX() < 100)
			game.batch.draw(newGameButtonActive, BUTTON_XPOS, 200, BUTTON_WIDTH, BUTTON_HEIGHT);
		else {
			game.batch.draw(newGameButtonInactive, BUTTON_XPOS, 200, BUTTON_WIDTH, BUTTON_HEIGHT);
		}
			
		if (Gdx.input.getX() < 100)
			game.batch.draw(loadButtonActive, BUTTON_XPOS, 100, BUTTON_WIDTH, BUTTON_HEIGHT);
		else {
			game.batch.draw(loadButtonInactive, BUTTON_XPOS, 100, BUTTON_WIDTH, BUTTON_HEIGHT);
		}*/
		
		game.batch.end();
	}

	@Override
	public void resize(int width, int height) {
	
		
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {}
	

}
