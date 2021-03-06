package com.pi4.mgmtgame.resources;

public class Carrot extends Plant{
	static final int basePrice = 1;
	static int carrotPrice = 10;

	public int getPrice() {
		return carrotPrice;
	}

	public void addPrice(int p) {
		carrotPrice += p;
	}

	public void subPrice(int p) {
		carrotPrice -= p;
		if (carrotPrice < 0)
			carrotPrice = 0;
	}

	public int getId() {
		return 2;
	}

	public String getTexture() {
		return("carrot_icon");
	}

	public String toString() {
		return ("Carrot");
	}
}
