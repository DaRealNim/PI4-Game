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
	public int currentPlayer;
	private int nbOfPlayers;
	private int playerID;
	
	public Server(Inventory[] inv, AssetManager manager, int nbOfPlayers) {
		System.out.println("----Server----");
        try {
			serverSocket = new ServerSocket(51769);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Exception in Server constructor.");
		}
        
		this.map = new Map(10, 10, manager, null);
		this.nbOfPlayers = nbOfPlayers;
		this.invArray = new Inventory[nbOfPlayers];
		this.invArray = inv;
		this.turn = 0;
		this.internalTurn = 0;
		this.inv = invArray[0];
	}
	
	public void acceptConnections() {
		try {
			System.out.println("Waiting on conns...");
			
			while (numPlayers < 2) {
				Socket playerSocket = serverSocket.accept();
				
				++numPlayers;		
				System.out.println("Player " + numPlayers + " connected");
				
				ServerSide serverSideConnection = new ServerSide(playerSocket, numPlayers);
			
				if (numPlayers == 1)
					player1 = serverSideConnection;
				else
					player2 = serverSideConnection;
				
				Thread t = new Thread(serverSideConnection);
				t.start();
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
			
			try {
				dataIn = new DataInputStream(playerSocket.getInputStream());
				dataOut = new DataOutputStream(playerSocket.getOutputStream());
				objIn = new ObjectInputStream(playerSocket.getInputStream());
				objOut = new ObjectOutputStream(playerSocket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e + "\nexception on ServerSide constructor");
			}
		}
		
		public void run() {
			try {
				dataOut.writeInt(playerID);
				dataOut.flush();
			} catch (IOException e) {
				System.out.println(e + "\nexception on ServerSide runnable");
				e.printStackTrace();
			}
		}
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
		Server gameServer = new Server(null, null, 2);
		gameServer.acceptConnections();
	}
}
