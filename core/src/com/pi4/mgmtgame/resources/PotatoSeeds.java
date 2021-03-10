package com.pi4.mgmtgame.resources;

public class PotatoSeeds extends Grain {
	final int id = 4;
	int growingTime = 1;
	static int potatoSeedPrice = basePrice;
	
	public Plant getGrownPlant() {
		return new Potatoes();
	}
	
	public int getGrowingTime() {
		return growingTime;
	}
	public void addPrice(int p) {
		potatoSeedPrice += p;
	}

	public void subPrice(int p) {
		potatoSeedPrice -= p;
	}
	public int getPrice() {
		return potatoSeedPrice;
	}
}