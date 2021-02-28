package com.pi4.mgmtgame.resources;

public class CarrotSeeds extends Grain {
	int growingTime = 1;
	int basePrice = 1;

	public int getPrice() {
		return basePrice;
	}

	@Override
	public int getGrowingTime() {
		return growingTime;
	}

	@Override
	public Plant getGrownPlant() {
		return new Carrots();
	}
}
