package com.pi4.mgmtgame.resources;
import java.io.Serializable;

public abstract class Item extends Resources implements Serializable {
	public abstract int getPrice();
	public abstract void addPrice(int p);
	public abstract void subPrice(int p);
	public abstract int getId();

}
