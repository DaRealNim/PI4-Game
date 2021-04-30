package com.pi4.mgmtgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.assets.AssetManager;
import com.pi4.mgmtgame.Inventory;
import com.pi4.mgmtgame.ManagementGame;
import com.pi4.mgmtgame.Map;
import com.pi4.mgmtgame.ServerInteraction;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.Group;


public class MainMenuScreen implements Screen {

	private SpriteBatch batch;
	protected Stage stage;
	private Viewport viewport;
  private OrthographicCamera camera;
	private AssetManager manager;
	ManagementGame game;
	private int nbOfPlayers=3;

	public MainMenuScreen(ManagementGame game, AssetManager manager) {
		this.game = game;
		this.batch = game.batch;
		this.manager = manager;

	  camera = new OrthographicCamera();

	  viewport = new FitViewport(ManagementGame.WIDTH, ManagementGame.HEIGHT, camera);
	  viewport.apply();

	  camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
	  camera.update();

	  stage = new Stage(viewport, batch);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);

		final Table mainTable = new Table();
		final Table inputTable = new Table();
		mainTable.setFillParent(true);
		mainTable.top();

		final Skin buttonSkins = manager.get("menuButtons/ButtonStyles.json", Skin.class);
		final Skin uiSkins = manager.get("menuButtons/uiskin.json", Skin.class);

		final Button quitButton = new Button(buttonSkins, "quit");
		final Button newGameButton = new Button(buttonSkins, "default");
		final Button loadButton = new Button(buttonSkins, "load");

		final Label ipLabel = new Label(  "Server IP   : " , new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		final Label portLabel = new Label("Server port : " , new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		final TextField ipField = new TextField("127.0.0.1", uiSkins);
		final TextField portField = new TextField("51769", uiSkins);

		quitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

		newGameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	mainTable.clear();
            	stage.clear();
            	stage.dispose();

            	Inventory[] inv = new Inventory[nbOfPlayers];
            	for(int i = 0;i<nbOfPlayers;i++)
        			inv[i]= new Inventory(i);
            	ServerInteraction server = new ServerInteraction(ipField.getText(), portField.getText());

            	game.setScreen(new MainGameScreen(game, manager, server));
            }
        });

		inputTable.add(ipLabel);
		inputTable.add(ipField);
		inputTable.row();

		inputTable.add(portLabel);
		inputTable.add(portField);
		inputTable.row();

		mainTable.add(inputTable).padBottom(40);
		mainTable.row();

		mainTable.add(newGameButton).size(300, 100).padBottom(50);
		mainTable.row();

		mainTable.add(loadButton).size(300, 100);
		mainTable.row();

		mainTable.add(quitButton).size(300, 100).padTop(50);;

		mainTable.align(Align.center);

		stage.addActor(mainTable);
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {
		stage.dispose();
	}


}
