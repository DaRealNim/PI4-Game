package com.pi4.mgmtgame.resources;

public abstract class Grain extends Resources {
	static int basePrice = 1;
	int growingTime = 1;

	public abstract int getGrowingTime();
	public abstract Plant getGrownPlant();
	public abstract int getId();
	public abstract String getFieldSpriteName();
	public abstract String getTexture();
}
