package com.pi4.mgmtgame.resources;

import java.io.Serializable;

public abstract class Grain extends Resources implements Serializable {
	static int basePrice = 1;
	int growingTime = 1;

	public abstract float getGrowingTime();
	public abstract Plant getGrownPlant();
	public abstract int getId();
	public abstract String getFieldSpriteName();
	public abstract String getFieldMiddleSpriteName();
	public abstract String getTexture();
}
