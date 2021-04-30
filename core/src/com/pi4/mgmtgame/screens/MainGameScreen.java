package com.pi4.mgmtgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pi4.mgmtgame.HUD;
import com.pi4.mgmtgame.ManagementGame;
import com.pi4.mgmtgame.ManagementGame;
import com.pi4.mgmtgame.Map;
import com.pi4.mgmtgame.Market;
import com.pi4.mgmtgame.Popup;
import com.pi4.mgmtgame.ServerInteraction;
import com.pi4.mgmtgame.blocks.Structure;
import com.pi4.mgmtgame.blocks.Environment;
import com.pi4.mgmtgame.blocks.TreeField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

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
	protected Stage stage, selectionSquareStage, decorationStage, ownerStage, staticStage;
	private ServerInteraction server;
	private Button waitingOverlay;
	private Image selectSquare;
	private Image selectedSquare;
	private boolean selected;
	private int selectedSquareX;
	private int selectedSquareY;
	private double cameraSpeed;
	private Image ownerScreenBackground;
	private Group ownerColoredSquares;

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
		camera.zoom = 2;
		this.cameraSpeed = 4;

		this.multiplexer = new InputMultiplexer() {
			@Override
			public boolean scrolled(float amountX, float amountY) {
				if (amountY > 0) {
					camera.zoom = Math.min(10, camera.zoom+.2f);
				}
				if (amountY < 0) {
					camera.zoom = Math.max(0.2f, camera.zoom-.2f);
				}
				cameraSpeed = 4*camera.zoom;
				return true;
			}
		};

		viewport = new FitViewport(ManagementGame.WIDTH / 4, ManagementGame.HEIGHT / 4, camera);
		viewport.apply();

		camera.position.set((map.getMapWidth()/2)*ManagementGame.TILE_SIZE, (map.getMapHeight()/2)*ManagementGame.TILE_SIZE, 0);
		camera.update();

		stage = new Stage(viewport, batch);
		selectionSquareStage = new Stage(viewport, batch);
		decorationStage = new Stage(viewport, batch);
		staticStage = new Stage(new FitViewport(ManagementGame.WIDTH / 4, ManagementGame.HEIGHT / 4, new OrthographicCamera()), batch);
		ownerStage = new Stage(viewport, batch);
		ownerScreenBackground = new Image(manager.get("b l a c k.png", Texture.class));
		ownerColoredSquares = new Group();


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
		updateOverlay();

		this.selectSquare = new Image(manager.get("select.png", Texture.class));
		this.selectedSquare = new Image(manager.get("select.png", Texture.class));
		this.selectedSquare.setColor(0f, 1f, 1f, 1f);
		this.selectedSquare.setVisible(false);
		selectionSquareStage.addActor(selectSquare);
		selectionSquareStage.addActor(selectedSquare);

		waitingOverlay = new Button(new TextureRegionDrawable(manager.get("b l a c k.png", Texture.class)));
		waitingOverlay.setVisible(false);
		hud.stage.addActor(waitingOverlay);

		// staticStage.addActor(ownerScreenBackground);
		ownerStage.addActor(ownerColoredSquares);


		Thread t = new Thread(new MapHudUpdate());
		t.start();
	}

	@Override
	public synchronized void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();

		updateSelection();

		selectionSquareStage.act(delta);
		selectionSquareStage.draw();

		decorationStage.act(delta);
		decorationStage.draw();

		staticStage.draw();

		ownerStage.act(delta);
		ownerStage.draw();

		processCameraMovement();

		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.act();
		hud.stage.draw();

		ownerScreenBackground.setVisible(hud.shouldShowOwners());
		ownerColoredSquares.setVisible(hud.shouldShowOwners());
	}

	private void updateSelection() {
		Vector3 vec = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		stage.getCamera().unproject(vec);
		if(vec.x / ManagementGame.TILE_SIZE < map.getMapWidth() && vec.x > 0 && vec.y / ManagementGame.TILE_SIZE < map.getMapHeight() && vec.y > 0) {
			this.selectSquare.setVisible(true);
			this.selectSquare.setX((int)(vec.x / ManagementGame.TILE_SIZE)*ManagementGame.TILE_SIZE);
			this.selectSquare.setY((int)(vec.y / ManagementGame.TILE_SIZE)*ManagementGame.TILE_SIZE);
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
			this.selectedSquareX = (int)(vec.x / ManagementGame.TILE_SIZE)*ManagementGame.TILE_SIZE;
			this.selectedSquareY = (int)(vec.y / ManagementGame.TILE_SIZE)*ManagementGame.TILE_SIZE;
			this.selectedSquare.setVisible(true);
			this.selectedSquare.setX(this.selectedSquareX);
			this.selectedSquare.setY(this.selectedSquareY);
		}
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();
	}

	private void processCameraMovement() {
		int translateX = 0;
		int translateY = 0;
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			translateX -= cameraSpeed;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			translateX += cameraSpeed;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			translateY -= cameraSpeed;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			translateY += cameraSpeed;
		}
		camera.translate(translateX, translateY);
	}

	private synchronized void updateOverlay() {
		decorationStage.clear();
		ownerColoredSquares.clear();
		for(int x=0; x<map.getMapWidth(); x++) {
			for(int y=0; y<map.getMapHeight(); y++) {
				Structure struct = map.getStructAt(x, y);
				Environment env = map.getEnvironmentAt(x, y);
				if (struct != null && struct instanceof TreeField && ((TreeField)struct).hasSeedGrown()) {
					Image treeTop = new Image(manager.get("blocks/arbre_haut.png", Texture.class));
					treeTop.setX(x*ManagementGame.TILE_SIZE);
					treeTop.setY((y+1)*ManagementGame.TILE_SIZE);
					decorationStage.addActor(treeTop);
				}
				if (env != null && !env.testOwner(-1)) {
					Image square = new Image(manager.get("square.png", Texture.class));
					square.setColor(1.0f, 0.0f, 0.0f, 1.0f);
					square.setX(x*ManagementGame.TILE_SIZE);
					square.setY(y*ManagementGame.TILE_SIZE);
					ownerColoredSquares.addActor(square);
				}
			}
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

	private Map getMapFromStage() {
		for(Actor a : stage.getActors()) {
			if (a instanceof Map)
				return (Map)a;
		}
		System.out.println("No map found");
		return null;
	}

	private class MapHudUpdate implements Runnable {

		@Override
		public synchronized void run() {
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
					map = getMapFromStage();
					updateOverlay();
				} else {
					waitingOverlay.setVisible(true);
					Map serverMap = server.getMap();
					serverMap.updateActors(manager, server);
					stage.addActor(serverMap);
					map.remove();
					map = serverMap;
					updateOverlay();
					hud.update();
					server.getInternalTurn();
				}

			}
		}
	}

}
