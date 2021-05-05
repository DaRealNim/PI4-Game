package com.pi4.mgmtgame.resources;

public class Cow extends Animal{
	static final int basePrice = 1;
	static int cowPrice = 750;
	static int wheatConsumed = 10;
	
	public Product getAnimalProduct() {
		return new Leather();
	}
	
	public float getGrowingMax() {
		return 5;
	}
	
	public int getWheatConsumed() {
		return wheatConsumed;
	}

	public void addPrice(int p) {
		cowPrice += p;
	}

	public void subPrice(int p) {
		cowPrice -= p;
	}
	
	public int getPrice() {
		return cowPrice;
	}

	public int getId() {
		return 0;
	}

	public String getTexture() {
		return("marketRes/cow.png");
	}
	
	public String getPastureSpriteName() {
		return "pasture_cow_full";
	}

	public String getPastureMiddleSpriteName() {
		return "pasture_cow_growing";
	}

	public String toString() {
		return ("cow");
	}	
}
