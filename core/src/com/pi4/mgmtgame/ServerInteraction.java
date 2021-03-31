package com.pi4.mgmtgame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.badlogic.gdx.assets.AssetManager;
import com.pi4.mgmtgame.blocks.*;
import com.pi4.mgmtgame.resources.Grain;
import com.pi4.mgmtgame.resources.Plant;

public class ServerInteraction {
	private ClientSide clientSideConnection;
	private int playerID;

	public ServerInteraction(int ID) {
		playerID = ID;
		connectToServer();
	}

	public Map getMap() {
		Map map = null;
		
		try {
			clientSideConnection.dataOut.writeInt(0);
			clientSideConnection.dataOut.flush();
			map = (Map) clientSideConnection.objIn.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return (map);
	}
	
	public int getCurrentPlayer() {
		int player = -1;
		
		try {
			clientSideConnection.dataOut.writeInt(1);
			clientSideConnection.dataOut.flush();
			player = clientSideConnection.dataIn.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		return (player);
	}

	public Inventory getInventory() {
		Inventory inv = null;
		
		try {
			clientSideConnection.dataOut.writeInt(2);
			clientSideConnection.dataOut.flush();
			inv = (Inventory) clientSideConnection.objIn.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return (inv);
	}

	public int getTurn() {
		int turn = 0;
		
		try {
			clientSideConnection.dataOut.writeInt(3);
			clientSideConnection.dataOut.flush();
			turn = clientSideConnection.dataIn.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		return (turn);
	}

	public int getInternalTurn() {
		int turn = -1;
		
		try {
			clientSideConnection.dataOut.writeInt(4);
			clientSideConnection.dataOut.flush();
			turn = clientSideConnection.dataIn.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		return (turn);
	}

	public boolean canBuildStructure(int x, int y, Structure struct) {
		boolean canBuildOk = false;
		
		try {
			clientSideConnection.dataOut.writeInt(5);
			clientSideConnection.dataOut.flush();
			
			clientSideConnection.dataOut.writeInt(x);
			clientSideConnection.dataOut.flush();
			
			clientSideConnection.dataOut.writeInt(y);
			clientSideConnection.dataOut.flush();
			
			clientSideConnection.objOut.writeObject(struct);
			clientSideConnection.objOut.flush();
			
			canBuildOk  = clientSideConnection.dataIn.readBoolean();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		return (canBuildOk);
	}

	public boolean requestBuildStructure(int x, int y, Structure struct) {
		boolean requestBuildOk = false;
		
		try {
			clientSideConnection.dataOut.writeInt(6);
			clientSideConnection.dataOut.flush();
			
			clientSideConnection.dataOut.writeInt(x);
			clientSideConnection.dataOut.flush();
			
			clientSideConnection.dataOut.writeInt(y);
			clientSideConnection.dataOut.flush();
			
			clientSideConnection.objOut.writeObject(struct);
			clientSideConnection.objOut.flush();
			
			requestBuildOk  = clientSideConnection.dataIn.readBoolean();
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return (requestBuildOk);
	}
	
	public boolean canDestroyStructure(int x, int y) {
		boolean canDestroyOk = false;
		
		try {
			clientSideConnection.dataOut.writeInt(7);
			clientSideConnection.dataOut.flush();
			
			clientSideConnection.dataOut.writeInt(x);
			clientSideConnection.dataOut.flush();
			
			clientSideConnection.dataOut.writeInt(y);
			clientSideConnection.dataOut.flush();
			
			canDestroyOk  = clientSideConnection.dataIn.readBoolean();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		return (canDestroyOk);
	}

	public boolean requestPlantSeed(int x, int y, Grain seed) {
		boolean requestPlantOk = false;
		
		try {
			clientSideConnection.dataOut.writeInt(8);
			clientSideConnection.dataOut.flush();
			
			clientSideConnection.dataOut.writeInt(x);
			clientSideConnection.dataOut.flush();
			
			clientSideConnection.dataOut.writeInt(y);
			clientSideConnection.dataOut.flush();
			
			clientSideConnection.objOut.writeObject(seed);
			clientSideConnection.objOut.flush();
			
			requestPlantOk  = clientSideConnection.dataIn.readBoolean();
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return (requestPlantOk);
	}
	
	public boolean requestDestroyStructure(int x, int y) {
		boolean requestDestroyOk = false;
		
		try {
			clientSideConnection.dataOut.writeInt(9);
			clientSideConnection.dataOut.flush();
			
			clientSideConnection.dataOut.writeInt(x);
			clientSideConnection.dataOut.flush();
			
			clientSideConnection.dataOut.writeInt(y);
			clientSideConnection.dataOut.flush();
			
			requestDestroyOk = clientSideConnection.dataIn.readBoolean();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		return (requestDestroyOk);
	}

	public boolean canHarvest(int x, int y) {
		boolean canHarvestOk = false;
		
		try {
			clientSideConnection.dataOut.writeInt(10);
			clientSideConnection.dataOut.flush();
			
			clientSideConnection.dataOut.writeInt(x);
			clientSideConnection.dataOut.flush();
			
			clientSideConnection.dataOut.writeInt(y);
			clientSideConnection.dataOut.flush();
			
			canHarvestOk = clientSideConnection.dataIn.readBoolean();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		return (canHarvestOk);	
	}

	public boolean requestHarvest(int x, int y) {
		boolean requestHarvestOk = false;
		
		try {
			clientSideConnection.dataOut.writeInt(11);
			clientSideConnection.dataOut.flush();
			
			clientSideConnection.dataOut.writeInt(x);
			clientSideConnection.dataOut.flush();
			
			clientSideConnection.dataOut.writeInt(y);
			clientSideConnection.dataOut.flush();
			
			requestHarvestOk = clientSideConnection.dataIn.readBoolean();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		return (requestHarvestOk);
	}

	public void passTurn() {
		try {
			clientSideConnection.dataOut.writeInt(12);
			clientSideConnection.dataOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private class ClientSide {
		protected Socket clientSocket;
		
		protected DataInputStream dataIn;
		protected DataOutputStream dataOut;
		protected ObjectInputStream objIn;
		protected ObjectOutputStream objOut;
		
		public ClientSide() {
			System.out.println("----Client----");
			try {
				clientSocket = new Socket("localhost", 51769);
				dataIn = new DataInputStream(clientSocket.getInputStream());
				dataOut = new DataOutputStream(clientSocket.getOutputStream());
				objIn = new ObjectInputStream(clientSocket.getInputStream());
				objOut = new ObjectOutputStream(clientSocket.getOutputStream());
				
				playerID = dataIn.readInt();
				
				System.out.println("\nConnection to " + clientSocket.getInetAddress() + " on port: " + clientSocket.getPort() + " successful.");
				System.out.println("Connected as player " + playerID);
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println(e + "\nexception on ClientSide constructor");
			}
		}
	}
	
	public void connectToServer() {
		clientSideConnection = new ClientSide();
	}
}
