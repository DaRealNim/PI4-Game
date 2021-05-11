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
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.pi4.mgmtgame.resources.Resources;
import com.pi4.mgmtgame.screens.MainGameScreen;
import com.pi4.mgmtgame.HoverListener;

public class HUD {
    public Stage stage;
    private Viewport viewport;
    private Button passTurnButton;
    private Button marketButton;
    private AssetManager manager;
    private ServerInteraction server;
    private Label moneyLabel, turnLabel;
    private Label[] resourceVolumeLabels;
    private Inventory inv;
    private int internalTurn;
  	private Sound gameMusic;
    private long gameMusicId;
    private CheckBox showOwnersCheckBox;
    private Market market;

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

      gameMusicId = gameMusic.play();
      gameMusic.setLooping(gameMusicId, true);
      gameMusic.setVolume(gameMusicId, 0.3f);

    }

    public int getInternalTurn() {
        return internalTurn;
    }

    public void updateLabels() {
        this.inv = server.getInventory();

        int counter = 0;
        for (Resources ressource : inv.getRessources()) {
            resourceVolumeLabels[counter].setText(ressource.getVolume());
            counter++;
        }

        moneyLabel.setText("$" + inv.getMoney());
        turnLabel.setText("Month : " + server.getTurn());
    }

    public void show() {
      Skin buttonSkins = manager.get("hudButtons/hudButton.json", Skin.class);
      Skin iconsSkin = manager.get("popupIcons/popup.json", Skin.class);

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


      moneyLabel = new Label("$" + inv.getMoney(),  new Label.LabelStyle(manager.get("PixelOperator20", BitmapFont.class), Color.WHITE));
      turnLabel = new Label("Month : " + server.getTurn(),  new Label.LabelStyle(manager.get("PixelOperator20", BitmapFont.class), Color.WHITE));

      Table inventoryTable = new Table();
      final ScrollPane scrollpane = new ScrollPane(inventoryTable, manager.get("menuButtons/uiskin.json", Skin.class));
      scrollpane.setSize(scrollpane.getPrefWidth()-200, scrollpane.getPrefHeight()+30);
      final InputListener listener = new InputListener() {
          public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
              stage.setScrollFocus(scrollpane);
          }

          public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
              stage.setScrollFocus(null);
          }
      };
      scrollpane.addListener(listener);
      scrollpane.setScrollingDisabled(false, true);
      scrollpane.setFadeScrollBars(false);
      inv = server.getInventory();
      resourceVolumeLabels = new Label[inv.getRessources().length];

      int counter = 0;
      for (Resources ressource : inv.getRessources()) {
          final Resources res = ressource;
          Button image = new Button(iconsSkin, ressource.getTexture());
          image.setTransform(true);
          image.setScale(1);

          image.addListener(new HoverListener() {
              @Override
              public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                  MainGameScreen.mouseLabelText = res.toString();
              }
          });

          Label volume = new Label(Integer.toString(ressource.getVolume()), new Label.LabelStyle(manager.get("PixelOperator20", BitmapFont.class), Color.WHITE));
          resourceVolumeLabels[counter] = volume;
          counter++;

          inventoryTable.add(image).padRight(10).padBottom(15).padTop(15);
          inventoryTable.add(volume).padRight(50).padBottom(15).padTop(15);

      }


      passTurnButton.addListener(new ClickListener() {
              @Override
              public void clicked(InputEvent event, float x, float y) {
                  if (market != null) {
                    market.marketOpen = false;
                    market.remove();
                  }
            	  server.passTurn();
              }
          });

      marketButton.addListener(new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
        	  market = new Market(manager, server);
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
      table.add(passTurnButton).center().expandX().padTop(40).padRight(30);
      table.add(moneyLabel).center().expandX();
      table.add(turnLabel).center().expandX();
      table.add(showOwnersCheckBox).center().expandX();
      table.add(scrollpane).width(600).height(70).padRight(30).padTop(-10);

      table.pack();
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

    public void setMusicVolume(float value) {
        gameMusic.setVolume(gameMusicId, value);
    }
}
