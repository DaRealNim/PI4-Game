package com.pi4.mgmtgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.assets.AssetManager;

import com.pi4.mgmtgame.ManagementGame;
import com.pi4.mgmtgame.Map;


public class MainGameScreen implements Screen	{

	public static final float SPEED = 140;

	float imgPosX = 0;
	float imgPosY = 0;

	Texture img;
	ManagementGame game;
	AssetManager manager;
	Map map;
    private Viewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    protected Stage stage;

	public MainGameScreen (ManagementGame game, AssetManager manager, Map map) {
		this.map = map;
		this.game = game;
		this.manager = manager;
		this.batch = game.batch;

		camera = new OrthographicCamera(ManagementGame.WIDTH, ManagementGame.HEIGHT);

		viewport = new FitViewport(ManagementGame.WIDTH / 4, ManagementGame.HEIGHT / 4, camera);
		viewport.apply();

		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();

		stage = new Stage(viewport, batch);
	}
	@Override
	public void show() {
		img = new Texture("badlogic.jpg");
		Gdx.input.setInputProcessor(stage);
		stage.addActor(map);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();
		processCameraMovement();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();
	}


	//Will have to add more conditions to this, plz no touchy!
	private void processCameraMovement()
	{
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			camera.translate(-2, 0);
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			camera.translate(2, 0);
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			camera.translate(0, -2);
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			camera.translate(0, 2);
		}
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
