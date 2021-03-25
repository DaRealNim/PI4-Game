package com.pi4.mgmtgame;

import com.badlogic.gdx.assets.AssetManager;
import com.pi4.mgmtgame.blocks.*;
import com.pi4.mgmtgame.resources.Grain;
import com.pi4.mgmtgame.resources.Plant;

public class ServerInteraction {
	private Map map;
	private Inventory[] invArray;
	private Inventory inv;
	private int turn;
	private int internalTurn;
	public int currentPlayer;
	private int nbOfPlayers;

	public ServerInteraction(Inventory[] inv, AssetManager manager, int nbOfPlayers) {
		this.map = new Map(10, 10, manager, this);
		this.nbOfPlayers = nbOfPlayers;
		this.invArray = new Inventory[nbOfPlayers];
		this.invArray = inv;
		this.turn = 0;
		this.internalTurn = 0;
		this.inv = invArray[0];

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

	public int getInternalTurn() {
		return internalTurn;
	}

	public boolean canBuildStructure(int x, int y, Structure struct) {
		Environment envBlock = map.getEnvironmentAt(x, y);
		return (envBlock.canBuild(struct) && inv.getMoney() >= struct.getConstructionCost() && envBlock != null);
	}

	public boolean requestBuildStructure(int x, int y, Structure struct) {
		if (canBuildStructure(x, y, struct)) {
			map.setStructAt(x, y, struct);
			inv.giveMoney(struct.getConstructionCost());
			return (true);
		}

		return (false);
	}
	
	public boolean canDestroyStructure(int x, int y) {
	    Structure structBlock = map.getStructAt(x, y);
	    return (structBlock != null && structBlock.testOwner(currentPlayer));
	}

	public boolean requestPlantSeed(int x, int y, Grain seed) {
		Structure structBlock = map.getStructAt(x, y);
		System.out.println("Seed id: " + seed.getId());
		if (structBlock instanceof Field && inv.hasGrain(seed) && structBlock != null
				&& structBlock.testOwner(currentPlayer)) {
			if (!((Field) structBlock).hasSeed()) {
				((Field) structBlock).plantSeed(seed);
				inv.removeGrain(seed.getId(), 1);
				return (true);
			}
		}

		return (false);
	}
	
	public boolean requestDestroyStructure(int x, int y) {
		if (canDestroyStructure(x, y)) {
			inv.receiveMoney((map.getStructAt(x, y).getConstructionCost()*30)/100);
			map.setStructAt(x, y, null);
			
			return (true);
		}
		
		return (false);
	}

	public boolean canHarvest(int x, int y) {
		Structure structBlock = map.getStructAt(x, y);
		if (structBlock instanceof Field && structBlock != null && structBlock.testOwner(currentPlayer)) {
			Field fieldBlock = (Field) structBlock;
			return (fieldBlock.hasSeedGrown());
		} else
			return false;
	}

	public boolean requestHarvest(int x, int y) {
		Structure structBlock = map.getStructAt(x, y);
		Plant harvested;
		Field fieldBlock;

		if (canHarvest(x, y)) {
			fieldBlock = (Field) structBlock;
			harvested = fieldBlock.harvest();
			harvested.addVolume(4);
			inv.addPlant(harvested.getId(), harvested.getVolume());
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
		internalTurn++;
		// à modifier
		inv = invArray[internalTurn % nbOfPlayers];
		currentPlayer = internalTurn % nbOfPlayers;
		if (internalTurn == nbOfPlayers) {
			turn++;
			internalTurn = 0;
			for (heightIndex = 0; heightIndex < mapHeight; heightIndex++) {
				for (widthIndex = 0; widthIndex < mapWidth; widthIndex++) {
					currBlock = map.getEnvironmentAt(heightIndex, widthIndex);
					if (currBlock != null)
						currBlock.passTurn();

					currBlock = map.getStructAt(heightIndex, widthIndex);
					if (currBlock != null)
						currBlock.passTurn();
				}
			}
		}
		System.out.println(this.inv);
		System.out.println(turn);
	}
}
