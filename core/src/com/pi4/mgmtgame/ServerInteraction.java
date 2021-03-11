package com.pi4.mgmtgame;

import com.badlogic.gdx.assets.AssetManager;
import com.pi4.mgmtgame.blocks.*;
import com.pi4.mgmtgame.resources.Grain;
import com.pi4.mgmtgame.resources.Plant;

public class ServerInteraction {
	private Map map;
	private Inventory inv;
	private int turn;

	public ServerInteraction(Inventory inv, AssetManager manager) {
		this.map = new Map(10, 10, manager, this);
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
		System.out.println("Seed id: "+seed.getId());
		if (structBlock instanceof Field && inv.hasGrain(seed)
			&& structBlock != null)
		{
			if (!((Field) structBlock).hasSeed())
			{
				((Field) structBlock).plantSeed(seed);
				inv.removeGrain(seed.getId(), 1);
				return (true);
			}
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

	public void passTurn() {
		int mapWidth = map.getMapWidth();
		int mapHeight = map.getMapHeight();
		int widthIndex;
		int heightIndex;
		Block currBlock;

		for (heightIndex = 0; heightIndex < mapHeight; heightIndex++)
		{
			for (widthIndex = 0; widthIndex < mapWidth; widthIndex++)
			{
				currBlock = map.getEnvironmentAt(heightIndex, widthIndex );
				if (currBlock != null)
					currBlock.passTurn();

				currBlock = map.getStructAt(heightIndex, widthIndex);
				if (currBlock != null)
					currBlock.passTurn();
			}
		}
	}



}
