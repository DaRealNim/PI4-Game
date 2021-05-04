package com.pi4.mgmtgame;

import java.util.Random;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
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
import com.pi4.mgmtgame.resources.Item;
import com.badlogic.gdx.audio.Sound;

public class HUD {
    public Stage stage;
    private Viewport viewport;
    private Button passTurnButton;
    private Button marketButton;
    private AssetManager manager;
    private ServerInteraction server;
    private Label moneyLabel, grainLabel, seedLabel, itemsLabel, turnLabel;
    private Inventory inv;
    private int internalTurn;
    private String seedLabelText, grainLabelText, itemsLabelText;
  	private Sound gameMusic;
    private CheckBox showOwnersCheckBox;

    public HUD (AssetManager man, ServerInteraction server) {
      this.manager = man;
      this.server = server;
      this.inv = server.getInventory();

      viewport = new FitViewport(ManagementGame.WIDTH, ManagementGame.HEIGHT, new OrthographicCamera());
      stage = new Stage(viewport);
      this.show();

      Random rd = new Random();
      int max = 2;
      int min = 1;
      int val = rd.nextInt((max - min) + min) + min;

      if (val == 1)
        gameMusic = Gdx.audio.newSound(Gdx.files.internal("sounds/farmvilleidk.mp3"));
      else
        gameMusic = Gdx.audio.newSound(Gdx.files.internal("sounds/farmcityshit.mp3"));

      gameMusic.setLooping(100, true);
  		gameMusic.setVolume(100, 0.3f);
  		// gameMusic.play();
    }

    public int getInternalTurn() {
        return internalTurn;
    }

    public void updateLabels() {
        this.inv = server.getInventory();
        seedLabelText = "";
        grainLabelText = "";
        itemsLabelText = "";

        for (Grain seed : inv.getSeeds())
        {
      	  if (seed != null)
      	   seedLabelText += seed + ": " + seed.getVolume() + "  \n";
        }

        for (Plant plant : inv.getPlants())
        {
      	  if (plant != null)
      	   grainLabelText += plant + ": " + plant.getVolume() + "  \n";
        }

        for (Item item : inv.getItems()) {
            if (item != null)
              itemsLabelText += item + ": " + item.getVolume() + " \n";
        }

        moneyLabel.setText("DOLLA BILLZ: " + inv.getMoney());
        turnLabel.setText("Month : " + server.getTurn());
        seedLabel.setText(seedLabelText);
        grainLabel.setText(grainLabelText);
        itemsLabel.setText(itemsLabelText);
    }

    public void show() {
      Skin buttonSkins = manager.get("hudButtons/hudButton.json", Skin.class);

      Texture backgroundTexture = manager.get("hudButtons/hudBackground.png", Texture.class);
      Image background = new Image(backgroundTexture);

      showOwnersCheckBox = new CheckBox("Show land owners", manager.get("menuButtons/uiskin.json", Skin.class));

      passTurnButton = new Button(buttonSkins, "passTurn");
      passTurnButton.setZIndex(1);
      marketButton = new Button(buttonSkins, "shopButton");

      passTurnButton.setTransform(true);
      passTurnButton.setScale(2);

      marketButton.setTransform(true);
      marketButton.setScale(2);


      moneyLabel = new Label("DOLLA BILLZ: " + inv.getMoney(),  new Label.LabelStyle(new BitmapFont(), Color.WHITE));
      turnLabel = new Label("Month : " + server.getTurn(),  new Label.LabelStyle(new BitmapFont(), Color.WHITE));

      seedLabelText = "";
      grainLabelText = "";
      itemsLabelText = "";

      for (Grain seed : inv.getSeeds())
      {
    	  if (seed != null)
    	     seedLabelText += seed + ": " + seed.getVolume() + "  \n";
      }

      for (Plant plant : inv.getPlants())
      {
    	  if (plant != null)
    	     grainLabelText += plant + ": " + plant.getVolume() + "  \n";
      }

      for (Item item : inv.getItems()) {
          if (item != null)
            itemsLabelText += item + ": " + item.getVolume() + " \n";
      }

      grainLabel = new Label(grainLabelText ,  new Label.LabelStyle(new BitmapFont(), Color.WHITE));
      seedLabel = new Label(seedLabelText ,  new Label.LabelStyle(new BitmapFont(), Color.WHITE));
      itemsLabel = new Label(itemsLabelText ,  new Label.LabelStyle(new BitmapFont(), Color.WHITE));


      passTurnButton.addListener(new ClickListener() {
              @Override
              public void clicked(InputEvent event, float x, float y) {
            	  server.passTurn();
                  System.out.println("Passed a turn!");
              }
          });

      marketButton.addListener(new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
        	  Market market = new Market(manager, server);
        	  stage.addActor(market);
          }
      });

      Table table = new Table().padTop(8);
      table.top();
      table.setFillParent(true);

      background.setHeight(background.getHeight()*4);
      background.setWidth(background.getWidth()*4);
      background.setY(ManagementGame.HEIGHT - background.getHeight());

      table.add(marketButton).center().expandX().padTop(40);
      table.add(passTurnButton).center().expandX().padTop(40);
      table.add(moneyLabel).center().expandX();
      table.add(showOwnersCheckBox).center().expandX();
      table.add(turnLabel).center().expandX();
      table.add(grainLabel).left().expandX();
      table.add(seedLabel).left().expandX();
      table.add(itemsLabel).right().expandX().padRight(40);

      stage.addActor(background);
      stage.addActor(table);
    }

    public void update() {
        updateLabels();
    }

    public boolean shouldShowOwners() {
        return showOwnersCheckBox.isChecked();
    }

    public void dispose() {
      stage.dispose();
    }
}
