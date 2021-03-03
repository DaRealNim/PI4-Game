package com.pi4.mgmtgame.resources;

public class WheatSeeds extends Grain {
	int growingTime = 1;
	static int price = basePrice;

	@Override
	public Plant getGrownPlant() {
		return new Wheat();
	}

	@Override
	public int getGrowingTime() {
		return growingTime;
	}
	public void addPrice(int p) {
		price += p;
	}

	public void subPrice(int p) {
		price -= p;
	}
	
	public int getPrice() {
		return price;
	}

}
