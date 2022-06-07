package com.game;

import com.badlogic.gdx.Game;
import com.game.Map.GameMap;

public final class FightingGame extends Game {

	private GameMap gameMap;
	@Override
	public void create() {
		gameMap = new GameMap();
		setScreen(gameMap);
	}
	@Override
	public void render() {
		super.render();
	}
	@Override
	public void dispose() {
		super.dispose();
		gameMap.dispose();
	}
}
