package com.pi4.mgmtgame;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pi4.mgmtgame.resources.Grain;
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
		Skin resSkins = manager.get("popupIcons/popup.json", Skin.class);
		
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
	    
	    for (Image i : marketRes)
	    {
	    	i.setHeight(16 * 4);
	    	i.setWidth(16 * 4);
	    	table.add(i);
		}
	    
	    stage.addActor(bg);
	    stage.addActor(table);
	}
	
	void buyGrain(Grain boughtGrain, int q) {
		server.getInventory().addGrain(boughtGrain.getId(), q);
		boughtGrain.addPrice((int) (boughtGrain.getPrice() * 1.05) * q);
	}
	
	void sellGrain(Grain soldGrain, int q) {
		server.getInventory().removeGrain(soldGrain.getId(), q);
		soldGrain.subPrice((int) (soldGrain.getPrice() * 1.05) * q);
	}
	
	void buyWood(Wood wood, int q) {
		server.getInventory().addGrain(wood.getId(), q);
		wood.addPrice((int) (wood.getPrice() * 1.05) * q);
	}
	
	void sellWood(Wood wood, int q) {
		server.getInventory().removeGrain(wood.getId(), q);
		wood.subPrice((int) (wood.getPrice() * 1.05) * q);
	}
}
