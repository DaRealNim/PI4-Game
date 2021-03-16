package com.pi4.mgmtgame.resources;

public class Carrot extends Plant{
	static final int basePrice = 1;
	static int carrotPrice = basePrice;

	public int getPrice() {
		return carrotPrice;
	}

	public void addPrice(int p) {
		carrotPrice += p;
	}

	public void subPrice(int p) {
		carrotPrice -= p;
	}

	public int getId() {
		return 2;
	}
	
	public String toString() {
		return ("Carrot");
	}
}
