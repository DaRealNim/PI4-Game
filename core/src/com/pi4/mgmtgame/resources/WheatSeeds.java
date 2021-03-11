package com.pi4.mgmtgame.resources;

public class WheatSeeds extends Grain {
	static int wheatSeedPrice = basePrice;


	public Plant getGrownPlant() {
		return new Wheat();
	}

	public int getId() {
		return 0;
	}


	public int getGrowingTime() {
		return 2;
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

	public String getFieldSpriteName() {
		return "field_wheat";
	}
}
