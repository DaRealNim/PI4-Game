package com.pi4.mgmtgame.resources;

public class Wool extends Product{
	static final int basePrice = 1;
	static int woolPrice = 50;

	@Override
	public int getPrice() {
		return woolPrice;
	}

	public void addPrice(int p) {
		woolPrice += p;
	}

	public void subPrice(int p) {
		woolPrice -= p;
	}

	public int getId() {
		return 2;
	}

	public String getTexture() {
		return("wool_icon");
	}

	public String toString() {
		return ("Wool");
	}
}
