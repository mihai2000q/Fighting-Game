package com.game;

import com.badlogic.gdx.Game;
import com.game.Map.GameMap;

public class NarutoG extends Game {

	@Override
	public void create() {
		setScreen(new GameMap());
	}
	@Override
	public void render() {
		super.render();
	}
}
