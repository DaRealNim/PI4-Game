package com.pi4.mgmtgame.resources;

public class FishRod extends Item{
	static int fishRodPrice = 450;

	public int getPrice() {
		return fishRodPrice;
	}

	public void addPrice(int p) {
		fishRodPrice += p;
	}

	public void subPrice(int p) {
		fishRodPrice -= p;
		if (fishRodPrice < 0)
			fishRodPrice = 0;
	}

	public int getId() {
		return 2;
	}

	public String getTexture() {
		return "fishing_icon";
	}

	public String toString() {
		return ("Fishing rod");
	}
}
