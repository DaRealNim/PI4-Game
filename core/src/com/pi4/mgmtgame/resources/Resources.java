package com.pi4.mgmtgame.resources;

import java.io.Serializable;

public abstract class Resources implements Serializable {
	int volume = 0;

	public void addVolume(int v) {
		volume += v;
	}

	public void subVolume(int v) {
		volume -= v;
	}

	public int getVolume() {
		return volume;
	}

	public abstract int getPrice();
	public abstract void addPrice(int p);
	public abstract void subPrice(int p);
	public abstract String getTexture();
	public abstract int getId();
}
