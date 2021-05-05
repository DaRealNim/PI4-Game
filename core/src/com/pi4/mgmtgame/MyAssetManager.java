package com.pi4.mgmtgame;

import com.badlogic.gdx.assets.AssetManager;

public class MyAssetManager extends AssetManager {
    public <T> void addAsset (final String fileName, Class<T> type, T asset) {
		super.addAsset(fileName, type, asset);
	}
}
