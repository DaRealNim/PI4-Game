package com.pi4.mgmtgame;

import com.pi4.mgmtgame.resources.Grain;
import com.pi4.mgmtgame.resources.Plant;

public class Inventory {
	private int money;
	private Plant[] plants;
	private Grain[] seeds;
	
	public Inventory (Plant[] plantArray, Grain[] grainArray) {
		plants = new Plant[100];
		seeds = new Grain[100];
		money = 0;
	}
	
	public void removePlant(int index, int value) {
		this.plants[index].sub(value);
	}
	
	public void addPlant(int index, int value) {
		this.plants[index].add(value);
	}
	
	public void removeGrain(int index, int value) {
		this.plants[index].sub(value);
	}
	
	public void addGrain(int index, int value) {
		this.plants[index].add(value);
	}
	
	public void giveMoney(int value) {
		this.money -= value;
	}
	
	public void receiveMoney(int value) {
		this.money += value;
	}
}
