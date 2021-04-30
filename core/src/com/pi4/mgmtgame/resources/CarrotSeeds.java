package com.pi4.mgmtgame.resources;

public class CarrotSeeds extends Grain {
	static int carrotSeedPrice = 10;

	public Plant getGrownPlant() {
		return new Carrot();
	}

	public int getId() {
		return 2;
	}

	public int getGrowingTime() {
		return 2;
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

	public String getTexture() {
		return("marketRes/carrot.png");
	}

	public String getFieldSpriteName() {
		return "field_carrots";
	}


	public String getFieldMiddleSpriteName() {
		return "field_grow";
	}

	public String toString() {
		return ("Carrot Seeds");
	}
}
