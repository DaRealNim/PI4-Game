package com.pi4.mgmtgame.resources;

public class TreeSeeds extends Grain {
	int growingTime = 1*3;
	static int treeSeedPrice = 10;

	public Plant getGrownPlant() {
		return new Wood();
	}

	public float getGrowingTime() {
		return growingTime;
	}
	public void addPrice(int p) {
		treeSeedPrice += p;
	}
 
	public void subPrice(int p) {
		treeSeedPrice -= p;
	}
	public int getPrice() {
		return treeSeedPrice;
	}

	public int getId() {
		return 3;
	}

	public String getFieldSpriteName() {
		return "treefarm_grew";
	}

	public String getFieldMiddleSpriteName() {
		return null;
	}

	public String getTexture() {
		return("marketRes/wood.png");
	}

	public String toString() {
		return ("Tree Seeds");
	}
	
}
