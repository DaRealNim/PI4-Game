package com.pi4.mgmtgame.networking;

import java.io.*;
import java.net.*;
import java.util.Random;

import com.badlogic.gdx.assets.AssetManager;
import com.pi4.mgmtgame.Inventory;
import com.pi4.mgmtgame.Map;
import com.pi4.mgmtgame.blocks.*;
import com.pi4.mgmtgame.resources.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.pi4.mgmtgame.blocks.TreeField;
import com.pi4.mgmtgame.bot.Bot;
import java.util.HashMap;
import java.awt.Color;

public class Server {
	private ServerSocket serverSocket;
	private int numPlayers;
	private ServerSide[] players;
	private Bot[] bots;
	private int botID;
	private Map map;
	private Inventory[] invArray;
	private boolean[] hasPlayerLost;
	private Inventory inv;
	private int turn;
	private int internalTurn;
	private int currentPlayer;
	private int nbOfPlayers;
	private int nbOfBots;
	private int playerID;
	private int remainingPlayers;
	private HashMap<Integer, Color> idToColorMap;
	private volatile boolean gameCanStart = false;

	public Server(int port, int nbOfPlayers, int nbOfBots) {
		System.out.println("----Server----");
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Exception in Server constructor.");
		}

		this.map = new Map(50, 50, null, null);
		this.nbOfPlayers = nbOfPlayers;
		this.remainingPlayers = nbOfPlayers;
		this.nbOfBots = nbOfBots;
		this.invArray = new Inventory[nbOfPlayers];
		for (int i = 0; i < nbOfPlayers; i++) {
			this.invArray[i] = new Inventory(i);
		}
		this.players = new ServerSide[nbOfPlayers];
		this.numPlayers = 0;
		this.turn = 0;
		this.internalTurn = -1;
		this.inv = invArray[0];
		this.idToColorMap = new HashMap<Integer, Color>();

		this.bots = new Bot[nbOfBots];
		this.botID = nbOfPlayers;
		for (int i = 0; i < nbOfBots; i++) {
			this.bots[i] = createBot();
		}

		this.hasPlayerLost = new boolean[nbOfPlayers+nbOfBots];

