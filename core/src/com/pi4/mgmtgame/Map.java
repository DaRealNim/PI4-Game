package com.pi4.mgmtgame;
import com.pi4.mgmtgame.ServerInteraction;
import com.pi4.mgmtgame.blocks.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Group;

public class Map extends Group {
	private Environment[][] envnmt_map;
	private Structure[][] struct_map;
	private int width;
	private int height;


	public Map(int w, int h, AssetManager manager, ServerInteraction server) {
		this.width = w;
		this.height = h;
		this.envnmt_map = new Environment[width][height];
		this.struct_map = new Structure[width][height];

		for(int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Plain p = new Plain(i, j, manager, server);
				envnmt_map[i][j] = p;
				addActor(p);
			}
		}
	}

	public void updateActors() {
		clear();
		for(int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				if (envnmt_map[i][j] != null) {
					addActor(envnmt_map[i][j]);
					envnmt_map[i][j].updateActors();
				}
				if (struct_map[i][j] != null) {
					addActor(struct_map[i][j]);
					struct_map[i][j].updateActors();
				}
			}
		}
	}

	public int getMapWidth() {
		return (width);
	}

	public int getMapHeight() {
		return (height);
	}

	public Environment getEnvironmentAt(int w, int h) {
		return envnmt_map[w][h];
	}

	public Structure getStructAt(int w, int h) {
		return struct_map[w][h];
	}

	public void setEnvironmentAt(int w, int h, Environment e) {
		envnmt_map[w][h] = e;
	}

	public void setStructAt(int w, int h, Structure s) {
		struct_map[w][h] = s;
	}


	//Commented for now because just adding blocks as group children work, but may need
	//later in case something goes horribly wrong
	//
	// @Override
	// public void act(float delta) {
	// 	for(Environment[] row : envnmt_map) {
	// 		for(Environment block : row) {
	// 			if (block != null)
	// 				block.act(delta);
	// 		}
	//     }
	// 	for(Structure[] row : struct_map) {
	// 		for(Structure block : row) {
	// 			if (block != null)
	// 				block.act(delta);
	// 		}
	//     }
	// }
	//
	//
	// @Override
	// public void draw(Batch batch, float parentAlpha) {
	//     for(Environment[] row : envnmt_map) {
	// 		for(Environment block : row) {
	// 			if (block != null)
	// 				block.draw(batch, parentAlpha);
	// 		}
	//     }
	// 	for(Structure[] row : struct_map) {
	// 		for(Structure block : row) {
	// 			if (block != null)
	// 				block.draw(batch, parentAlpha);
	// 		}
	//     }
	// }


}
