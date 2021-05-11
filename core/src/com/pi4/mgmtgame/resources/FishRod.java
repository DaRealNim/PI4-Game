package com.pi4.mgmtgame.resources;

public class FishRod extends Item{
	static int fishRodPrice = 10;

	public int getPrice() {
		return fishRodPrice;
	}

	public void addPrice(int p) {
		fishRodPrice += p;
	}

	public void subPrice(int p) {
		fishRodPrice -= p;
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
