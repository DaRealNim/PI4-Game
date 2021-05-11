package com.pi4.mgmtgame.resources;

public class Crickets extends Item{
	static int cricketsPrice = 10;

	public int getPrice() {
		return cricketsPrice;
	}

	public void addPrice(int p) {
		cricketsPrice += p;
	}

	public void subPrice(int p) {
		cricketsPrice -= p;
	}

	public int getId() {
		return 0;
	}

	public String getTexture() {
		return "grasshopper_icon";
	}

	public String toString() {
		return ("Tub of grasshoppers");
	}
}
