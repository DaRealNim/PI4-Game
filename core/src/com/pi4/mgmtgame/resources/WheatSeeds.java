package com.pi4.mgmtgame.resources;

public class WheatSeeds extends Grain {
	final int id = 0;
	int growingTime = 1;
	static int wheatSeedPrice = basePrice;

	
	public Plant getGrownPlant() {
		return new Wheat();
	}

	
	public int getGrowingTime() {
		return growingTime;
	}
	
	public int getPrice() {
		return (wheatSeedPrice);
	}

	
	public void addPrice(int p) {
		wheatSeedPrice += p;
	}


	public void subPrice(int p) {
		wheatSeedPrice -= p;
	}
}
