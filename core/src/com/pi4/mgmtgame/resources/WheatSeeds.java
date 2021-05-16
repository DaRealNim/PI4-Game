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
		return 4*3;
	}

	public int getPrice() {
		return (wheatSeedPrice);
	}


	public void addPrice(int p) {
		wheatSeedPrice += p;
	}

	public String getTexture() {
		return("wheatseeds_icon");
	}

	public void subPrice(int p) {
		wheatSeedPrice -= p;
		if (wheatSeedPrice < 0)
			wheatSeedPrice = 0;
	}

	public String getFieldSpriteName() {
		return "field_wheat_grew";
	}

	public String getFieldMiddleSpriteName() {
		return "field_wheat_middle";
	}

	public String toString() {
		return ("Wheat seeds");
	}
}
