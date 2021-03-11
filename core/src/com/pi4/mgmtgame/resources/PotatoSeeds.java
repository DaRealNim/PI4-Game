package com.pi4.mgmtgame.resources;

public class PotatoSeeds extends Grain {
	static int potatoSeedPrice = basePrice;

	public Plant getGrownPlant() {
		return new Potato();
	}

	public int getGrowingTime() {
		return 3;
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

	public int getId() {
		return 1;
	}

	public String getFieldSpriteName() {
		return "field_potatoes";
	}
}
