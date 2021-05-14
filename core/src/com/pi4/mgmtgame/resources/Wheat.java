package com.pi4.mgmtgame.resources;

public class Wheat extends Plant {
	static final int basePrice = 1;
	static int wheatPrice = 10;

	public int getPrice() {
		return wheatPrice;
	}

	public void addPrice(int p) {
		wheatPrice += p;
	}

	public void subPrice(int p) {
		wheatPrice -= p;
		if (wheatPrice < 0)
			wheatPrice = 0;
	}

	public int getId() {
		return 0;
	}

	public String getTexture() {
		return("wheat_icon");
	}

	public String toString() {
		return ("Wheat");
	}
}
