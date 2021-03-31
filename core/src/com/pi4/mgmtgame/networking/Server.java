package com.pi4.mgmtgame.networking;
import java.io.*;
import java.net.*;

import com.badlogic.gdx.assets.AssetManager;
import com.pi4.mgmtgame.Inventory;
import com.pi4.mgmtgame.Map;
import com.pi4.mgmtgame.blocks.Block;
import com.pi4.mgmtgame.blocks.Environment;
import com.pi4.mgmtgame.blocks.Field;
import com.pi4.mgmtgame.blocks.Structure;
import com.pi4.mgmtgame.resources.Grain;
import com.pi4.mgmtgame.resources.Plant;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;

public class Server {
	private ServerSocket serverSocket;
	private int numPlayers;
	private ServerSide player1;
	private ServerSide player2;

	private Map map;
	private Inventory[] invArray;
	private Inventory inv;
	private int turn;
	private int internalTurn;
	private int currentPlayer;
	private int nbOfPlayers;
	private int playerID;

	public Server(int nbOfPlayers) {
		System.out.println("----Server----");
        try {
			serverSocket = new ServerSocket(51769);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Exception in Server constructor.");
		}

		this.map = new Map(10, 10, null, null);
		this.nbOfPlayers = nbOfPlayers;
		this.invArray = new Inventory[nbOfPlayers];
		for(int i=0; i<nbOfPlayers; i++) {
			this.invArray[i] = new Inventory(i);
		}
		this.turn = 0;
		this.internalTurn = 0;
		this.inv = invArray[0];
	}

	public void acceptConnections() {
		try {
			System.out.println("Waiting on conns...");

			while (numPlayers < 2) {
				Socket playerSocket = serverSocket.accept();
				System.out.println("Player " + numPlayers + " connected");

				ServerSide serverSideConnection = new ServerSide(playerSocket, numPlayers);
				System.out.println("bruh???");

				if (numPlayers == 1)
					player1 = serverSideConnection;
				else
					player2 = serverSideConnection;

				Thread t = new Thread(serverSideConnection);
				System.out.println("bruh??");
				t.start();
				System.out.println("bruh?");
				++numPlayers;
			}
			System.out.println("Game will now start.");
		}
		catch (Exception e) {
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

		public ServerSide(Socket s, int id) 	{
			playerSocket = s;
			playerID = id;
			System.out.println("haha");
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
			System.out.println("run executed!");
			try {
				System.out.println("sending "+playerID);
				dataOut.writeInt(playerID);
				dataOut.flush();
				System.out.println("done");

				while (true) {
					int x = 0;
					int y = 0;
					Structure struct = null;
					Grain grain = null;
					int request = dataIn.readInt();
					System.out.println("Got request "+request);
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
							objOut.writeObject(getInventory());
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

	public Inventory getInventory() {
		return inv;
	}

	public int getTurn() {
		return turn;
	}

	public int getInternalTurn() {
		return internalTurn;
	}

	public boolean canBuildStructure(int x, int y, Structure struct) {
		Environment envBlock = map.getEnvironmentAt(x, y);
		return (envBlock.canBuild(struct) && inv.getMoney() >= struct.getConstructionCost() && envBlock != null);
	}

	public boolean requestBuildStructure(int x, int y, Structure struct) {
		if (canBuildStructure(x, y, struct)) {
			map.setStructAt(x, y, struct);
			inv.giveMoney(struct.getConstructionCost());
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
		System.out.println("Seed id: " + seed.getId());
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

	public boolean requestDestroyStructure(int x, int y) {
		if (canDestroyStructure(x, y)) {
			inv.receiveMoney((map.getStructAt(x, y).getConstructionCost()*30)/100);
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
		// à modifier
		inv = invArray[internalTurn % nbOfPlayers];
		currentPlayer = internalTurn % nbOfPlayers;
		if (internalTurn == nbOfPlayers) {
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
		System.out.println(this.inv);
		System.out.println(turn);
	}

	public static void main(String[] args) {
		Server gameServer = new Server(2);
		gameServer.acceptConnections();
	}
}
