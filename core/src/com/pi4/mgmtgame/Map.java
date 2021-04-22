package com.pi4.mgmtgame;
import com.pi4.mgmtgame.ServerInteraction;
import com.pi4.mgmtgame.blocks.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.io.Serializable;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.warmwaffles.noise.prime.PerlinNoise;
import java.util.Random;

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

		Random generator = new Random();
		int seed = generator.nextInt();
		System.out.println(seed);
		PerlinNoise noise = new PerlinNoise(seed, 1, 1, 1, 1);

		for(int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if (noise.getHeight(i/2+0.5, j/2+0.5) < -0.3) {
					Lake l = new Lake(i, j);
					l.setSpriteName("lake");
					envnmt_map[i][j] = l;
					addActor(l);
				} else {
					Plain p = new Plain(i, j);
					int type;
					if (rand_range(1, 100) < 60)
						type = 1;
					else
						type = rand_range(1, 5);
					String skinType = "plaine" + type;
					p.setSpriteName(skinType);
					envnmt_map[i][j] = p;
					addActor(p);
				}
			}
		}

		//Correct sprites for lakes
		for(int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				Environment env = getEnvironmentAt(x, y);
				if(env instanceof Lake) {
					if (isNotLake(x - 1, y)) {
						if (isNotLake(x, y - 1)) {
							env.setSpriteName("lake_bottom_left");
						} else if (isNotLake(x, y + 1)) {
							env.setSpriteName("lake_top_left");
						} else if (isNotLake(x, y + 1)) {
							env.setSpriteName("lake_top_left");
						} else {
							env.setSpriteName("lake_left_" + rand_range(1, 4));
						}
					} else if (isNotLake(x + 1, y)) {
						if (isNotLake(x, y - 1)) {
							env.setSpriteName("lake_bottom_right");
						} else if (isNotLake(x, y + 1)) {
							env.setSpriteName("lake_top_right");
						} else {
							env.setSpriteName("lake_right_" + rand_range(1, 3));
						}
					} else if (isNotLake(x, y - 1)) {
						env.setSpriteName("lake_down_" + rand_range(1, 2));
					} else if (isNotLake(x, y + 1)) {
						env.setSpriteName("lake_up_" + rand_range(1, 7));
					} else if (isNotLake(x - 1, y - 1)) {
						env.setSpriteName("lake_corner_bottom_left");
					} else if (isNotLake(x + 1, y - 1)) {
						env.setSpriteName("lake_corner_bottom_right");
					} else if (isNotLake(x - 1, y + 1)) {
						env.setSpriteName("lake_corner_top_left");
					} else if (isNotLake(x + 1, y + 1)) {
						env.setSpriteName("lake_corner_top_right");
					}

					// }
				}
			}
		}
	}

	private boolean isNotLake(int x, int y) {
		if (x >= 0 && x < width && y >= 0 && y < height) {
			return !getEnvironmentAt(x, y).toString().equals("Lake");
		} else {
			return false;
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
		if (e != null) {
			addActor(e);
		}
	}

	public void setStructAt(int w, int h, Structure s) {
		struct_map[w][h] = s;
		if (s != null) {
			addActor(s);
		}
	}

	public Environment[] getEnvironmentNeighbors(int x, int y) {
		Environment[] ret = new Environment[8];
		int c=0;
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				if(i != 0 && j != 0) {
					ret[c] = getEnvironmentAt(x + i, y + i);
					c++;
				}
			}
		}
		return ret;
	}

	private int rand_range(int min, int max) {
		Random random = new Random();
		return random.nextInt(max - min) + min;
	}


}
