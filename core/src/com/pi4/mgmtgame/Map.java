package com.pi4.mgmtgame;
import com.pi4.mgmtgame.blocks.*;

class Map {
	Environment[][] envnmt_map;
	Structure[][] struct_map;
	
	
	public Map(int w, int h) {
		this.envnmt_map = new Environment[w][h];
		this.struct_map = new Structure[w][h];
		
		for(int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				envnmt_map[i][j] = new Plain();		
			}
		}
	}
	
	
	
}
