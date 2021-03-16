package com.pi4.mgmtgame;

import com.pi4.mgmtgame.resources.Carrot;
import com.pi4.mgmtgame.resources.CarrotSeeds;
import com.pi4.mgmtgame.resources.Grain;
import com.pi4.mgmtgame.resources.Plant;
import com.pi4.mgmtgame.resources.Potato;
import com.pi4.mgmtgame.resources.PotatoSeeds;
import com.pi4.mgmtgame.resources.TreeSeeds;
import com.pi4.mgmtgame.resources.Wheat;
import com.pi4.mgmtgame.resources.WheatSeeds;
import com.pi4.mgmtgame.resources.Wood;

public class Inventory {
	private int money;
	private Plant[] plants;
	private Grain[] seeds;

	public Inventory () {
		
		plants = new Plant[10];
		plants[0] = new Wheat();
		plants[1] = new Potato();
		plants[2] = new Carrot();
		plants[3] = new Wood();
		
		seeds = new Grain[10];
		seeds[0] = new WheatSeeds();
		seeds[1] = new PotatoSeeds();
		seeds[2] = new CarrotSeeds();
		seeds[3] = new TreeSeeds();
		
		money = 2000;
		
		for(int i = 0; i < 4; i++)
			seeds[i].addVolume(2);
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
	public boolean hasGrain(int i) {
		return (this.seeds[i].getVolume() > 0);
	}

	public boolean hasPlant(Plant plant) {
		return (this.plants[plant.getId()].getVolume() > 0);
	}
	public boolean hasPlant(int i) {
		return (this.plants[i].getVolume() > 0);
	}

	public Grain[] getSeeds() {
		return seeds;
	}
	public Plant[] getPlants() {
		return plants;
	}

	@Override
	public String toString() {
		return this.money+"$\nWheat seeds: "+this.seeds[0].getVolume()+"\nPotato seeds: "+this.seeds[1].getVolume()+"\nCarrot seeds: "+this.seeds[2].getVolume();
	}
}
