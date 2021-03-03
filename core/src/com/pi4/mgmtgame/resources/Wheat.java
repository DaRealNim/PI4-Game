package com.pi4.mgmtgame.resources;

public class Wheat extends Plant {
	static final int basePrice = 1;
	static int price = basePrice;
	public int getPrice() {
		return price;
	}

	public void addPrice(int p) {
		price += p;
	}

	public void subPrice(int p) {
		price -= p;
	}

	
}
