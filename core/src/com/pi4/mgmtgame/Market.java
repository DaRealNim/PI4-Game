package com.pi4.mgmtgame;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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

	    viewport = new FitViewport(ManagementGame.WIDTH, ManagementGame.HEIGHT, new OrthographicCamera());
	    stage = new Stage(viewport);
	}
	
	public void show()
	{
		Skin resSkins = manager.get("popupIcons/popup.json", Skin.class);
		
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
