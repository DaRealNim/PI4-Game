package com.pi4.mgmtgame.resources;

public class CarrotSeeds extends Grain {
	static int carrotSeedPrice = 10;

	public Plant getGrownPlant() {
		return new Carrot();
	}

	public int getId() {
		return 2;
	}

	public float getGrowingTime() {
		return 2*3;
	}
	public void addPrice(int p) {
		carrotSeedPrice += p;
	}

	public void subPrice(int p) {
		carrotSeedPrice -= p;
		if (carrotSeedPrice < 0)
			carrotSeedPrice = 0;
	}
	public int getPrice() {
		return carrotSeedPrice;
	}

	public String getTexture() {
		return("carrotseeds_icon");
	}

	public String getFieldSpriteName() {
		return "field_carrots";
	}


	public String getFieldMiddleSpriteName() {
		return "field_grow";
	}

	public String toString() {
		return ("Carrot seeds");
	}
}
