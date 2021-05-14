package com.pi4.mgmtgame.resources;

public class PotatoSeeds extends Grain {
	static int potatoSeedPrice = 10;

	public Plant getGrownPlant() {
		return new Potato();
	}

	public float getGrowingTime() {
		return 3*3;
	}
	public void addPrice(int p) {
		potatoSeedPrice += p;
	}

	public void subPrice(int p) {
		potatoSeedPrice -= p;
		if (potatoSeedPrice < 0)
			potatoSeedPrice = 0;
	}

	public int getPrice() {
		return potatoSeedPrice;
	}

	public int getId() {
		return 1;
	}

	public String getTexture() {
		return("potatoseeds_icon");
	}

	public String getFieldSpriteName() {
		return "field_potato_grew";
	}

	public String getFieldMiddleSpriteName() {
		return "field_potato_middle";
	}

	public String toString() {
		return ("Potato seeds");
	}
}
