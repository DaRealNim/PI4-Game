package com.pi4.mgmtgame.resources;

public class Sheep extends Animal {
	static final int basePrice = 1;
	static int sheepPrice = 500;
	static int wheatConsumed = 5;

	public Product getAnimalProduct() {
		return new Wool();
	}

	public float getGrowingMax() {
		return 4;
	}

	public int getWheatConsumed() {
		return wheatConsumed;
	}

	public void addPrice(int p) {
		sheepPrice += p;
	}

	public void subPrice(int p) {
		sheepPrice -= p;
		if (sheepPrice < 0)
			sheepPrice = 0;
	}

	public int getPrice() {
		return sheepPrice;
	}

	public int getId() {
		return 1;
	}

	public String getTexture() {
		return("sheep_icon");
	}

	public String getPastureSpriteName() {
		return "pasture_sheep_full";
	}

	public String getPastureMiddleSpriteName() {
		return "pasture_sheep_growing";
	}

	public String toString() {
		return ("Sheep");
	}
}
