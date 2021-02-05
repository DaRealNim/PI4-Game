package com.pi4.mgmtgame.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.pi4.mgmtgame.ManagementGame;

public class DesktopLauncher {
	

	
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		
		config.setIdleFPS(60);
		config.setWindowSizeLimits(ManagementGame.WIDTH, ManagementGame.HEIGHT, -1, -1); 
		config.setResizable(true);
		
		new Lwjgl3Application(new ManagementGame(), config);
	}
}
