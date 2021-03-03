package com.pi4.mgmtgame.resources;

public class PotatoSeeds extends Grain {
	int growingTime = 1;
	int basePrice = 1;
	static int price;
	int volume;

	int getPrice() {
		return price;
	}

	@Override
	int getGrowingTime() {
		return growingTime;
	}

	@Override
	Plant getGrownPlant() {
		return new Potatoes();
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