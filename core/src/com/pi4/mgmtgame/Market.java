package com.pi4.mgmtgame;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pi4.mgmtgame.resources.Grain;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.pi4.mgmtgame.resources.Plant;
import com.pi4.mgmtgame.resources.Resources;
import com.pi4.mgmtgame.resources.Wood;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.pi4.mgmtgame.screens.MainGameScreen;

public class Market extends Group {

	ServerInteraction server;
    private AssetManager manager;


	public Market(AssetManager man, ServerInteraction server) {
		this.server = server;
		this.manager = man;
	    this.server = server;

	    this.show();
	}

	public void show()
	{
		Skin marketSkins = manager.get("marketRes/marketButtons.json", Skin.class);

		Texture bgTexture = manager.get("marketRes/bgMarket2.png", Texture.class);
		Image bg = new Image(bgTexture);

		Texture woodTxt = manager.get("marketRes/wood.png", Texture.class);
		Texture carrotTxt = manager.get("marketRes/carrot.png", Texture.class);
		Texture potatoTxt = manager.get("marketRes/potato.png", Texture.class);
		Texture wheatTxt = manager.get("marketRes/wheat.png", Texture.class);
		Texture billTxt = manager.get("marketRes/bill.png", Texture.class);

		Image wood = new Image(woodTxt);
		Image carrot = new Image(carrotTxt);
		Image potato = new Image(potatoTxt);
		Image wheat = new Image(wheatTxt);
		Image bill = new Image(billTxt);

		Image[] marketRes = {wood, carrot, potato, wheat, bill};

		Table products = new Table();
		final ScrollPane scrollpane = new ScrollPane(products, manager.get("menuButtons/uiskin.json", Skin.class));
		scrollpane.setSize(bg.getWidth()-60, bg.getHeight()-60);
		scrollpane.setX(30);
		scrollpane.setY(30);
		scrollpane.setFadeScrollBars(false);
		Button close = new Button(manager.get("popupIcons/popup.json", Skin.class), "closeButton");

		final Market market = this;
		final InputListener listener = new InputListener() {
			@Override
		    public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
		        if (amountY > 0) {
		            scrollpane.fling(0.5f, 0, -1000);
		        }
		        if (amountY < 0) {
		            scrollpane.fling(0.5f, 0, 1000);
		        }
		        return true;
		    }

			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                getStage().setScrollFocus(market);
            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                getStage().setScrollFocus(null);
            }

		};
		addListener(listener);

		close.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				removeListener(listener);
				remove();
			}
		});

		close.setTransform(true);
		close.setX(bg.getWidth() - 64);
		close.setY(bg.getHeight() - 64);

		for (Plant p : server.getInventory().getPlants())  {
			final Plant plant = p;
			Button i = new Button(new TextureRegionDrawable(manager.get(p.getTexture(), Texture.class)));
			i.setTransform(true);
			i.setScale(5);

			Button buyButton = new Button(marketSkins, "buyButton");
			buyButton.setTransform(true);
			buyButton.setScale(1.5f);
			Button sellButton = new Button(marketSkins, "sellButton");
			sellButton.setTransform(true);
			sellButton.setScale(1.5f);

			String itemText = p.toString();
			String priceText = "" + p.getPrice();
			Label itemLabel = new Label(itemText, new Label.LabelStyle(manager.get("PixelOperator20", BitmapFont.class), Color.WHITE));
			final Label priceLabel = new Label(priceText, new Label.LabelStyle(manager.get("PixelOperator20", BitmapFont.class), Color.WHITE));
			priceLabel.setText("$" + server.getPrice(plant));

			sellButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					server.sellPlant(plant, 1);
					priceLabel.setText("$" + server.getPrice(plant));
				}
			});

			buyButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					server.buyPlant(plant, 1);
					priceLabel.setText("$" + server.getPrice(plant));
				}
			});

			products.add(i);
			products.add(itemLabel).padLeft(70).padBottom(100);
			products.add(priceLabel).padLeft(-50);
			products.add(buyButton).padRight(30);
			products.add(sellButton);
			products.row();

		}


		for (Grain g : server.getInventory().getSeeds()) {
			final Grain grain = g;
			System.out.println(g.getTexture());

			Button i = new Button(new TextureRegionDrawable(manager.get(grain.getTexture(), Texture.class)));
			i.setTransform(true);
			i.setScale(5);

			Button buyButton = new Button(marketSkins, "buyButton");
			buyButton.setTransform(true);
			buyButton.setScale(1.5f);
			Button sellButton = new Button(marketSkins, "sellButton");
			sellButton.setTransform(true);
			sellButton.setScale(1.5f);

			String itemText = grain.toString();
			String priceText = "" + grain.getPrice();
			Label itemLabel = new Label(itemText, new Label.LabelStyle(manager.get("PixelOperator20", BitmapFont.class), Color.WHITE));
			final Label priceLabel = new Label(priceText, new Label.LabelStyle(manager.get("PixelOperator20", BitmapFont.class), Color.WHITE));
			priceLabel.setText("$" + server.getPrice(grain));

			sellButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					server.sellGrain(grain, 1);
					priceLabel.setText("$" + server.getPrice(grain));
				}
			});

			buyButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					server.buyGrain(grain, 1);
					priceLabel.setText("$" + server.getPrice(grain));
				}
			});

			products.add(i);
			products.add(itemLabel).padLeft(70).padBottom(100);
			products.add(priceLabel).padLeft(-50);
			products.add(buyButton).padRight(30);
			products.add(sellButton);
			products.row();
		}

		addActor(bg);
		addActor(scrollpane);
		addActor(close);
	}


	boolean userHasMoneyToBuy(int q, Resources r) {
		return (server.getInventory().getMoney() >= r.getPrice() * q);
	}

	boolean userCanSellPlant(int q, Plant p) {
		return (server.getInventory().getPlants()[p.getId()].getVolume() >= q);
	}

	boolean userCanSellGrain(int q, Grain g) {
		return (server.getInventory().getSeeds()[g.getId()].getVolume() >= q);
	}


}
