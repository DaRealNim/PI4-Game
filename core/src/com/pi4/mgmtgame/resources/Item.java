package com.pi4.mgmtgame.resources;

public abstract class Item extends Resources{
	public abstract int getPrice();
	public abstract void addPrice(int p);
	public abstract void subPrice(int p);
	public abstract int getId();

}
