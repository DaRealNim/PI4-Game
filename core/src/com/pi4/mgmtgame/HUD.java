package com.pi4.mgmtgame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pi4.mgmtgame.resources.Grain;
import com.pi4.mgmtgame.resources.Plant;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
    private ServerInteraction server;
    private Label moneyLabel, grainLabel, seedLabel;
    private Inventory inv;
    String seedLabelText;
    String grainLabelText;


    public HUD (AssetManager man, ServerInteraction server) {
      this.manager = man;
      this.server = server;
      this.inv = server.getInventory();

      viewport = new FitViewport(ManagementGame.WIDTH, ManagementGame.HEIGHT, new OrthographicCamera());
      stage = new Stage(viewport);

      this.show();
    }

    public void updateLabels() {
        seedLabelText = "";
        grainLabelText = "";

        for (Grain seed : inv.getSeeds())
        {
      	  if (seed != null)
      	  seedLabelText += seed.toString() + ": " + seed.getVolume() + "  \n";
        }

        for (Plant plant : inv.getPlants())
        {
      	  if (plant != null)
      	  grainLabelText += plant.toString() + ": " + plant.getVolume() + "  \n";
        }

        moneyLabel.setText("DOLLA BILLZ: " + inv.getMoney());
        seedLabel.setText(seedLabelText);
        grainLabel.setText(grainLabelText);
    }

    public void show() {
      Skin buttonSkins = manager.get("hudButtons/hudButton.json", Skin.class);
      Texture backgroundTexture = manager.get("hudButtons/hudBackground.png", Texture.class);
      Image background = new Image(backgroundTexture);

      passTurnButton = new Button(buttonSkins, "passTurn");
      passTurnButton.setZIndex(1);

      moneyLabel = new Label("DOLLA BILLZ: " + inv.getMoney(),  new Label.LabelStyle(new BitmapFont(), Color.WHITE));

      seedLabelText = "";
      grainLabelText = "";

      for (Grain seed : inv.getSeeds())
      {
    	  if (seed != null)
    	  seedLabelText += seed.toString() + ": " + seed.getVolume() + "  \n";
      }

      for (Plant plant : inv.getPlants())
      {
    	  if (plant != null)
    	  grainLabelText += plant.toString() + ": " + plant.getVolume() + "  \n";
      }

      grainLabel = new Label(grainLabelText ,  new Label.LabelStyle(new BitmapFont(), Color.WHITE));
      seedLabel = new Label(seedLabelText ,  new Label.LabelStyle(new BitmapFont(), Color.WHITE));

      passTurnButton.addListener(new ClickListener(){
              @Override
              public void clicked(InputEvent event, float x, float y) {
            	  server.passTurn();
                  System.out.println("Passed a turn!");
              }
          });

      Table table = new Table().padTop(8);
      table.top();
      table.setFillParent(true);

      background.setHeight(background.getHeight()*4);
      background.setWidth(background.getWidth()*4);
      background.setY(ManagementGame.HEIGHT - background.getHeight());



      table.add(passTurnButton).center().expandX();
      table.add(moneyLabel).center().expandX();
      table.add(grainLabel).left().expandX();
      table.add(seedLabel).right().expandX().padRight(40);

      stage.addActor(background);
      stage.addActor(table);
    }

    public void update() {
        updateLabels();
    }

    public void dispose() {
      stage.dispose();
    }
}
