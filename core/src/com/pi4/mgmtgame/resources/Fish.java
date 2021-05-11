package com.pi4.mgmtgame.resources;

public class Fish extends Product{
	static final int basePrice = 1;
	static int fishPrice = 50;

	@Override
	public int getPrice() {
		return fishPrice;
	}

	public void addPrice(int p) {
		fishPrice += p;
	}

	public void subPrice(int p) {
		fishPrice -= p;
	}

	public int getId() {
		return 3;
	}

	public String getTexture() {
		return("fish_icon");
	}

	public String toString() {
		return ("Fish");
	}
}
