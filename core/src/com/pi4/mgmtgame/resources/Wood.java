package com.pi4.mgmtgame.resources;

import com.badlogic.gdx.graphics.Texture;

public class Wood extends Plant {
	static final int basePrice = 1;
	static int woodPrice = 10;

	public int getPrice() {
		return woodPrice;
	}

	public void addPrice(int p) {
		woodPrice += p;
	}

	public void subPrice(int p) {
		woodPrice -= p;
	}

	public int getId() {
		return 3;
	}

	public String getTexture() {
		return("marketRes/wood.png");
	}

	public String toString() {
		return ("Wood");
	}
}
