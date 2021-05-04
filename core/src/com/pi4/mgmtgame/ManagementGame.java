package com.pi4.mgmtgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import com.pi4.mgmtgame.screens.MainMenuScreen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.*;
import com.pi4.mgmtgame.MyAssetManager;

public class ManagementGame extends Game {

	public SpriteBatch batch;
	public static int WIDTH = 1366;
	public static int HEIGHT = 768;
	public static int TILE_SIZE = 32;

	@Override
	public void create () {
		batch = new SpriteBatch();
		MyAssetManager manager = new MyAssetManager();

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PixelOperator-Bold.ttf"));
		FreeTypeFontParameter params = new FreeTypeFontParameter();
		params.size = 40;
		manager.addAsset("PixelOperator40", BitmapFont.class, generator.generateFont(params));
		params.size = 20;
		manager.addAsset("PixelOperator20", BitmapFont.class, generator.generateFont(params));

		manager.load("menuButtons/ButtonStyles.json", Skin.class, new SkinLoader.SkinParameter("menuButtons/ButtonStyles.atlas"));
		manager.load("menuButtons/uiskin.json", Skin.class, new SkinLoader.SkinParameter("menuButtons/uiskin.atlas"));
		manager.load("blocks/Blocks.json", Skin.class, new SkinLoader.SkinParameter("blocks/Blocks.atlas"));
		manager.load("hudButtons/hudButton.json", Skin.class, new SkinLoader.SkinParameter("hudButtons/hudButton.atlas"));
		manager.load("hudButtons/hudBackground.png", Texture.class);
		manager.load("popupIcons/popup.json", Skin.class, new SkinLoader.SkinParameter("popupIcons/popup.atlas"));
		manager.load("marketRes/bgMarket.png", Texture.class);
		manager.load("marketRes/bill.png", Texture.class);
		manager.load("marketRes/carrot.png", Texture.class);
		manager.load("marketRes/potato.png", Texture.class);
		manager.load("marketRes/wheat.png", Texture.class);
		manager.load("marketRes/wood.png", Texture.class);
		manager.load("marketRes/marketButtons.json", Skin.class, new SkinLoader.SkinParameter("marketRes/marketButtons.atlas"));
		manager.load("b l a c k.png", Texture.class);
		manager.load("square.png", Texture.class);
		manager.load("select.png", Texture.class);
		manager.load("blocks/arbre_haut.png", Texture.class);
		manager.finishLoading();

		this.setScreen(new MainMenuScreen(this, manager));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose() {
		System.exit(0);
	}
}
