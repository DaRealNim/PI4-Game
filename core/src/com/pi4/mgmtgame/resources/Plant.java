package com.pi4.mgmtgame.resources;

public abstract class Plant extends Resources {
	int id;
	
	public abstract int getPrice();
	public abstract void addPrice(int p);
	public abstract void subPrice(int p);
}
