package com.pi4.mgmtgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import com.pi4.mgmtgame.screens.MainMenuScreen;

public class ManagementGame extends Game {

	public SpriteBatch batch;
	public static int WIDTH = 1366;
	public static int HEIGHT = 768;

	@Override
	public void create () {
		batch = new SpriteBatch();
		AssetManager manager = new AssetManager();
		manager.load("menuButtons/ButtonStyles.json", Skin.class, new SkinLoader.SkinParameter("menuButtons/ButtonStyles.atlas"));
		manager.load("blocks/Blocks.json", Skin.class, new SkinLoader.SkinParameter("blocks/Blocks.atlas"));
		manager.finishLoading();
		this.setScreen(new MainMenuScreen(this, manager));
	}

	@Override
	public void render () {
		super.render();
	}
}
