package com.pi4.mgmtgame.resources;

public abstract class Resources {

	abstract int getPrice();

	abstract int getvolume();

	abstract void addVolume(int v);

	abstract void subVolume(int v);

	abstract void addPrice(int p);

	abstract void subPrice(int p);
}