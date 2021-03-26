package com.pi4.mgmtgame;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pi4.mgmtgame.resources.Grain;
import com.pi4.mgmtgame.resources.Plant;
import com.pi4.mgmtgame.resources.Resources;
import com.pi4.mgmtgame.resources.Wood;

public class Market {
	
	ServerInteraction server;
	public Stage stage;
    private Viewport viewport;
    private AssetManager manager;
    
    
	public Market(AssetManager man, ServerInteraction server) {
		this.server = server;
		this.manager = man;
	    this.server = server;
	    
	    viewport = new FitViewport(ManagementGame.WIDTH / 4, ManagementGame.HEIGHT / 4, new OrthographicCamera());
	    viewport.apply();
	    
	    stage = new Stage(viewport);
	    
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
	    table.setFillParent(true);
		
		bg.setHeight(bg.getHeight() / 4);
	    bg.setWidth(bg.getWidth() / 4);
	    
	    for (Plant p : server.getInventory().getPlants())
	    {
	    	System.out.println(p.getTexture());
	    	Image i = new Image(manager.get(p.getTexture(), Texture.class));
	    	i.setHeight(64);
	    	i.setWidth(64);
	    	table.add(i);
		}
	    
	    for (Grain g : server.getInventory().getSeeds())
	    {
	    	Image i = new Image(manager.get(g.getTexture(), Texture.class));
	    	i.setHeight(64);
	    	i.setWidth(64);
	    	table.add(i);
		}
	    
	    stage.addActor(bg);
	    stage.addActor(table);
	}
	
	
	boolean userHasMoneyToBuy(int q, Resources r){
		return (server.getInventory().getMoney() >= r.getPrice() * q);
	}
	
	void buyGrain(Grain boughtGrain, int q) {
		Inventory userInv = server.getInventory();
		int grainPrice = boughtGrain.getPrice();
		
		if (userHasMoneyToBuy(q, boughtGrain)) {
			userInv.giveMoney(grainPrice * q);
			userInv.addGrain(boughtGrain.getId(), q);
			boughtGrain.addPrice((int) (grainPrice * 1.05) * q);
		}
	}
	
	void sellGrain(Grain soldGrain, int q) {
		Inventory userInv = server.getInventory();
		int grainPrice = soldGrain.getPrice();
		
		if (userHasMoneyToBuy(q, soldGrain)) {
			userInv.receiveMoney(grainPrice * q);
			userInv.removeGrain(soldGrain.getId(), q);
			soldGrain.addPrice((int) (grainPrice * 1.05) * q);
		}
		soldGrain.subPrice((int) (soldGrain.getPrice() * 1.05) * q);
	}
	
	void buyPlant(Plant p, int q) {
		Inventory userInv = server.getInventory();
		int plantPrice = p.getPrice();
		
		if (userHasMoneyToBuy(q, p)) {
			userInv.giveMoney(plantPrice * q);
			userInv.addPlant(p.getId(), q);
			p.addPrice((int) (plantPrice * 1.05) * q);
		}
		
		p.addPrice((int) (plantPrice * 1.05) * q);
	}
	
	void sellPlant(Plant p, int q) {
		Inventory userInv = server.getInventory();
		int plantPrice = p.getPrice();
		
		if (userHasMoneyToBuy(q, p)) {
			userInv.receiveMoney(plantPrice * q);
			userInv.removePlant(p.getId(), q);
			p.addPrice((int) (plantPrice * 1.05) * q);
		}
		
		p.addPrice((int) (plantPrice * 1.05) * q);
	}
}
