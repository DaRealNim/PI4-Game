package com.pi4.mgmtgame.resources;

public class Meat extends Product{
	static final int basePrice = 1;
	static int meatPrice = 100;

	@Override
	public int getPrice() {
		return meatPrice;
	}

	public void addPrice(int p) {
		meatPrice += p;
	}

	public void subPrice(int p) {
		meatPrice -= p;
		if (meatPrice < 0)
			meatPrice = 0;
	}

	public int getId() {
		return 0;
	}

	public String getTexture() {
		return("meat_icon");
	}

	public String toString() {
		return ("Meat");
	}

}
