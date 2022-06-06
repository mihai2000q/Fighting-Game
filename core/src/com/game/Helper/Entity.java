package com.game.Helper;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Entity extends Sprite {

    protected World world;
    protected Vector2 spawnPoint;
    protected MassData massData;
    protected Body body;
    protected BodyDef bodyDef;

    public Entity(World world, float X, float Y) {
        this.world = world;
        spawnPoint = new Vector2(X / Constants.PPM, Y / Constants.PPM);
        massData = new MassData();
    }
    protected void defineBody(BodyDef.BodyType bodyType) {
        bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(spawnPoint);
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);

        defineFixture();
    }
    public float getX(){
        return body.getPosition().x;
    }
    public float getY(){
        return body.getPosition().y;
    }
    protected abstract void defineFixture();
    public Body getBody(){
        return body;
    }
}
