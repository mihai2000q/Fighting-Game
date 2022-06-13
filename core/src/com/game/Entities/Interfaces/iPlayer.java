package com.game.Entities.Interfaces;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.game.Entities.Player;
import com.game.Helper.Interfaces.iEntity;

public interface iPlayer extends iEntity {
    void update(float delta);
    void draw(Batch batch);
    Player.State getState();
    void restartPosition();
    void setDead();
    void setHit(float xSpeed, float ySpeed);
    void resetHit();
    String toString();
}
