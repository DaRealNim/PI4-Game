package com.pi4.mgmtgame;

import java.io.Serializable;

import com.pi4.mgmtgame.resources.Carrot;
import com.pi4.mgmtgame.resources.CarrotSeeds;
import com.pi4.mgmtgame.resources.Grain;
import com.pi4.mgmtgame.resources.Item;
import com.pi4.mgmtgame.resources.Plant;
import com.pi4.mgmtgame.resources.Potato;
import com.pi4.mgmtgame.resources.PotatoSeeds;
import com.pi4.mgmtgame.resources.Resources;
import com.pi4.mgmtgame.resources.TreeSeeds;
import com.pi4.mgmtgame.resources.Wheat;
import com.pi4.mgmtgame.resources.WheatSeeds;
import com.pi4.mgmtgame.resources.Wood;
import com.pi4.mgmtgame.resources.Repulsive;
import com.pi4.mgmtgame.resources.Crickets;

public class Inventory implements Serializable {
	private int money;
	private Plant[] plants;
	private Grain[] seeds;
	private Item[] items;
	private int invID;
	public Inventory (int x) {
		invID = x;
		plants = new Plant[4];
		plants[0] = new Wheat();
		plants[1] = new Potato();
		plants[2] = new Carrot();
		plants[3] = new Wood();

		seeds = new Grain[4];
		seeds[0] = new WheatSeeds();
		seeds[1] = new PotatoSeeds();
		seeds[2] = new CarrotSeeds();
		seeds[3] = new TreeSeeds();

		items = new Item[2];
		items[0] = new Crickets();
		items[1] = new Repulsive();

		items[0].addVolume(1);
		items[1].addVolume(1);

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

	public void removeItem(int id, int value) {
		this.items[id].subVolume(value);
	}

	public void addItem(int id, int value) {
		this.items[id].addVolume(value);
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
	public boolean hasItem(int i) {
		return (this.items[i].getVolume() > 0);
	}
	public boolean hasItem(Item item) {
		return (this.items[item.getId()].getVolume() > 0);
	}
	public Grain[] getSeeds() {
		return seeds;
	}
	public Plant[] getPlants() {
		return plants;
	}
	public Item[] getItems() {
		return items;
	}
	public int getinvID() {
		return invID;
	}


	@Override
	public String toString() {
		String ret = this.money+"$\n";
		for (int i=0; i<4; i++) {
			ret += seeds[i] + ": " + seeds[i].getVolume() + "\n";
		}
		for (int i=0; i<4; i++) {
			ret += plants[i] + ": " + plants[i].getVolume() + "\n";
		}
		for (int i=0; i<2; i++) {
			ret += items[i] + ": " + items[i].getVolume() + "\n";
		}
		return ret;
	}
}
