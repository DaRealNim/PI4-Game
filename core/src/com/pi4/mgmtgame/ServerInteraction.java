package com.pi4.mgmtgame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.badlogic.gdx.assets.AssetManager;
import com.pi4.mgmtgame.blocks.*;
import com.pi4.mgmtgame.resources.Animal;
import com.pi4.mgmtgame.resources.Grain;
import com.pi4.mgmtgame.resources.Plant;
import com.pi4.mgmtgame.resources.Product;
import com.pi4.mgmtgame.resources.Item;
import com.pi4.mgmtgame.resources.Resources;
import java.util.HashMap;

public class ServerInteraction {
	private ClientSide clientSideConnection;
	private HUD hud;
	private int playerID;
	private int storedInternalTurn = -1;
	private int hqX;
	private int hqY;
	private HashMap<Integer, java.awt.Color> idToColorMap;

	public ServerInteraction(String ip, String port) {
		int portInt = Integer.parseInt(port);
		connectToServer(ip, portInt);
	}

	public void passHUD(HUD hud) {
		this.hud = hud;
	}

	public int getID() {
		return playerID;
	}

	public int getStoredInternalTurn() {
		return storedInternalTurn;
	}

	public synchronized Map getMap() {
		// System.out.println("getMap()");
		Map map = null;

		try {
			clientSideConnection.dataOut.writeInt(0);
			clientSideConnection.dataOut.flush();
			map = (Map) clientSideConnection.objIn.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// System.out.println("end getMap()");
		return (map);
	}

	public synchronized int getCurrentPlayer() {
		// System.out.println("getCurrentPlayer()");
		int player = -1;

		try {
			clientSideConnection.dataOut.writeInt(1);
			clientSideConnection.dataOut.flush();
			player = clientSideConnection.dataIn.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.println("end getCurrentPlayer()");
		return (player);
	}

	public synchronized Inventory getInventory() {
		// System.out.println("getInventory()");
		Inventory inv = null;
		try {
			clientSideConnection.dataOut.writeInt(2);
			clientSideConnection.dataOut.flush();
			inv = (Inventory) clientSideConnection.objIn.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// System.out.println("end getInventory()");
		return (inv);
	}

	public synchronized int getTurn() {
		// System.out.println("getTurn()");
		int turn = 0;
		try {
			clientSideConnection.dataOut.writeInt(3);
			clientSideConnection.dataOut.flush();
			turn = clientSideConnection.dataIn.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.println("end getTurn()");
		return (turn);
	}

	public synchronized int getInternalTurn() {
		// System.out.println("getInternalTurn()");
		int turn = -1;
		try {
			clientSideConnection.dataOut.writeInt(4);
			clientSideConnection.dataOut.flush();
			turn = clientSideConnection.dataIn.readInt();
			storedInternalTurn = turn;
		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.println("end getInternalTurn()");
		return (turn);
	}

	public synchronized boolean canBuildStructure(int x, int y, Structure struct) {
		// System.out.println("canBuildStructure()");
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
		} catch (Exception e) {
			e.printStackTrace();
		}

		// System.out.println("end canBuildStructure()");
		return (canBuildOk);
	}

	public synchronized boolean requestBuildStructure(int x, int y, Structure struct) {
		// System.out.println("requestBuildStructure()");
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
		// System.out.println("end requestBuildStructure()");
		return (requestBuildOk);
	}

	public synchronized boolean canDestroyStructure(int x, int y) {
		// System.out.println("canDestroyStructure()");
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
		// System.out.println("end canDestroyStructure()");
		return (canDestroyOk);
	}

	public synchronized boolean requestPlantSeed(int x, int y, Grain seed) {
		// System.out.println("requestPlantSeed()");
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
		// System.out.println("end requestPlantSeed()");
		return (requestPlantOk);
	}

	public synchronized boolean requestDestroyStructure(int x, int y) {
		// System.out.println("requestDestroyStructure()");
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
		//hud.update()();
		// System.out.println("end requestDestroyStructure()");
		return (requestDestroyOk);
	}

	public synchronized boolean canHarvest(int x, int y) {
		// System.out.println("canHarvest()");
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
		// System.out.println("end canHarvest()");
		return (canHarvestOk);
	}

	public synchronized boolean requestHarvest(int x, int y) {
		// System.out.println("requestHarvest()");
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
		// System.out.println("end requestHarvest()");
		return (requestHarvestOk);
	}

	public synchronized void passTurn() {
		// System.out.println("passTurn()");
		try {
			clientSideConnection.dataOut.writeInt(12);
			clientSideConnection.dataOut.flush();
			storedInternalTurn = -1;
		} catch (IOException e) {
			e.printStackTrace();
		}
		// System.out.println("end passTurn()");
	}

	public synchronized void buyGrain(Grain boughtGrain, int q) {
		try {
			clientSideConnection.dataOut.writeInt(13);
			clientSideConnection.dataOut.flush();
			clientSideConnection.objOut.writeObject(boughtGrain);
			clientSideConnection.objOut.flush();
			clientSideConnection.dataOut.writeInt(q);
			clientSideConnection.dataOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void sellGrain(Grain soldGrain, int q) {
		try {
			clientSideConnection.dataOut.writeInt(14);
			clientSideConnection.dataOut.flush();
			clientSideConnection.objOut.writeObject(soldGrain);
			clientSideConnection.objOut.flush();
			clientSideConnection.dataOut.writeInt(q);
			clientSideConnection.dataOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void buyPlant(Plant boughtPlant, int q) {
		try {
			clientSideConnection.dataOut.writeInt(15);
			clientSideConnection.dataOut.flush();
			clientSideConnection.objOut.writeObject(boughtPlant);
			clientSideConnection.objOut.flush();
			clientSideConnection.dataOut.writeInt(q);
			clientSideConnection.dataOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void sellPlant(Plant soldPlant, int q) {
		try {
			clientSideConnection.dataOut.writeInt(16);
			clientSideConnection.dataOut.flush();
			clientSideConnection.objOut.writeObject(soldPlant);
			clientSideConnection.objOut.flush();
			clientSideConnection.dataOut.writeInt(q);
			clientSideConnection.dataOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void requestUseItem(int x, int y, Item item) {
		try {
			clientSideConnection.dataOut.writeInt(17);
			clientSideConnection.dataOut.flush();

			clientSideConnection.dataOut.writeInt(x);
			clientSideConnection.dataOut.flush();

			clientSideConnection.dataOut.writeInt(y);
			clientSideConnection.dataOut.flush();

			clientSideConnection.objOut.writeObject(item);
			clientSideConnection.objOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized boolean canBuyTerrain(int x, int y) {
        boolean canBuyOk = false;
        try {
            clientSideConnection.dataOut.writeInt(18);
            clientSideConnection.dataOut.flush();

            clientSideConnection.dataOut.writeInt(x);
            clientSideConnection.dataOut.flush();

            clientSideConnection.dataOut.writeInt(y);
            clientSideConnection.dataOut.flush();

            canBuyOk = clientSideConnection.dataIn.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (canBuyOk);
    }

    public synchronized boolean requestBuyTerrain(int x, int y) {
        boolean canBuyOk = false;
        try {
            clientSideConnection.dataOut.writeInt(19);
            clientSideConnection.dataOut.flush();

            clientSideConnection.dataOut.writeInt(x);
            clientSideConnection.dataOut.flush();

            clientSideConnection.dataOut.writeInt(y);
            clientSideConnection.dataOut.flush();

            canBuyOk = clientSideConnection.dataIn.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (canBuyOk);
    }

	public synchronized int getPrice(Resources r) {
			try {
				clientSideConnection.dataOut.writeInt(20);
				clientSideConnection.dataOut.flush();

				clientSideConnection.objOut.writeObject(r);
				clientSideConnection.objOut.flush();

				return (clientSideConnection.dataIn.readInt());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return (-1);
	}
	
	public synchronized boolean canBreed(int x, int y, Animal animal) {
		return true;
	}
	
	public synchronized boolean requestBreed(int x, int y, Animal animal) {
		return true;
	}
	
	public synchronized void buyAnimal(Animal boughtAnimal, int q) {
		
	}
	
	public synchronized void sellAnimal(Animal soldAnimal, int q) {
		
	}
	
	public synchronized void buyProduct(Product boughtProduct, int q) {
		
	}
	
	public synchronized void sellProduct(Product soldProduct, int q) {
		
	}

	public synchronized boolean canGameStart() {
		try {
			clientSideConnection.dataOut.writeInt(256);
			clientSideConnection.dataOut.flush();

			return (clientSideConnection.dataIn.readBoolean());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public int getHqX() {
		return hqX;
	}

	public int getHqY() {
		return hqY;
	}

	public HashMap<Integer, java.awt.Color> getIdToColorMap() {
		return idToColorMap;
	}

	private class ClientSide {
		protected Socket clientSocket;

		protected DataInputStream dataIn;
		protected DataOutputStream dataOut;
		protected ObjectInputStream objIn;
		protected ObjectOutputStream objOut;

		public ClientSide(String ip, int port) {
			System.out.println("----Client----");
			try {
				clientSocket = new Socket(ip, port);
				dataOut = new DataOutputStream(clientSocket.getOutputStream());
				dataIn = new DataInputStream(clientSocket.getInputStream());
				objOut = new ObjectOutputStream(clientSocket.getOutputStream());
				objIn = new ObjectInputStream(clientSocket.getInputStream());


				playerID = dataIn.readInt();
				hqX = dataIn.readInt();
				hqY = dataIn.readInt();
				idToColorMap = (HashMap<Integer, java.awt.Color>)objIn.readObject();


				System.out.println("\nConnection to " + clientSocket.getInetAddress() + " on port: " + clientSocket.getPort() + " successful.");
				System.out.println("Connected as player " + playerID);
				System.out.println("Waiting for game to start...");
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println(e + "\nexception on ClientSide constructor");
			}
		}
	}

	public void connectToServer(String ip, int port) {
		clientSideConnection = new ClientSide(ip, port);
	}
}
