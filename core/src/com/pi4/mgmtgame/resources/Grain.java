package com.pi4.mgmtgame.resources;

public abstract class Grain extends Resources {
	String price;
	int inventoryVolume = 1;
	int getInventoryVolume() {
		return inventoryVolume;
	}
	abstract int getPrice();

	abstract int getGrowingTime();

	abstract Plant getGrownPlant();
	

}
