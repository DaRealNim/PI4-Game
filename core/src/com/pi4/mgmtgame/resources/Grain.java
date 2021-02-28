package com.pi4.mgmtgame.resources;

public abstract class Grain extends Resources {
	String price;
	int inventoryVolume = 1;
	int getInventoryVolume() {
		return inventoryVolume;
	}
	public abstract int getPrice();

	public abstract int getGrowingTime();

	public abstract Plant getGrownPlant();


}
