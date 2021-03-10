package com.pi4.mgmtgame.resources;

public abstract class Grain extends Resources {
	int id;
	static int basePrice = 1;
	int growingTime = 1;
	
	public abstract int getGrowingTime();
	public abstract Plant getGrownPlant();
	
}
