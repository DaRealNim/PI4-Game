package com.pi4.mgmtgame.resources;

public abstract class Resources {
	int price;
	int timeToGrow; // maybe make it a method with the season as argument ?

	abstract int getPrice();

	abstract int getInventoryVolume();
}