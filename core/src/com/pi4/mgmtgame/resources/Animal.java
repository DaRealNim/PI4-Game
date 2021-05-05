package com.pi4.mgmtgame.resources;

import java.io.Serializable;

public abstract class Animal extends Resources implements Serializable{
	static int basePrice = 1;
	int growingTime = 1;
	int id;

	public abstract void addPrice(int p);
	public abstract void subPrice(int p);
	public abstract int getId();
	public abstract String getTexture();
	public abstract float getGrowingMax();
	public abstract String getPastureSpriteName();
	public abstract String getPastureMiddleSpriteName();
}
