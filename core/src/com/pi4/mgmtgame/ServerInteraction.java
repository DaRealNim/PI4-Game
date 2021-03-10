package com.pi4.mgmtgame;

import com.badlogic.gdx.assets.AssetManager;
import com.pi4.mgmtgame.blocks.*;
import com.pi4.mgmtgame.resources.Grain;

public class ServerInteraction {
	private Map map;
	private Inventory inventaire;
	private int turn;
	
	public ServerInteraction(Map map, Inventory inventaire) {
		this.map = map;
		this.inventaire = inventaire;
		this.turn = 0;
	}
	
	public Map getMap() {
		return map;
	}
	
	public Inventory getInventaire() {
		return inventaire;
	}

	public int getTurn() {
		return turn;
	}

	public boolean requestBuildStructure(int x, int y, Structure struct) {
		Environment e = map.getEnvironmentAt(x, y);		
		
		if (e.canBuild(struct) 
			&& inventaire.getMoney() >= struct.getConstructionCost()
			&& e != null) 
		{
			map.setStructAt(x, y, struct);
			inventaire.giveMoney(struct.getConstructionCost());
			return true;
		}
		return false;
	}
	
	public boolean requestPlantSeed(int x, int y, Grain seed) {
		Structure e = map.getStructAt(x, y);
	
		if (e instanceof Field && inventaire.hasGrain(seed)
			&& e != null) 
		{
			((Field) e).plantSeed(seed);
			inventaire.removeGrain(seed.getId(), 1);
			return true;
		}
		
		return false;
		
	}
	
	public boolean requestHarvest(int x, int y) {
		/*
		 * vérifie que la case est bien un champ et que la graine a poussé, 
		 * si oui récolte la graine (Field.harvest()) et la place dans 
		 * l'inventaire
		 */
		
		return false;
	}
	public void nextTurn() {
		/*
		 * s'occupe de parcourir chaque champ et de faire pousser les 
		 * plantes a l'intérieur d'un cran, incrémente turn
		 */
	}
	
	
	
}
