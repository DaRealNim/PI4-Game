package com.pi4.mgmtgame.resources;

public class CarrotSeeds extends Grain {
	final int id = 6;
	int growingTime = 1;
	static int carrotSeedPrice = basePrice;
	
	public Plant getGrownPlant() {
		return new Carrots();
	}
	
	public int getGrowingTime() {
		return growingTime;
	}
	public void addPrice(int p) {
		carrotSeedPrice += p;
	}

	public void subPrice(int p) {
		carrotSeedPrice -= p;
	}
	public int getPrice() {
		return carrotSeedPrice;
	}
}