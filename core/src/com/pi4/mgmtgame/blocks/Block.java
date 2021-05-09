package com.pi4.mgmtgame.blocks;

import java.io.Serializable;
import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Group;

import com.pi4.mgmtgame.ServerInteraction;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pi4.mgmtgame.Map;
import com.pi4.mgmtgame.Inventory;

public class Block extends Group implements Serializable {
    private transient Button displayedButton;
    protected transient AssetManager manager;
    protected String spriteName;
    private int x, y;
    protected int ownerID;
    private Stage popupStage;

    public Block(int x, int y) {
    	this.setGridX(x);
    	this.setGridY(y);
    }

    public void addViewController(final AssetManager manager, final ServerInteraction server, final Stage popupStage) {
        this.popupStage = popupStage;
    }

    public void setButton(Button b) {
        clear();
        addActor(b);
        this.displayedButton = b;
    }

    public Button getButton() {
        return this.displayedButton;
    }

    public void updateActors() {
        clear();
        changeStyle(this.spriteName);
        addActor(this.displayedButton);
    }

    public void changeStyle(String styleName) {
        this.spriteName = styleName;
        if (this.displayedButton != null) {
            getButton().setStyle(getButton().getSkin().get(styleName, Button.ButtonStyle.class));
        }
    }

    protected void updateMap(AssetManager manager, ServerInteraction server) {
        Map map = (Map) getParent();
        Map serverMap = server.getMap();
        serverMap.updateActors(manager, server, popupStage);
        Stage stage = getStage();
        map.remove();
        stage.addActor(serverMap);
    }

	public int getGridX() {
		return x;
	}

	public void setGridX(int x) {
		this.x = x;
	}

	public int getGridY() {
		return y;
	}

	public void setGridY(int y) {
		this.y = y;
	}

	public void passTurn(Inventory inv) {
	}

	public ArrayList<Structure> getAdjacentStruct() {
		ArrayList<Structure> structs = new ArrayList<Structure>();
        Map map = (Map) this.getParent();
        int x = getGridX();
        int y = getGridY();
		for(int i=-1;i<2;i++) {
			for(int j=-1;j<2;j++)	{
				if(i!=0 && j!=0 && x+i>=0 && x+i<map.getMapWidth() && y+j>=0 && y+j<map.getMapHeight())
				structs.add(map.getStructAt(x+i,y+j));
			}
		}
		return structs;
	}

	public ArrayList<Environment> getAdjacentEnv() {
	ArrayList<Environment> envs = new ArrayList<Environment>();
    Map map = (Map) this.getParent();
    int x = getGridX();
    int y = getGridY();
	for(int i=-1;i<2;i++) {
		for(int j=-1;j<2;j++)	{
			if(i!=0 && j!=0 && x+i>=0 && x+i<map.getMapWidth() && y+j>=0 && y+j<map.getMapHeight())
			envs.add(map.getEnvironmentAt(x+i,y+j));
		}
	}
	return envs;
	}

	public int getNearbyLakes() {
		int lks=0;
		ArrayList<Environment> envs = getAdjacentEnv();
		for(Environment e:envs){
			if(e instanceof Lake)
				lks++;
		}
		return lks;
	}

	public int getNearbySprinklers() {
		int spk=0;
		ArrayList<Structure> strs = getAdjacentStruct();
		for(Structure e:strs){
			if(e instanceof Sprinkler)
				spk++;
		}
		return spk;
	}

	public int getNearbyBoosts() {
		return getNearbySprinklers()+getNearbyLakes();
	}

    public void setSpriteName(String name) {
        this.spriteName = name;
    }

    public String getSpriteName() {
        return this.spriteName;
    }

    public boolean testOwner(int x) {
		if (x == ownerID)
			return true;
		return false;
	}

	public int getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(int x) {
		ownerID = x;
	}

}
