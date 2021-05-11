package com.pi4.mgmtgame;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pi4.mgmtgame.resources.*;
import com.pi4.mgmtgame.screens.MainGameScreen;

public class Market extends Group {

	ServerInteraction server;
    private AssetManager manager;
	public static boolean marketOpen = false;


	public Market(AssetManager man, ServerInteraction server) {
		this.server = server;
		this.manager = man;
	    this.server = server;
		if (!marketOpen) {
			marketOpen = true;
	    	show();
		} else {
			remove();
		}
	}

	public void show()
	{
		Skin marketSkins = manager.get("marketRes/marketButtons.json", Skin.class);

		Texture bgTexture = manager.get("marketRes/bgMarket2.png", Texture.class);
		Image bg = new Image(bgTexture);

		Skin iconsSkin = manager.get("popupIcons/popup.json", Skin.class);

		Table products = new Table();
		final ScrollPane scrollpane = new ScrollPane(products, manager.get("menuButtons/uiskin.json", Skin.class));
		scrollpane.setSize(bg.getWidth()-60, bg.getHeight()-60);
		scrollpane.setX(15);
		scrollpane.setY(30);
		scrollpane.setFadeScrollBars(false);
		Button close = new Button(manager.get("popupIcons/popup.json", Skin.class), "closeButton");

		final Market market = this;
		final InputListener listener = new InputListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                getStage().setScrollFocus(scrollpane);
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
				marketOpen = false;
				remove();
			}
		});

		close.setTransform(true);
		close.setX(bg.getWidth() - 64);
		close.setY(bg.getHeight() - 64);

		Inventory inv = server.getInventory();

		for (Resources res : inv.getRessources())  {
			final Resources ressource = res;
			Button i = new Button(iconsSkin, res.getTexture());
			i.setTransform(true);
			i.setScale(2);

			Button buyButton = new Button(marketSkins, "buyButton");
			buyButton.setTransform(true);
			buyButton.setScale(1.5f);
			Button sellButton = new Button(marketSkins, "sellButton");
			sellButton.setTransform(true);
			sellButton.setScale(1.5f);

			String itemText = res.toString();
			String priceText = "" + res.getPrice();
			Label itemLabel = new Label(itemText, new Label.LabelStyle(manager.get("PixelOperator20", BitmapFont.class), Color.WHITE));
			final Label priceLabel = new Label(priceText, new Label.LabelStyle(manager.get("PixelOperator20", BitmapFont.class), Color.WHITE));
			priceLabel.setText("$" + server.getPrice(res));

			sellButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (ressource instanceof Plant)
						server.sellPlant((Plant)ressource, 1);
					else if (ressource instanceof Grain)
						server.sellGrain((Grain)ressource, 1);
					else if (ressource instanceof Item)
						server.sellItem((Item)ressource, 1);
					else if (ressource instanceof Animal)
						server.sellAnimal((Animal)ressource, 1);
					else if (ressource instanceof Product)
						server.sellProduct((Product)ressource, 1);
					priceLabel.setText("$" + server.getPrice(ressource));
				}
			});

			buyButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (ressource instanceof Plant)
						server.buyPlant((Plant)ressource, 1);
					else if (ressource instanceof Grain)
						server.buyGrain((Grain)ressource, 1);
					else if (ressource instanceof Item)
						server.buyItem((Item)ressource, 1);
					else if (ressource instanceof Animal)
						server.buyAnimal((Animal)ressource, 1);
					else if (ressource instanceof Product)
						server.buyProduct((Product)ressource, 1);
					priceLabel.setText("$" + server.getPrice(ressource));
				}
			});

			Table infoTable = new Table();
			infoTable.add(itemLabel);
			infoTable.row();
			infoTable.add(priceLabel);
			infoTable.row();
			Table buttonCell = new Table();
			buttonCell.add(buyButton).padRight(30);
			buttonCell.add(sellButton);
			infoTable.add(buttonCell);

			products.add(i).padRight(50);
			products.add(infoTable).padBottom(30);
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
