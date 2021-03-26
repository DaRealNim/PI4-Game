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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pi4.mgmtgame.resources.Grain;
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
		
		Table table = new Table();
	    table.setX(bg.getHeight() / 2);
	    table.setY(bg.getWidth() / 2);
	    
	    for (Plant p : server.getInventory().getPlants())
	    {
	    	final Plant plant = p;
	    	Image i = new Image(manager.get(p.getTexture(), Texture.class));
	    	Button buyButton = new Button(marketSkins, "buyButton");
	    	Button sellButton = new Button(marketSkins, "sellButton");
	    	
	    	sellButton.addListener(new ClickListener() {
	            @Override
	            public void clicked(InputEvent event, float x, float y) {
	          	  sellPlant(plant, 1);
	            }       
	    	});
	    	
	    	buyButton.addListener(new ClickListener() {
	            @Override
	            public void clicked(InputEvent event, float x, float y) {
	          	  buyPlant(plant, 1);
	            }       
	    	});
	    	
	    	table.add(i);
	    	table.add(buyButton).padRight(25);
	    	table.add(sellButton).padRight(50);
	    	table.row();
	    }
	    
	    for (Grain g : server.getInventory().getSeeds())
	    {
	    	final Grain grain = g;
	    	Image i = new Image(manager.get(g.getTexture(), Texture.class));
	    	Button buyButton = new Button(marketSkins, "buyButton");
	    	Button sellButton = new Button(marketSkins, "sellButton");

	    	sellButton.addListener(new ClickListener() {
	            @Override
	            public void clicked(InputEvent event, float x, float y) {
	          	  sellGrain(grain, 1);
	            }       
	    	});
	    	
	    	buyButton.addListener(new ClickListener() {
	            @Override
	            public void clicked(InputEvent event, float x, float y) {
	          	  buyGrain(grain, 1);
	            }       
	    	});
	    	
	   
	    	table.add(i);
	    	table.add(buyButton);
	    	table.add(sellButton);
	    	table.row();
		}
	    
	    addActor(bg);
	    addActor(table);
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
	
	void buyGrain(Grain boughtGrain, int q) {
		Inventory userInv = server.getInventory();
		int grainPrice = boughtGrain.getPrice();
		
		if (userHasMoneyToBuy(q, boughtGrain)) {
			userInv.giveMoney(grainPrice * q);
			userInv.addGrain(boughtGrain.getId(), q);
			boughtGrain.addPrice(1);
		}
	}
	
	void sellGrain(Grain soldGrain, int q) {
		Inventory userInv = server.getInventory();
		int grainPrice = soldGrain.getPrice();
		
		if (userCanSellGrain(q, soldGrain)) {
			userInv.receiveMoney(grainPrice * q);
			userInv.removeGrain(soldGrain.getId(), q);
			soldGrain.subPrice(1);
		}
	}
	
	void buyPlant(Plant boughtPlant, int q) {
		Inventory userInv = server.getInventory();
		int plantPrice = boughtPlant.getPrice();
		
		if (userHasMoneyToBuy(q, boughtPlant)) {
			userInv.giveMoney(plantPrice * q);
			userInv.addPlant(boughtPlant.getId(), q);
			boughtPlant.addPrice(1);
		}
	}
	
	void sellPlant(Plant soldPlant, int q) {
		Inventory userInv = server.getInventory();
		int plantPrice = soldPlant.getPrice();
		
		if (userCanSellPlant(q, soldPlant)) {
			userInv.receiveMoney(plantPrice * q);
			userInv.removePlant(soldPlant.getId(), q);
			soldPlant.subPrice(1);
		}
	}
}
