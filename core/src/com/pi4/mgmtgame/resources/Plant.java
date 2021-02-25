package com.pi4.mgmtgame.resources;

public abstract class Plant extends Resources{

	@Override
	abstract int getPrice();

	@Override
	abstract int getInventoryVolume();
	
}
