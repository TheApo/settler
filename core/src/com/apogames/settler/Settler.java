package com.apogames.settler;

import com.apogames.settler.asset.AssetLoader;
import com.apogames.settler.backend.Game;
import com.apogames.settler.game.MainPanel;
import com.badlogic.gdx.Gdx;

public class Settler extends Game {

	@Override
	public void create () {
		AssetLoader.load();
		setScreen(new MainPanel());
		if (Constants.IS_HTML) {
			Gdx.graphics.setContinuousRendering(false);
			Gdx.graphics.requestRendering();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		AssetLoader.dispose();
	}

	public void resume() {
		super.resume();
		AssetLoader.load();
	}
}
