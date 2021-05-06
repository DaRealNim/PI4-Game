package com.pi4.mgmtgame.networking;

import java.io.*;
import java.net.*;
import java.util.Random;

import com.badlogic.gdx.assets.AssetManager;
import com.pi4.mgmtgame.Inventory;
import com.pi4.mgmtgame.Map;
import com.pi4.mgmtgame.blocks.Block;
import com.pi4.mgmtgame.blocks.Environment;
import com.pi4.mgmtgame.blocks.Field;
import com.pi4.mgmtgame.blocks.HQ;
import com.pi4.mgmtgame.blocks.Plain;
import com.pi4.mgmtgame.blocks.Structure;
import com.pi4.mgmtgame.resources.Resources;
import com.pi4.mgmtgame.resources.Animal;
import com.pi4.mgmtgame.resources.Grain;
import com.pi4.mgmtgame.resources.Item;
import com.pi4.mgmtgame.resources.Plant;
import com.pi4.mgmtgame.resources.Product;
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
	private Inventory inv;
	private int turn;
	private int internalTurn;
	private int currentPlayer;
	private int nbOfPlayers;
	private int nbOfBots;
	private int playerID;
	private HashMap<Integer, Color> idToColorMap;
	private volatile boolean gameCanStart = false;

	public Server(int nbOfPlayers, int nbOfBots) {
		System.out.println("----Server----");
		try {
			serverSocket = new ServerSocket(51769);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Exception in Server constructor.");
		}

		this.map = new Map(50, 50, null, null);
		this.nbOfPlayers = nbOfPlayers;
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
		for(int i=0; i<nbOfBots; i++) {
			this.bots[i] = createBot();
		}

		for(int i=0; i<nbOfPlayers+nbOfBots; i++) {
			// float r = Math.round(Math.random() * 100.0f) / 100.0f;
			// float g = Math.round(Math.random() * 100.0f) / 100.0f;
			// float b = Math.round(Math.random() * 100.0f) / 100.0f;
			Color col = Color.getHSBColor(Map.rand_range(0, 255)/255.0f, Map.rand_range(50, 255)/255.0f, 1.0f);
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
				HQ hq = new HQ(hqcoords[0],hqcoords[1]);
				hq.setOwnerID(playerID);
				map.setStructAt(hqcoords[0], hqcoords[1], hq);

				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						Environment env = map.getEnvironmentAt(hqcoords[0]+i, hqcoords[1]+j);
						Structure struct = map.getStructAt(hqcoords[0]+i, hqcoords[1]+j);
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
					Item item = null;
					int request = -1;
					try {
						request = dataIn.readInt();
					} catch (SocketException | EOFException e) {
						System.out.println("Player "+playerID+" disconnected");
						players[playerID] = null;
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
					case 256:
						dataOut.writeBoolean(gameCanStart);
						dataOut.flush();
						break;
					default:
						System.out.println("wyd????");
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
		return invArray[id];
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
		// System.out.println("Player "+internalTurn+" turn");
		if (players[internalTurn % nbOfPlayers] == null)
			passTurn();
		// System.out.println("Player "+internalTurn+" turn after while");
		// à modifier
		inv = invArray[internalTurn % nbOfPlayers];
		currentPlayer = internalTurn % nbOfPlayers;
		if (internalTurn == nbOfPlayers) {
			for (Bot bot : bots)
			{
				if (bot != null)
					bot.play();
			}
			turn++;
			internalTurn = 0;
			for (heightIndex = 0; heightIndex < mapHeight; heightIndex++) {
				for (widthIndex = 0; widthIndex < mapWidth; widthIndex++) {
					currBlock = map.getEnvironmentAt(heightIndex, widthIndex);
					if (currBlock != null)
						currBlock.passTurn();

					currBlock = map.getStructAt(heightIndex, widthIndex);
					if (currBlock != null)
						currBlock.passTurn();
				}
			}
		}
		// System.out.println(this.inv);
		System.out.println("Turn "+turn+"\n=======================");
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

	public void buyGrain(Grain boughtGrain, int q) {
		Inventory userInv = getInventory();
		int grainPrice = boughtGrain.getPrice();

		if (userHasMoneyToBuy(q, boughtGrain)) {
			userInv.giveMoney(grainPrice * q);
			userInv.addGrain(boughtGrain.getId(), q);
			boughtGrain.addPrice(1);
			// System.out.println(boughtGrain.toString() + " price: " + boughtGrain.getPrice());
		}
	}

	public void sellGrain(Grain soldGrain, int q) {
		Inventory userInv = getInventory();
		int grainPrice = soldGrain.getPrice();

		if (userCanSellGrain(q, soldGrain)) {
			soldGrain.subPrice(1);
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
			boughtPlant.addPrice(1);
			// System.out.println(boughtPlant.toString() + " price: " + boughtPlant.getPrice());
		}
	}

	public void sellPlant(Plant soldPlant, int q) {
		Inventory userInv = getInventory();
		int plantPrice = soldPlant.getPrice();

		if (userCanSellPlant(q, soldPlant)) {
			soldPlant.subPrice(1);
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

		Environment envLeft = map.getEnvironmentAt(x-1, y);
		Environment envRight = map.getEnvironmentAt(x+1, y);
		Environment envUp = map.getEnvironmentAt(x, y+1);
		Environment envDown = map.getEnvironmentAt(x, y-1);
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


		if (terrainEnv != null && terrainEnv.testOwner(-1) && availableMoney >= terrainPrice && (ownUp || ownDown || ownLeft || ownRight))
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

	public boolean canBreed(int x, int y, Animal animal) {
		//pour benj
		return true;
	}

	public boolean requestBreed(int x, int y, Animal animal) {
		//pour benj
		return true;
	}

	public void buyAnimal(Animal boughtAnimal, int q) {

	}

	public void sellAnimal(Animal soldAnimal, int q) {

	}

	public void buyProduct(Product boughtProduct, int q) {

	}

	public void sellProduct(Product soldProduct, int q) {

	}

	public int[] placeHQ() {
		int x;
		int y;
		int height = map.getMapHeight();
		int width = map.getMapWidth();
		while (true) {
			boolean ok = true;
			x = Map.rand_range(1, height-2);
			y = Map.rand_range(1, height-2);
			if(!(map.getEnvironmentAt(x,y) instanceof Plain) || (map.getStructAt(x,y) instanceof TreeField))
				ok = false;
			for (int i = -5; i < 5; i++) {
				for (int j = -5; j < 5; j++) {
					if(map.getStructAt(x+i,y+j) instanceof HQ)
						ok = false;
				}
			}
			if (ok)
				break;
		}
		int[] v = {x,y};
		return v;
	}

	public Bot createBot()
	{
		int[] hq = placeHQ();
		Bot newBot = new Bot(this.map, new Inventory(botID), botID, this, hq[0], hq[1]);
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				Environment env = map.getEnvironmentAt(hq[0]+i, hq[1]+j);
				Structure struct = map.getStructAt(hq[0]+i, hq[1]+j);
				if (env != null)
					env.setOwnerID(botID);
				if (struct != null)
					struct.setOwnerID(botID);
			}
		}
		++botID;

		return (newBot);
	}

	public static void main(String[] args) {
		Server gameServer = new Server(1, 10);
		gameServer.acceptConnections();
	}
}
