package com.pi4.mgmtgame.networking;
import java.io.*;
import java.net.*;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

public class Server {
	private ServerSocket serverSocket;
	private int numPlayers;
	private ServerSide player1;
	private ServerSide player2;
	
	public Server() {
		System.out.println("----Server----");
        try {
			serverSocket = new ServerSocket(51769);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Exception in Server constructor.");
		}
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
			
			System.out.println("Closed conns.");
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(e + "\nexception on acceptConns");
		}
	}
	
	private class ServerSide implements Runnable {
		private Socket playerSocket;
		private DataInputStream dataIn;
		private DataOutputStream dataOut;
		private int playerID;
		
		public ServerSide(Socket s, int id) 	{
			playerSocket = s;
			playerID = id;		
			try {
				dataIn = new DataInputStream(playerSocket.getInputStream());
				dataOut = new DataOutputStream(playerSocket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e + "\nexception on ServerSide constructor");
			}
		}
		
		public void run() {
			try {
				dataOut.writeInt(playerID);
				dataOut.flush();
			
				while (true) {	
					
				}
			} catch (IOException e) {
				System.out.println(e + "\nexception on ServerSide runnable");
				e.printStackTrace();
			}
		}
	}
	
    public static void main(String[] args) {
    	Server gameServer = new Server();
    	gameServer.acceptConnections();
    }
}
