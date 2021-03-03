package com.pi4.mgmtgame.resources;

public class WheatSeeds extends Grain {
	int growingTime = 1;
	int basePrice = 1;
	static int price;
	int volume;

	@Override
	int getGrowingTime() {
		return growingTime;
	}

	@Override
	Plant getGrownPlant() {
		return new Wheat();
	}
	
	int getPrice() {
		return price;
	}


	void addVolume(int v) {
		volume += v;
	}

	void subVolume(int v) {
		volume -= v;
	}

	void addPrice(int p) {
		price += p;
	}

	void subPrice(int p) {
		price -= p;
	}

	@Override
	int getvolume() {
		return volume;
	}
}
