package com.pi4.mgmtgame.resources;

public class Wheat extends Plant {
	int basePrice = 1;
	int volume = 1;
	static int price;

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
