package com.pi4.mgmtgame.resources;

public class TreeSeeds extends Grain {
	int growingTime = 1;
	static int treeSeedPrice = basePrice;

	public Plant getGrownPlant() {
		return new Wood();
	}

	public int getGrowingTime() {
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
		return "none";
	}
	
	public String toString() {
		return ("Tree Seeds");
	}
}
