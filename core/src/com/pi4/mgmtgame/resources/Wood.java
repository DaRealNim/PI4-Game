package com.pi4.mgmtgame.resources;

public class Wood extends Plant {
	final int id = 1;
	static final int basePrice = 1;
	static int woodPrice = basePrice;

	public int getPrice() {
		return woodPrice;
	}

	public void addPrice(int p) {
		woodPrice += p;
	}

	public void subPrice(int p) {
		woodPrice -= p;
	}
}
