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

		Texture bgTexture = manager.get("marketRes/bgMarket.png", Texture.class);
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

		Table grainTable = new Table();
	  grainTable.setX(bg.getWidth() / 4);
	  grainTable.setY(bg.getHeight() / 2);

		Table plantTable = new Table();
	  plantTable.setX(bg.getWidth() / 4);
		plantTable.setY(bg.getHeight() / 2);

		Button close = new Button(manager.get("popupIcons/popup.json", Skin.class), "closeButton");

		close.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				remove();
			}
		});

		close.setTransform(true);
		close.setScale(3);
		close.setX(bg.getWidth() - 64);
		close.setY(bg.getHeight() - 64);

	  for (Plant p : server.getInventory().getPlants())  {
	  		final Plant plant = p;
				Button i = new Button(new TextureRegionDrawable(manager.get(p.getTexture(), Texture.class)));
				i.setTransform(true);
				i.setScale(2);

				Button buyButton = new Button(marketSkins, "buyButton");
	  		Button sellButton = new Button(marketSkins, "sellButton");

				String itemText = p.toString();
				String priceText = "" + p.getPrice();
				Label itemLabel = new Label(itemText, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
				final Label priceLabel = new Label(priceText, new Label.LabelStyle(new BitmapFont(), Color.WHITE));

	    	sellButton.addListener(new ClickListener() {
	            @Override
	            public void clicked(InputEvent event, float x, float y) {
	          	  server.sellPlant(plant, 1);
								priceLabel.setText(server.getPrice(plant));
	            }
	    	});

	    	buyButton.addListener(new ClickListener() {
	            @Override
	            public void clicked(InputEvent event, float x, float y) {
	          	  server.buyPlant(plant, 1);
								priceLabel.setText(server.getPrice(plant));
	            }
	    	});

				plantTable.add(itemLabel).padLeft(110);
				plantTable.row();
	    	plantTable.add(i).padRight(25).padTop(25).padLeft(120);
				plantTable.row();
				plantTable.row();
				plantTable.add(priceLabel).padLeft(110).padBottom(-10);
				plantTable.row();
	    	plantTable.add(buyButton);
	    	plantTable.add(sellButton).padRight(10);
	    	plantTable.row();
	    }


	    for (Grain g : server.getInventory().getSeeds()) {
	    	final Grain grain = g;
				System.out.println(g.getTexture());

				Button i = new Button(new TextureRegionDrawable(manager.get(g.getTexture(), Texture.class)));
				i.setTransform(true);
				i.setScale(2);

				String itemText = g.toString(); //haha g string
				String priceText = "" + g.getPrice();
				Label itemLabel = new Label(itemText, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
				final Label priceLabel = new Label(priceText, new Label.LabelStyle(new BitmapFont(), Color.WHITE));

	    	Button buyButton = new Button(marketSkins, "buyButton");
	    	Button sellButton = new Button(marketSkins, "sellButton");

	    	sellButton.addListener(new ClickListener() {
	            @Override
	            public void clicked(InputEvent event, float x, float y) {
	          	  server.sellGrain(grain, 1);
								priceLabel.setText(server.getPrice(grain));
	            }
	    	});

	    	buyButton.addListener(new ClickListener() {
	            @Override
	            public void clicked(InputEvent event, float x, float y) {
	          	  server.buyGrain(grain, 1);
								priceLabel.setText(server.getPrice(grain));
	            }
	    	});

				grainTable.add(itemLabel).padLeft(150);
				grainTable.row();
	    	grainTable.add(i).padRight(15).padTop(25).padLeft(150);
				grainTable.row();
				grainTable.add(priceLabel).padLeft(150).padBottom(-10);
				grainTable.row();
	    	grainTable.add(buyButton).padLeft(30);
	    	grainTable.add(sellButton);
	    	grainTable.row();
		}

		grainTable.padLeft(260);
		plantTable.padRight(-30);
		plantTable.right();
		grainTable.left();

	  addActor(bg);
		addActor(close);
	  addActor(plantTable);
		addActor(grainTable);
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
