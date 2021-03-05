package com.pi4.mgmtgame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.Gdx;




public class HUD {
    public Stage stage;
    private Viewport viewport;
    private Button passTurnButton;
    private AssetManager manager;


    public HUD (AssetManager man)
    {
      this.manager = man;
      viewport = new FitViewport(ManagementGame.WIDTH, ManagementGame.HEIGHT, new OrthographicCamera());
      stage = new Stage(viewport);
      this.show();
    }

    public void show() {
      Gdx.input.setInputProcessor(stage);

      Skin buttonSkins = manager.get("hudButtons/hudButton.json", Skin.class);

      passTurnButton = new Button(buttonSkins, "passTurn");

      passTurnButton.addListener(new ClickListener(){
              @Override
              public void clicked(InputEvent event, float x, float y) {
                  System.out.println("Passed a turn!");
              }
          });

      Table table = new Table();

      table.top();
      table.setFillParent(true);

      table.add(passTurnButton).padTop(10).expandX();

      stage.addActor(table);
    }
    public void update()
    {
      //update the values when we have values to update, probably get Inventory in the constructor somewhere
    }

    public void dispose()
    {
      stage.dispose();
    }
}
