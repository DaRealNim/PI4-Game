package com.pi4.mgmtgame.networking;
import java.io.*;
import java.net.*;

import com.pi4.mgmtgame.Inventory;
import com.pi4.mgmtgame.ServerInteraction;


public class Player {
	private Inventory inv;
	private ServerInteraction serverInterface;
	private ClientSide clientSideConnection;
	private int playerID;
	private int otherPlayer;
	
	public Player() {
		connectToServer();
	}
	
	private class ClientSide {
		private Socket clientSocket;
		private DataInputStream dataIn;
		private DataOutputStream dataOut;
		
		public ClientSide() {
			System.out.println("----Client----");
			try {
				clientSocket = new Socket("localhost", 51769);
				dataIn = new DataInputStream(clientSocket.getInputStream());
				dataOut = new DataOutputStream(clientSocket.getOutputStream());
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
	
	public static void main(String[] args) {
		Player p = new Player();
		p.connectToServer();
	}
}
