package com.pi4.mgmtgame.resources;

public class Repulsive extends Item{
static int repulsivePrice = 10;
	
	public int getPrice() {
		return repulsivePrice;
	}

	public void addPrice(int p) {
		repulsivePrice += p;
	}

	public void subPrice(int p) {
		repulsivePrice -= p;
	}

	public int getId() {
		return 1;
	}

	public String getTexture() {
		return null;
	}

	public String toString() {
		return ("repulsive");
	}
}
