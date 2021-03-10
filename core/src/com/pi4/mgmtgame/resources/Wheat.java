package com.pi4.mgmtgame.resources;

public class Wheat extends Plant {
	final int id = 2;
	static final int basePrice = 1;
	static int wheatPrice = basePrice;
	
	public int getPrice() {
		return wheatPrice;
	}

	public void addPrice(int p) {
		wheatPrice += p;
	}

	public void subPrice(int p) {
		wheatPrice -= p;
	}
}
