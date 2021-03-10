package com.pi4.mgmtgame;

import com.badlogic.gdx.assets.AssetManager;
import com.pi4.mgmtgame.blocks.*;
import com.pi4.mgmtgame.resources.Grain;
import com.pi4.mgmtgame.resources.Plant;

public class ServerInteraction {
	private Map map;
	private Inventory inv;
	private int turn;
	
	public ServerInteraction(Map map, Inventory  inv) {
		this.map = map;
		this.inv =  inv;
		this.turn = 0;
	}
	
	public Map getMap() {
		return map;
	}
	
	public Inventory getInventory() {
		return inv;
	}

	public int getTurn() {
		return turn;
	}

	public boolean requestBuildStructure(int x, int y, Structure struct) {
		Environment envBlock = map.getEnvironmentAt(x, y);		
		
		if (envBlock.canBuild(struct) 
			&& inv.getMoney() >= struct.getConstructionCost()
			&& envBlock != null) 
		{
			map.setStructAt(x, y, struct);
			inv.giveMoney(struct.getConstructionCost());
			return (true);
		}
		
		return (false);
	}
	
	public boolean requestPlantSeed(int x, int y, Grain seed) {
		Structure structBlock = map.getStructAt(x, y);
	
		if (structBlock instanceof Field && inv.hasGrain(seed)
			&& structBlock != null) 
		{
			((Field) structBlock).plantSeed(seed);
			inv.removeGrain(seed.getId(), 1);
			return (true);
		}
		
		return (false);
	}
	
	public boolean requestHarvest(int x, int y) {
		Structure structBlock = map.getStructAt(x, y);
		Plant harvested;
		Field fieldBlock;
		
		if (structBlock instanceof Field && structBlock != null) {
			fieldBlock = (Field) structBlock;
			
			if (fieldBlock.hasSeedGrown()) {
				harvested = fieldBlock.harvest();
				harvested.addVolume(4);
				inv.addPlant(harvested.getId(), harvested.getVolume());
			}
			return (true);
		}
		return (false);
	}
	
	public void nextTurn() {
		int mapWidth = map.getMapWidth();
		int mapHeight = map.getMapHeight();
		int widthIndex = 0;
		int heightIndex = 0;
		Block currBlock;
		
		while (widthIndex < mapWidth) {
			while (heightIndex < mapHeight) {
				currBlock = map.getEnvironmentAt(widthIndex, heightIndex);
				currBlock.passTurn();
				
				currBlock = map.getStructAt(widthIndex, heightIndex);
				currBlock.passTurn();
				
				heightIndex++;
			}
			widthIndex++;
		}
	}
	
	
	
}
