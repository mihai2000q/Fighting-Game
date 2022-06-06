package com.game.Entities.Interfaces;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface iPlayer {
    void update(float delta);
    void draw(Batch batch);
    void getState();
    void restartPosition();
    void setDead();
}
