package com.pi4.mgmtgame.screens;

import com.pi4.mgmtgame.ManagementGame;
import com.pi4.mgmtgame.Map;
import com.pi4.mgmtgame.Market;
import com.pi4.mgmtgame.ServerInteraction;
import com.pi4.mgmtgame.HUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.math.Vector3;
import com.pi4.mgmtgame.Popup;


public class MainGameScreen implements Screen	{

	public static final float SPEED = 140;

	float imgPosX = 0;
	float imgPosY = 0;

	Texture img;
	ManagementGame game;
	AssetManager manager;
	Map map;
	private Viewport viewport;
	private final OrthographicCamera camera;
	private SpriteBatch batch;
	private HUD hud;
	private InputMultiplexer multiplexer;
	protected Stage stage, stage2;
	private ServerInteraction server;
	private Button waitingOverlay;
	private Image selectSquare;
	private Image selectedSquare;
	private boolean selected;
	private int selectedSquareX;
	private int selectedSquareY;

	public MainGameScreen (ManagementGame game, AssetManager manager, ServerInteraction server) {
		this.map = server.getMap();
		this.game = game;
		this.manager = manager;
		this.batch = game.batch;
		this.server = server;

		this.selected = false;
		this.selectedSquareX = -1;
		this.selectedSquareY = -1;

		camera = new OrthographicCamera(ManagementGame.WIDTH, ManagementGame.HEIGHT);

		this.multiplexer = new InputMultiplexer() {
			@Override
			public boolean scrolled(float amountX, float amountY) {
				if (amountY > 0) {
					camera.zoom = Math.min(2, camera.zoom+.2f);
				}
				if (amountY < 0) {
					camera.zoom = Math.max(0.1f, camera.zoom-.2f);
				}
				return true;
			}
		};

		viewport = new FitViewport(ManagementGame.WIDTH / 4, ManagementGame.HEIGHT / 4, camera);
		viewport.apply();

		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();

		stage = new Stage(viewport, batch);
		stage2 = new Stage(viewport, batch);

		hud = new HUD(manager, server);
		server.passHUD(hud);
	}

	@Override
	public void show() {
		multiplexer.addProcessor(hud.stage);
		multiplexer.addProcessor(stage);

		Gdx.input.setInputProcessor(multiplexer);
		stage.addActor(map);
		map.updateActors(manager, server);

		this.selectSquare = new Image(manager.get("select.png", Texture.class));
		this.selectedSquare = new Image(manager.get("select.png", Texture.class));
		this.selectedSquare.setColor(0f, 1f, 1f, 1f);
		this.selectedSquare.setVisible(false);
		stage2.addActor(selectSquare);
		stage2.addActor(selectedSquare);

		waitingOverlay = new Button(new TextureRegionDrawable(manager.get("b l a c k.png", Texture.class)));
		waitingOverlay.setVisible(false);
		hud.stage.addActor(waitingOverlay);



		Thread t = new Thread(new MapHudUpdate());
		t.start();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();

		Vector3 vec = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		stage.getCamera().unproject(vec);
		if(vec.x / 16 < map.getMapWidth() && vec.x > 0 && vec.y / 16 < map.getMapHeight() && vec.y > 0) {
			this.selectSquare.setVisible(true);
			this.selectSquare.setX((int)(vec.x / 16)*16);
			this.selectSquare.setY((int)(vec.y / 16)*16);
		} else {
			this.selectSquare.setVisible(false);
		}

		if (selected) {
			this.selectSquare.setVisible(false);
			boolean foundPopup = false;
			for(Actor a : stage.getActors()) {
				if (a instanceof Popup) {
					foundPopup = true;
					break;
				}
			}
			if (!foundPopup) {
				selected = false;
				selectedSquare.setVisible(false);
				selectSquare.setVisible(true);
			}
		}

		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            this.selected = true;
			this.selectedSquareX = (int)(vec.x / 16)*16;
			this.selectedSquareY = (int)(vec.y / 16)*16;
			this.selectedSquare.setVisible(true);
			this.selectedSquare.setX(this.selectedSquareX);
			this.selectedSquare.setY(this.selectedSquareY);
		}

		stage2.act(delta);
		stage2.draw();

		processCameraMovement();

		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.act();
		hud.stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();
	}


	//Will have to add more conditions to this, plz no touchy!
	private void processCameraMovement() {
		int translateX = 0;
		int translateY = 0;
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			translateX -= 2;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			translateX += 2;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			translateY -= 2;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			translateY += 2;
		}
		camera.translate(translateX, translateY);
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {}

	private class MapHudUpdate implements Runnable {

		@Override
		public void run() {
			while(true) {
				try {
					Thread.sleep(500);
				} catch(InterruptedException e) {
				}
				int t = server.getStoredInternalTurn();
				// System.out.println(server.getID() + ", " + t);
				if (server.getID() == t) {
					waitingOverlay.setVisible(false);
					hud.update();
				} else {
					waitingOverlay.setVisible(true);
					Map serverMap = server.getMap();
					serverMap.updateActors(manager, server);
					map.remove();
					map = serverMap;
					stage.addActor(serverMap);
					hud.update();
					server.getInternalTurn();
				}

			}
		}
	}

}
