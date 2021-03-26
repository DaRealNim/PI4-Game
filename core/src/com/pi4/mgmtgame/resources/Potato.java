package com.pi4.mgmtgame.resources;

public class Potato extends Plant {
	static final int basePrice = 1;
	static int potatoPrice = basePrice;

	public int getPrice() {
		return potatoPrice;
	}

	public void addPrice(int p) {
		potatoPrice += p;
	}

	public void subPrice(int p) {
		potatoPrice -= p;
	}

	public int getId() {
		return 1;
	}
	
	public String getTexture() {
		return("marketRes/potato.png");
	}
	
	public String toString() {
		return ("Potato");
	}
}