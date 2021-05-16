package com.pi4.mgmtgame.resources;

public class Potato extends Plant {
	static final int basePrice = 1;
	static int potatoPrice = 20;

	public int getPrice() {
		return potatoPrice;
	}

	public void addPrice(int p) {
		potatoPrice += p;
	}

	public void subPrice(int p) {
		potatoPrice -= p;
		if (potatoPrice < 0)
			potatoPrice = 0;
	}

	public int getId() {
		return 1;
	}

	public String getTexture() {
		return("potato_icon");
	}

	public String toString() {
		return ("Potato");
	}
}
