package com.pi4.mgmtgame.resources;

public class WheatSeeds extends Grain {
	static int wheatSeedPrice = 10;


	public Plant getGrownPlant() {
		return new Wheat();
	}

	public int getId() {
		return 0;
	}


	public float getGrowingTime() {
		return 2*3;
	}

	public int getPrice() {
		return (wheatSeedPrice);
	}


	public void addPrice(int p) {
		wheatSeedPrice += p;
	}

	public String getTexture() {
		return("marketRes/wheat.png");
	}

	public void subPrice(int p) {
		wheatSeedPrice -= p;
	}

	public String getFieldSpriteName() {
		return "field_carrots";
	}

	public String getFieldMiddleSpriteName() {
		return "field_grow";
	}

	public String toString() {
		return ("Wheat Seeds");
	}
}
