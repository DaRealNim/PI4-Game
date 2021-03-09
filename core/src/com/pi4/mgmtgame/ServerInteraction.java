package com.pi4.mgmtgame;

import com.badlogic.gdx.assets.AssetManager;
import com.pi4.mgmtgame.blocks.*;
import com.pi4.mgmtgame.resources.Grain;

public class ServerInteraction {
	private Map map;
	private Inventory inventaire;
	private AssetManager manager;
	private int turn;
	
	public ServerInteraction(Map map, Inventory inventaire, AssetManager manager) {
		this.map = map;
		this.inventaire = inventaire;
		this.manager = manager;
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
		/*
		 * vérifie que struct peut bien être construit sur le terrain pointé 
		 * (Environment.canBuild(Structure struct)) et que le joueur a bien 
		 * assez d'argent
		 */
		
		Environment e = map.getEnvironmentAt(x, y);
		
		if (e == null) {
			return false;
		}
		
		boolean allowed = e.canBuild(struct) && (inventaire.getMoney() >= struct.getConstructionCost());
		
		if (allowed) {
			map.setStructAt(x, y, struct);
			inventaire.giveMoney(struct.getConstructionCost());
			return true;
		}
		
		return false;
	}
	
	public boolean requestPlantSeed(int x, int y, Grain seed) {
		/*
		 * vérifie que la case est bien un champ et que le joueur 
		 * a bien une graine dans son inventaire, y plante une graine, 
		 * s'occupe de la retirer de l'inventaire
		 */
		
		Structure e = map.getStructAt(x, y);
		if (e == null) {
			return false;
		}
		
		boolean allowed = (e instanceof Field) && inventaire.hasGrain(seed);
	
		if (allowed) {
			((Field) e).plantSeed(seed);
			/*
			 * Ici faut faire un for c'est relou 
			 */
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
