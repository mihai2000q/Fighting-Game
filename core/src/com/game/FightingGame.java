package com.game;

import com.badlogic.gdx.Game;
import com.game.Map.GameStage;

public final class FightingGame extends Game {

	private GameStage gameMap;
	@Override
	public void create() {
		gameMap = new GameStage(); //has to be here and not in constructor because this method is run first
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
