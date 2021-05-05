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
	}
	
	public int getPrice() {
		return potatoSeedPrice;
	}

	public int getId() {
		return 1;
	}

	public String getTexture() {
		return("marketRes/potato.png");
	}

	public String getFieldSpriteName() {
		return "field_potato_grew";
	}

	public String getFieldMiddleSpriteName() {
		return "field_potato_middle";
	}

	public String toString() {
		return ("Potato Seeds");
	}
}
