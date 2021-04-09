package com.pi4.mgmtgame;
import com.pi4.mgmtgame.ServerInteraction;
import com.pi4.mgmtgame.blocks.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.io.Serializable;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Group;

public class Map extends Group implements Serializable {
	private Environment[][] envnmt_map;
	private Structure[][] struct_map;
	private ServerInteraction server;
	private int width;
	private int height;


	public Map(int w, int h, AssetManager manager, ServerInteraction server) {
		this.width = w;
		this.height = h;
		this.server = server;
		this.envnmt_map = new Environment[width][height];
		this.struct_map = new Structure[width][height];

		for(int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Plain p = new Plain(i, j);
				envnmt_map[i][j] = p;
				addActor(p);
			}
		}
	}

	public void explicitPrint() {
		System.out.println("Couche environement:");
		for(Environment[] row : envnmt_map) {
			for(Environment block : row) {
				System.out.println(block);
			}
		}
		System.out.println("Couche structure:");
		for(Structure[] row : struct_map) {
			for(Structure block : row) {
				System.out.println(block);
			}
		}
	}

	@Override
    public void act(float delta) {
		for(Structure[] row : struct_map) {
			for(Structure block : row) {
				if (block != null) {
					if (block.testOwner(server.getID())) {
						block.getColor().a = (float)1;
					} else {
						block.getColor().a = (float)0.7;
					}
				}
			}
	    }
    }

	public void updateActors(final AssetManager manager, final ServerInteraction server) {
		clear();
		this.server = server;
		for(int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				if (envnmt_map[i][j] != null) {
					envnmt_map[i][j].addViewController(manager, server);
					addActor(envnmt_map[i][j]);
					envnmt_map[i][j].updateActors();
				}
				if (struct_map[i][j] != null) {
					struct_map[i][j].addViewController(manager, server);
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
