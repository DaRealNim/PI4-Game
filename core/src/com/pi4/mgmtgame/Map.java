package com.pi4.mgmtgame;
import com.pi4.mgmtgame.blocks.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.assets.AssetManager;

public class Map extends Actor {
	private Environment[][] envnmt_map;
	private Structure[][] struct_map;


	public Map(int w, int h, AssetManager manager) {
		this.envnmt_map = new Environment[w][h];
		this.struct_map = new Structure[w][h];

		for(int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				envnmt_map[i][j] = new Plain(manager);
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


	@Override
	public void draw(Batch batch, float parentAlpha) {
	    for(Environment[] row : envnmt_map) {
			for(Environment block : row) {
	        	block.draw(batch, parentAlpha);
			}
	    }
		for(Structure[] row : struct_map) {
			for(Structure block : row) {
	        	block.draw(batch, parentAlpha);
			}
	    }
	}


}
