package com.pi4.mgmtgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pi4.mgmtgame.ManagementGame;

public class MainMenuScreen implements Screen {

	private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
	ManagementGame game;
	
	public MainMenuScreen(ManagementGame game) {
		this.game = game;
		this.batch = game.batch;
		
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
		
		Table mainTable = new Table();
	    mainTable.setFillParent(true);
	    mainTable.top();
	    
		TextureAtlas buttonAtlas = new TextureAtlas("menuButtons/ButtonStyles.atlas");
		Skin buttonSkins = new Skin(Gdx.files.internal("menuButtons/ButtonStyles.json"), buttonAtlas);
		
		Button quitButton = new Button(buttonSkins, "quit");
		Button newGameButton = new Button(buttonSkins, "default");
		Button loadButton = new Button(buttonSkins, "load");
		
		quitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
		
		newGameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	game.setScreen(new MainGameScreen(game));
            }
        });
		
		

		 mainTable.add(newGameButton).size(300, 100).padBottom(50);
	     
		 mainTable.row();
	    
	     mainTable.add(loadButton).size(300, 100);
	    
	     mainTable.row();
	     
	     mainTable.add(quitButton).size(300, 100).padTop(50);;
	     
	     mainTable.align(Align.bottom);
	     
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