		for (int i = 0; i < nbOfPlayers + nbOfBots; i++) {
			// float r = Math.round(Math.random() * 100.0f) / 100.0f;
			// float g = Math.round(Math.random() * 100.0f) / 100.0f;
			// float b = Math.round(Math.random() * 100.0f) / 100.0f;
			Color col = Color.getHSBColor(Map.rand_range(0, 255) / 255.0f, Map.rand_range(50, 255) / 255.0f, 1.0f);
			idToColorMap.put(i, col);
		}
	}

	public void acceptConnections() {
		try {
			System.out.println("Waiting on conns...");

			while (numPlayers < nbOfPlayers) {
				Socket playerSocket = serverSocket.accept();
				System.out.println("Player " + numPlayers + " connected");

				ServerSide serverSideConnection = new ServerSide(playerSocket, numPlayers);
				this.players[numPlayers] = serverSideConnection;

				Thread t = new Thread(serverSideConnection);
				t.start();
				++numPlayers;
			}
			System.out.println("Game will now start.");
			gameCanStart = true;
			internalTurn = 0;
			serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e + "\nexception on acceptConns");
		}
	}

	private class ServerSide implements Runnable {
		private Socket playerSocket;
		private int playerID;

		protected DataInputStream dataIn;
		protected DataOutputStream dataOut;
		protected ObjectInputStream objIn;
		protected ObjectOutputStream objOut;

		public ServerSide(Socket s, int id) {
			playerSocket = s;
			playerID = id;
			try {
				dataOut = new DataOutputStream(playerSocket.getOutputStream());
				dataIn = new DataInputStream(playerSocket.getInputStream());
				objOut = new ObjectOutputStream(playerSocket.getOutputStream());
				objIn = new ObjectInputStream(playerSocket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e + "\nexception on ServerSide constructor");
			}
		}

		@Override
		public void run() {
			try {
				dataOut.writeInt(playerID);
				dataOut.flush();

				int[] hqcoords = placeHQ();
				HQ hq = new HQ(hqcoords[0], hqcoords[1]);
				hq.setOwnerID(playerID);
				map.setStructAt(hqcoords[0], hqcoords[1], hq);

				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						Environment env = map.getEnvironmentAt(hqcoords[0] + i, hqcoords[1] + j);
						Structure struct = map.getStructAt(hqcoords[0] + i, hqcoords[1] + j);
						if (env != null)
							env.setOwnerID(playerID);
						if (struct != null)
							struct.setOwnerID(playerID);
					}
				}

				dataOut.writeInt(hqcoords[0]);
				dataOut.flush();
				dataOut.writeInt(hqcoords[1]);
				dataOut.flush();

				objOut.writeObject(idToColorMap);
				objOut.flush();

				while (true) {
					int x = 0;
					int y = 0;
					int quantity = 0;
					Structure struct = null;
					Grain grain = null;
					Plant plant = null;
					int animalID;
					Item item = null;
					Animal animal = null;
					Product product = null;
					int request = -1;
					try {
						request = dataIn.readInt();
					} catch (SocketException | EOFException e) {
						System.out.println("Player " + playerID + " disconnected");
						players[playerID] = null;
						if (!hasPlayerLost[playerID])
							remainingPlayers--;
						if (remainingPlayers == 0) {
							System.out.println("No player remaining, closing server...");
							System.exit(0);
						}
						if (internalTurn == playerID)
							passTurn();
						break;
					}

					switch (request) {
					case 0:
						objOut.reset();
						objOut.writeObject(getMap());
						objOut.flush();
						break;
					case 1:
						dataOut.writeInt(getCurrentPlayer());
						dataOut.flush();
						break;
					case 2:
						objOut.reset();
						objOut.writeObject(getInventory(playerID));
						objOut.flush();
						break;
					case 3:
						dataOut.writeInt(getTurn());
						dataOut.flush();
						break;
					case 4:
						dataOut.writeInt(getInternalTurn());
						dataOut.flush();
						break;
					case 5:
						x = dataIn.readInt();
						y = dataIn.readInt();
						struct = (Structure) objIn.readObject();
						dataOut.writeBoolean(canBuildStructure(x, y, struct));
						dataOut.flush();
						break;
					case 6:
						x = dataIn.readInt();
						y = dataIn.readInt();
						struct = (Structure) objIn.readObject();
						if (internalTurn != playerID) {
							dataOut.writeBoolean(false);
							dataOut.flush();
							break;
						}
						dataOut.writeBoolean(requestBuildStructure(x, y, struct));
						dataOut.flush();
						break;
					case 7:
						x = dataIn.readInt();
						y = dataIn.readInt();
						dataOut.writeBoolean(canDestroyStructure(x, y));
						dataOut.flush();
						break;
					case 8:
						x = dataIn.readInt();
						y = dataIn.readInt();
						grain = (Grain) objIn.readObject();
						if (internalTurn != playerID) {
							dataOut.writeBoolean(false);
							dataOut.flush();
							break;
						}
						dataOut.writeBoolean(requestPlantSeed(x, y, grain));
						dataOut.flush();
						break;
					case 9:
						x = dataIn.readInt();
						y = dataIn.readInt();
						if (internalTurn != playerID) {
							dataOut.writeBoolean(false);
							dataOut.flush();
							break;
						}
						dataOut.writeBoolean(requestDestroyStructure(x, y));
						dataOut.flush();
						break;
					case 10:
						x = dataIn.readInt();
						y = dataIn.readInt();
						dataOut.writeBoolean(canHarvest(x, y));
						dataOut.flush();
						break;
					case 11:
						x = dataIn.readInt();
						y = dataIn.readInt();
						if (internalTurn != playerID) {
							dataOut.writeBoolean(false);
							dataOut.flush();
							break;
						}
						dataOut.writeBoolean(requestHarvest(x, y));
						dataOut.flush();
						break;
					case 12:
						if (internalTurn != playerID)
							break;
						passTurn();
						break;
					case 13:
						grain = (Grain) objIn.readObject();
						quantity = dataIn.readInt();
						if (internalTurn != playerID)
							break;
						buyGrain(grain, quantity);
						break;
					case 14:
						grain = (Grain) objIn.readObject();
						quantity = dataIn.readInt();
						if (internalTurn != playerID)
							break;
						sellGrain(grain, quantity);
						break;
					case 15:
						plant = (Plant) objIn.readObject();
						quantity = dataIn.readInt();
						if (internalTurn != playerID)
							break;
						buyPlant(plant, quantity);
						break;
					case 16:
						plant = (Plant) objIn.readObject();
						quantity = dataIn.readInt();
						if (internalTurn != playerID)
							break;
						sellPlant(plant, quantity);
						break;
					case 17:
						x = dataIn.readInt();
						y = dataIn.readInt();
						item = (Item) objIn.readObject();
						if (internalTurn != playerID) {
							break;
						}
						requestUseItem(x, y, item);
						break;
					case 18:
						x = dataIn.readInt();
						y = dataIn.readInt();
						dataOut.writeBoolean(canBuyTerrain(x, y));
						dataOut.flush();
						break;
					case 19:
						x = dataIn.readInt();
						y = dataIn.readInt();
						if (internalTurn != playerID) {
							dataOut.writeBoolean(false);
							dataOut.flush();
							break;
						}
						dataOut.writeBoolean(requestBuyTerrain(x, y));
						dataOut.flush();
						break;
					case 20:
						Resources res = (Resources) objIn.readObject();
						dataOut.writeInt(res.getPrice());
						dataOut.flush();
						break;
					case 21:
						x = dataIn.readInt();
						y = dataIn.readInt();
						animalID = dataIn.readInt();
						if (internalTurn != playerID) {
							dataOut.writeBoolean(false);
							dataOut.flush();
							break;
						}
						dataOut.writeBoolean(requestBreed(x, y, animalID));
						dataOut.flush();
						break;
					case 22:
						x = dataIn.readInt();
						y = dataIn.readInt();
						if (internalTurn != playerID) {
							dataOut.writeBoolean(false);
							dataOut.flush();
							break;
						}
						dataOut.writeBoolean(canFish(x, y));
						dataOut.flush();
						break;
					case 23:
						x = dataIn.readInt();
						y = dataIn.readInt();
						if (internalTurn != playerID) {
							break;
						}
						tryToFish(x, y);
						break;
					case 24:
						if (internalTurn != playerID) {
							break;
						}
						fixRod();
						break;
					case 25:
						if (internalTurn != playerID) {
							dataOut.writeBoolean(false);
							dataOut.flush();
							break;
						}
						dataOut.writeBoolean(testRod());
						dataOut.flush();
						break;
					case 27:
						item = (Item) objIn.readObject();
						quantity = dataIn.readInt();
						if (internalTurn != playerID)
							break;
						buyItem(item, quantity);
						break;
					case 28:
						item = (Item) objIn.readObject();
						quantity = dataIn.readInt();
						if (internalTurn != playerID)
							break;
						sellItem(item, quantity);
						break;
					case 29:
						animal = (Animal) objIn.readObject();
						quantity = dataIn.readInt();
						if (internalTurn != playerID)
							break;
						buyAnimal(animal, quantity);
						break;
					case 30:
						animal = (Animal) objIn.readObject();
						quantity = dataIn.readInt();
						if (internalTurn != playerID)
							break;
						sellAnimal(animal, quantity);
						break;
					case 31:
						product = (Product) objIn.readObject();
						quantity = dataIn.readInt();
						if (internalTurn != playerID)
							break;
						buyProduct(product, quantity);
						break;
					case 32:
						product = (Product) objIn.readObject();
						quantity = dataIn.readInt();
						if (internalTurn != playerID)
							break;
						sellProduct(product, quantity);
						break;
					case 256:
						dataOut.writeBoolean(gameCanStart);
						dataOut.flush();
						break;
					case 257:
						dataOut.writeBoolean(hasPlayerLost[playerID]);
						dataOut.flush();
						break;
					default:
						System.out.println("wyd????  "+request+" ");
						break;
					}
				}
			} catch (Exception e) {
				System.out.println(e + "\nexception on ServerSide runnable");
				e.printStackTrace();
			}
		}
	}

	public int getCurrentPlayer() {
		return (currentPlayer);
	}

	public Map getMap() {
		return map;
	}

	public Inventory getInventory(int id) {
		if (id >= 0 && id < invArray.length) {
			return invArray[id];
		} else {
			return null;
		}
	}

	public Inventory getInventory() {
		return inv;
	}

	public int getTurn() {
		return turn;
	}

	public int getInternalTurn() {
		return internalTurn % nbOfPlayers;
	}

	public boolean canBuildStructure(int x, int y, Structure struct) {
		Environment envBlock = map.getEnvironmentAt(x, y);
		return (envBlock.canBuild(struct) && struct.canBuild(getInventory())
				&& inv.getMoney() >= struct.getConstructionCost() && envBlock != null
				&& envBlock.testOwner(internalTurn));
	}

	public boolean canFish(int x, int y) {
		Environment env = getMap().getEnvironmentAt(x, y);
		return (env != null && testRod() && env instanceof Lake && ((Lake)env).canFish());
	}

	public void tryToFish(int x, int y) {
		Environment env = getMap().getEnvironmentAt(x, y);
		if (canFish(x, y)) {
			Random random = new Random();
			int nb = random.nextInt(6) - 3;
		   	if (nb < 0)
				nb = 0;
			inv.useRod(nb);
			inv.addProduct(3, nb);
			((Lake)env).fish();
		}
	}

	public boolean requestBuildStructure(int x, int y, Structure struct) {
		if (canBuildStructure(x, y, struct)) {
			map.setStructAt(x, y, struct);
			inv.giveMoney(struct.getConstructionCost());
			struct.doBuild(getInventory());
			return (true);
		}
		return (false);
	}

	public boolean canDestroyStructure(int x, int y) {
		Structure structBlock = map.getStructAt(x, y);
		return (structBlock != null && structBlock.testOwner(currentPlayer));
	}

	public boolean requestPlantSeed(int x, int y, Grain seed) {
		Structure structBlock = map.getStructAt(x, y);
		// System.out.println("Seed id: " + seed.getId());
		if (structBlock instanceof Field && inv.hasGrain(seed) && structBlock != null
				&& structBlock.testOwner(currentPlayer)) {
			if (!((Field) structBlock).hasSeed()) {
				((Field) structBlock).plantSeed(seed);
				inv.removeGrain(seed.getId(), 1);
				return (true);
			}
		}

		return (false);
	}

	public boolean requestUseItem(int x, int y, Item item) {
		Structure structBlock = map.getStructAt(x, y);
		// System.out.println("Item id: " + item.getId());
		if (structBlock instanceof Field && inv.hasItem(item)) {
			if ((!((Field) structBlock).hasItem() && item.getId() == 0)
					|| ((Field) structBlock).hasItem() && item.getId() == 1) {
				((Field) structBlock).UseItem(item);
				inv.removeItem(item.getId(), 1);
				return (true);
			}

		}
		return (false);
	}

	public boolean requestDestroyStructure(int x, int y) {
		if (canDestroyStructure(x, y)) {
			inv.receiveMoney((map.getStructAt(x, y).getConstructionCost() * 30) / 100);
			if (map.getStructAt(x, y) != null)
				map.getStructAt(x, y).remove();
			map.setStructAt(x, y, null);

			return (true);
		}

		return (false);
	}

	public boolean canHarvest(int x, int y) {
		Structure structBlock = map.getStructAt(x, y);
		if (structBlock instanceof Field && structBlock != null && structBlock.testOwner(currentPlayer)) {
			Field fieldBlock = (Field) structBlock;
			return (fieldBlock.hasSeedGrown());
		} else
			return false;
	}

	public boolean requestHarvest(int x, int y) {
		Structure structBlock = map.getStructAt(x, y);
		Plant harvested;
		Field fieldBlock;

		if (canHarvest(x, y)) {
			fieldBlock = (Field) structBlock;
			harvested = fieldBlock.harvest();
			harvested.addVolume(4);
			inv.addPlant(harvested.getId(), harvested.getVolume());
			return (true);
		}
		return (false);
	}

	public void passTurn() {
		int mapWidth = map.getMapWidth();
		int mapHeight = map.getMapHeight();
		int widthIndex;
		int heightIndex;
		Block currBlock;
		internalTurn++;
		if (internalTurn == nbOfPlayers) {
			for (Bot bot : bots) {
				if (bot != null) {
					if (!hasPlayerLost[bot.getBotID()]) {
						inv = bot.getInventory();
						bot.play();
						internalTurn++;
					}
				}
			}
			turn++;

			internalTurn = 0;
			for (heightIndex = 0; heightIndex < mapHeight; heightIndex++) {
				for (widthIndex = 0; widthIndex < mapWidth; widthIndex++) {
					currBlock = map.getEnvironmentAt(heightIndex, widthIndex);
					if (currBlock != null)
						currBlock.passTurn(getInventory(currBlock.getOwnerID()));

					currBlock = map.getStructAt(heightIndex, widthIndex);
					if (currBlock != null)
						currBlock.passTurn(getInventory(currBlock.getOwnerID()));
				}
			}

			for(Plant p : getInventory().getPlants()) {
				if (p.getPrice() < 400)
					p.addPrice(Map.rand_range(5, 15));
			}
			for(Product p : getInventory().getProduct()) {
				if (p.getPrice() < 700)
					p.addPrice(Map.rand_range(5, 20));
			}
			for(Grain p : getInventory().getSeeds()) {
				if (p.getPrice() > 10)
					p.subPrice(Map.rand_range(0, 15));
			}

			for(int i=0; i<invArray.length; i++) {
				if (!hasPlayerLost[i] && invArray[i].getMoney() < 0) {
					hasPlayerLost[i] = true;
					remainingPlayers--;
				}
			}
			for(int i=0; i<bots.length; i++) {
				if (bots[i].getInventory().getMoney() < 0) {
					hasPlayerLost[bots[i].getBotID()] = true;
				}
			}
			System.out.println(java.util.Arrays.toString(hasPlayerLost));
		}
		currentPlayer = internalTurn % nbOfPlayers;
		if (players[currentPlayer] == null || (hasPlayerLost[currentPlayer] && nbOfPlayers > 1))
			passTurn();
		inv = invArray[internalTurn % nbOfPlayers];

		System.out.println("Turn " + turn + "\n=======================");
		System.out.println(internalTurn);
		System.out.println(currentPlayer);
	}

	public boolean userHasMoneyToBuy(int q, Resources r) {
		return (getInventory().getMoney() >= r.getPrice() * q);
	}

	public boolean userCanSellPlant(int q, Plant p) {
		return (getInventory().getPlants()[p.getId()].getVolume() >= q);
	}

	public boolean userCanSellGrain(int q, Grain g) {
		return (getInventory().getSeeds()[g.getId()].getVolume() >= q);
	}

	public boolean userCanSellItem(int q, Item i) {
		return (getInventory().getItems()[i.getId()].getVolume() >= q);
	}

	public boolean userCanSellAnimal(int q, Animal a) {
		return (getInventory().getAnimals()[a.getId()].getVolume() >= q);
	}

	public boolean userCanSellProduct(int q, Product p) {
		return (getInventory().getProduct()[p.getId()].getVolume() >= q);
	}

	public void buyGrain(Grain boughtGrain, int q) {
		Inventory userInv = getInventory();
		int grainPrice = boughtGrain.getPrice();

		if (userHasMoneyToBuy(q, boughtGrain)) {
			userInv.giveMoney(grainPrice * q);
			userInv.addGrain(boughtGrain.getId(), q);
			if (boughtGrain.getPrice() < 400)
				boughtGrain.addPrice(Map.rand_range(1, 3));
			// System.out.println(boughtGrain.toString() + " price: " +
			// boughtGrain.getPrice());
		}
	}

	public void sellGrain(Grain soldGrain, int q) {
		Inventory userInv = getInventory();
		int grainPrice = soldGrain.getPrice();

		if (userCanSellGrain(q, soldGrain)) {
			soldGrain.subPrice(Map.rand_range(1, 3));
			grainPrice = soldGrain.getPrice();
			userInv.receiveMoney(grainPrice * q);
			userInv.removeGrain(soldGrain.getId(), q);
			// System.out.println(soldGrain.toString() + " price: " + soldGrain.getPrice());
		}
	}

	public void buyPlant(Plant boughtPlant, int q) {
		Inventory userInv = getInventory();
		int plantPrice = boughtPlant.getPrice();

		if (userHasMoneyToBuy(q, boughtPlant)) {
			userInv.giveMoney(plantPrice * q);
			userInv.addPlant(boughtPlant.getId(), q);
			boughtPlant.addPrice(Map.rand_range(1, 3));
			// System.out.println(boughtPlant.toString() + " price: " +
			// boughtPlant.getPrice());
		}
	}

	public void sellPlant(Plant soldPlant, int q) {
		Inventory userInv = getInventory();
		int plantPrice = soldPlant.getPrice();

		if (userCanSellPlant(q, soldPlant)) {
			soldPlant.subPrice(Map.rand_range(1, 5));
			plantPrice = soldPlant.getPrice();
			userInv.receiveMoney(plantPrice * q);
			userInv.removePlant(soldPlant.getId(), q);
			// System.out.println(soldPlant.toString() + " price: " + soldPlant.getPrice());
		}
	}

	public boolean canBuyTerrain(int x, int y) {
		Inventory userInv = getInventory();
		Environment terrainEnv = map.getEnvironmentAt(x, y);
		Structure terrainStruct = map.getStructAt(x, y);
		int terrainPrice = terrainEnv.getPrice();
		int availableMoney = userInv.getMoney();

		Environment envLeft = map.getEnvironmentAt(x - 1, y);
		Environment envRight = map.getEnvironmentAt(x + 1, y);
		Environment envUp = map.getEnvironmentAt(x, y + 1);
		Environment envDown = map.getEnvironmentAt(x, y - 1);
		boolean ownLeft = false;
		boolean ownRight = false;
		boolean ownUp = false;
		boolean ownDown = false;

		if (envLeft != null)
			ownLeft = envLeft.testOwner(currentPlayer);
		if (envRight != null)
			ownRight = envRight.testOwner(currentPlayer);
		if (envUp != null)
			ownUp = envUp.testOwner(currentPlayer);
		if (envDown != null)
			ownDown = envDown.testOwner(currentPlayer);

		if (terrainEnv != null && terrainEnv.testOwner(-1) && availableMoney >= terrainPrice
				&& (ownUp || ownDown || ownLeft || ownRight))
			return true;
		return false;
	}

	public boolean requestBuyTerrain(int x, int y) {
		Inventory userInv = getInventory();
		Environment env = map.getEnvironmentAt(x, y);
		Structure struct = map.getStructAt(x, y);
		int terrainPrice = env.getPrice();

		if (canBuyTerrain(x, y)) {
			userInv.giveMoney(terrainPrice);
			if (env != null)
				env.setOwnerID(internalTurn);
			if (struct != null)
				struct.setOwnerID(internalTurn);
			return true;
		}

		return false;
	}

	public int[] placeHQ() {
		int x;
		int y;
		Random random = new Random();
		int height = map.getMapHeight();
		int width = map.getMapWidth();
		while (true) {
			boolean ok = true;
			x = Map.rand_range(1, width-2);
			y = Map.rand_range(1, width-2);
			if (!(map.getEnvironmentAt(x, y) instanceof Plain))
				ok = false;
			for (int i = -5; i < 5; i++) {
				for (int j = -5; j < 5; j++) {
					if (map.getStructAt(x + i, y + j) instanceof HQ)
						ok = false;
				}
			}
			if (ok)
				break;
		}
		int[] v = { x, y };
		return v;
	}

	public boolean canBreed(int x, int y, int animalID) {
		// pour benj
		Structure struct = map.getStructAt(x, y);
		if (struct instanceof Pasture) {
			return getInventory().hasAnimal(animalID) && !((Pasture) struct).hasAnimal()
					&& struct.testOwner(currentPlayer);
		}
		return false;
	}

	public boolean requestBreed(int x, int y, int animalID) {
		// pour benj
		if (canBreed(x, y, animalID)) {
			Pasture pasture = (Pasture) map.getStructAt(x, y);
			pasture.breedAnimal(getInventory().getAnimals()[animalID]);
			getInventory().removeAnimal(animalID, 1);
			return true;
		}
		return false;
	}

	public void buyItem(Item boughtItem, int q) {
		Inventory userInv = getInventory();
		int price = boughtItem.getPrice();

		if (userHasMoneyToBuy(q, boughtItem)) {
			userInv.giveMoney(price * q);
			userInv.addItem(boughtItem.getId(), q);
			if (boughtItem instanceof FishRod) {
				userInv.rodDurability = 10;
			}
			boughtItem.addPrice(1);
		}
	}

	public void sellItem(Item sellItem, int q) {
		Inventory userInv = getInventory();
		int price = sellItem.getPrice();

		if (userCanSellItem(q, sellItem)) {
			sellItem.subPrice(1);
			price = sellItem.getPrice();
			userInv.receiveMoney(price * q);
			userInv.removeItem(sellItem.getId(), q);
		}
	}

	public void buyAnimal(Animal boughtAnimal, int q) {
		Inventory userInv = getInventory();
		int price = boughtAnimal.getPrice();

		if (userHasMoneyToBuy(q, boughtAnimal)) {
			userInv.giveMoney(price * q);
			userInv.addAnimal(boughtAnimal.getId(), q);
			boughtAnimal.addPrice(Map.rand_range(5, 10));
		}
	}

	public void sellAnimal(Animal soldAnimal, int q) {
		Inventory userInv = getInventory();
		int price = soldAnimal.getPrice();

		if (userCanSellAnimal(q, soldAnimal)) {
			soldAnimal.subPrice(Map.rand_range(5, 10));
			price = soldAnimal.getPrice();
			userInv.receiveMoney(price * q);
			userInv.removeAnimal(soldAnimal.getId(), q);
		}
	}

	public void buyProduct(Product boughtProduct, int q) {
		Inventory userInv = getInventory();
		int price = boughtProduct.getPrice();

		if (userHasMoneyToBuy(q, boughtProduct)) {
			userInv.giveMoney(price * q);
			userInv.addProduct(boughtProduct.getId(), q);
			boughtProduct.addPrice(Map.rand_range(1, 3));
		}
	}

	public void sellProduct(Product soldProduct, int q) {
		Inventory userInv = getInventory();
		int price = soldProduct.getPrice();

		if (userCanSellProduct(q, soldProduct)) {
			soldProduct.subPrice(Map.rand_range(1, 3));
			price = soldProduct.getPrice();
			userInv.receiveMoney(price * q);
			userInv.removeProduct(soldProduct.getId(), q);
		}
	}

	public Bot createBot() {
		int[] hq = placeHQ();
		Bot newBot = new Bot(this.map, new Inventory(botID), botID, this, hq[0], hq[1]);
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				Environment env = map.getEnvironmentAt(hq[0] + i, hq[1] + j);
				Structure struct = map.getStructAt(hq[0] + i, hq[1] + j);
				if (env != null)
					env.setOwnerID(botID);
				if (struct != null)
					struct.setOwnerID(botID);
			}
		}
		++botID;

		return (newBot);
	}

	public boolean testRod() {
		return (inv.hasItem(2) && inv.rodDurability > 0);
	}



	public void fixRod() {
		if (inv.hasPlant(3)) {
			inv.removePlant(3, 5);
			inv.rodDurability = 15;
		}
	}

	private static void printUsage() {
		System.out.println("Usage:");
		System.out.println("./gradle server:run --args '[nbOfPlayers] [nbOfBots] [port=51769]'");
	}

	public static void main(String[] args) {
		int port;
		int p;
		int b;

		if (args.length < 2) {
			System.out.println("Missing argument");
			printUsage();
			return;
		}
		try {
			p = Integer.valueOf(args[0]);
		} catch (Exception e) {
			System.out.println("Invalid number of players");
			printUsage();
			return;
		}
		try {
			b = Integer.valueOf(args[1]);
		} catch (Exception e) {
			System.out.println("Invalid number of bots");
			printUsage();
			return;
		}
		try {
			port = Integer.valueOf(args[2]);
			if (port < 0 || port > 65535) {
				System.out.println("Invalid port number, defaulting to 51769");
				throw new Exception();
			}
		} catch (Exception e) {
			port = 51769;
		}
		Server gameServer = new Server(port, p, b);
		gameServer.acceptConnections();
	}
}
