package com.game;

import com.badlogic.gdx.Game;
import com.game.Map.GameMap;

public class NarutoG extends Game {

	private final GameMap gameMap;

	public NarutoG(){
		gameMap = new GameMap();
	}
	@Override
	public void create() {
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
