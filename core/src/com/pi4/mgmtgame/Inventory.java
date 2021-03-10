package com.pi4.mgmtgame;

import com.pi4.mgmtgame.resources.Grain;
import com.pi4.mgmtgame.resources.Plant;

public class Inventory {
	private int money;
	private Plant[] plants;
	private Grain[] seeds;
	
	public Inventory () {
		plants = new Plant[10];
		seeds = new Grain[10];
		money = 0;
	}
	
	public Inventory (Plant[] plantArray, Grain[] grainArray, int money) {
		plants = new Plant[10];
		seeds = new Grain[10];
		this.money = money;
	}
	
	
	public void removePlant(int id, int value) {
		this.plants[id].subVolume(value);
	}
	
	public void addPlant(int id, int value) {
		this.plants[id].addVolume(value);
	}
	
	public void removeGrain(int id, int value) {
		this.seeds[id].subVolume(value);
	}
	
	public void addGrain(int id, int value) {
		this.seeds[id].addVolume(value);
	}
	
	public void giveMoney(int value) {
		this.money -= value;
	}
	
	public void receiveMoney(int value) {
		this.money += value;
	}
	
	public int getMoney() {
		return money;
	}

	public boolean hasGrain(Grain seed) {
		return (this.seeds[seed.getId()].getVolume() > 0);
	}
	
	public boolean hasPlant(Plant plant) {
		return (this.plants[plant.getId()].getVolume() > 0);
	}
}
