package com.pi4.mgmtgame.resources;

public class Leather extends Product{
	static final int basePrice = 1;
	static int leatherPrice = 75;
	
	@Override
	public int getPrice() {
		return leatherPrice;
	}

	public void addPrice(int p) {
		leatherPrice += p;
	}

	public void subPrice(int p) {
		leatherPrice -= p;
	}

	public int getId() {
		return 1;
	}

	public String getTexture() {
		return("marketRes/leather.png");
	}

	public String toString() {
		return ("Leather");
	}
}
