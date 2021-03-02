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
	
	Environment getEnvironmentAt(int w, int h) {
		return envnmt_map[w][h];
	}
	
	Structure getStructAt(int w, int h) {
		return struct_map[w][h];
	}
	
	void setEnvironmentAt(int w, int h, Environment e) {
		envnmt_map[w][h] = e;
	}
	
	void setStructAt(int w, int h, Structure s) {
		struct_map[w][h] = s;
	}
	
	/*
	 * @Override
	 * public void draw(Batch batch, float parentAlpha) {
	 * 	    for(Environmnent e : envnmt_map) {
	 * 	        e.draw();
	 * 	    }
	 * 	    for(Structure s : struct_map) {
	 * 	        s.draw();
	 * 	    }
	 * 	}
	 */
	
}
