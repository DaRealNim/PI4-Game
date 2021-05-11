package com.pi4.mgmtgame;

import java.io.Serializable;

import com.pi4.mgmtgame.resources.Animal;
import com.pi4.mgmtgame.resources.Carrot;
import com.pi4.mgmtgame.resources.CarrotSeeds;
import com.pi4.mgmtgame.resources.Cow;
import com.pi4.mgmtgame.resources.Grain;
import com.pi4.mgmtgame.resources.Item;
import com.pi4.mgmtgame.resources.Leather;
import com.pi4.mgmtgame.resources.Meat;
import com.pi4.mgmtgame.resources.Plant;
import com.pi4.mgmtgame.resources.Potato;
import com.pi4.mgmtgame.resources.PotatoSeeds;
import com.pi4.mgmtgame.resources.Product;
import com.pi4.mgmtgame.resources.Resources;
import com.pi4.mgmtgame.resources.Sheep;
import com.pi4.mgmtgame.resources.TreeSeeds;
import com.pi4.mgmtgame.resources.Wheat;
import com.pi4.mgmtgame.resources.WheatSeeds;
import com.pi4.mgmtgame.resources.Wood;
import com.pi4.mgmtgame.resources.Wool;
import com.pi4.mgmtgame.resources.Repulsive;
import com.pi4.mgmtgame.resources.Crickets;
import com.pi4.mgmtgame.resources.Fish;
import com.pi4.mgmtgame.resources.FishRod;

public class Inventory implements Serializable {
	private int money;
	private Plant[] plants;
	private Grain[] seeds;
	private Item[] items;
	private Animal[] animal;
	private Product[] product;
	private Resources[] resArray;
	private int invID;
	public int rodDurability=10;
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

		for(int i = 0; i < 4; i++)
			seeds[i].addVolume(2);

		items = new Item[3];
		items[0] = new Crickets();
		items[1] = new Repulsive();
		items[2] = new FishRod();
		addItem(2,1);

		animal = new Animal[2];
		animal[0] = new Cow();
		animal[1] = new Sheep();

		product = new Product[4];
		product[0] = new Meat();
		product[1] = new Leather();
		product[2] = new Wool();
		product[3] = new Fish();

		resArray = new Resources[getPlants().length + getSeeds().length + getItems().length + getAnimals().length + getProduct().length];
		int c = 0;
		System.arraycopy(getPlants(), 0, resArray, c, getPlants().length);
		c += getPlants().length;
		System.arraycopy(getSeeds(), 0, resArray, c, getSeeds().length);
		c += getSeeds().length;
		System.arraycopy(getItems(), 0, resArray, c, getItems().length);
		c += getItems().length;
		System.arraycopy(getAnimals(), 0, resArray, c, getAnimals().length);
		c += getAnimals().length;
		System.arraycopy(getProduct(), 0, resArray, c, getProduct().length);

		money = 5000;
	}

	public Inventory (Plant[] plantArray, Grain[] grainArray, Animal[] animalArray, Product[] productArray, int money) {
		plants = new Plant[10];
		seeds = new Grain[10];
		animal = new Animal[10];
		product = new Product[10];
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

	public void removeAnimal(int id, int value) {
		this.animal[id].subVolume(value);
	}

	public void addAnimal(int id, int value) {
		this.animal[id].addVolume(value);
	}

	public void removeProduct(int id, int value) {
		this.product[id].subVolume(value);
	}

	public void addProduct(int id, int value) {
		this.product[id].addVolume(value);
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
	public boolean hasAnimal(int i) {
		return (this.animal[i].getVolume() > 0);
	}
	public boolean hasAnimal(Animal animal) {
		return (this.animal[animal.getId()].getVolume() > 0);
	}
	public boolean hasProduct(int i) {
		return (this.product[i].getVolume() > 0);
	}
	public boolean hasProduct(Animal animal) {
		return (this.product[animal.getId()].getVolume() > 0);
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
	public Animal[] getAnimals() {
		return animal;
	}
	public Product[] getProduct() {
		return product;
	}
	public Resources[] getRessources() {
		return resArray;
	}
	public int getinvID() {
		return invID;
	}
	public void useRod(int nb) {
		rodDurability -= nb;
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
		for (int i=0; i<2; i++) {
			ret += animal[i] + ": " + animal[i].getVolume() + "\n";
		}
		for (int i=0; i<3; i++) {
			ret += product[i] + ": " + product[i].getVolume() + "\n";
		}
		return ret;
	}



}
