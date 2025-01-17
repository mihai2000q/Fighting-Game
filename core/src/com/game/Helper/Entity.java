package com.game.Helper;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.game.Helper.Interfaces.iEntity;

public abstract class Entity extends Sprite implements iEntity {

    protected final World world;
    protected final Vector2 spawnPoint;
    protected final MassData massData;
    protected final BodyDef bodyDef;
    protected Body body;

    public Entity(World world, float X, float Y) {
        this.world = world;
        bodyDef = new BodyDef();
        spawnPoint = new Vector2(X / Constants.PPM, Y / Constants.PPM);
        massData = new MassData();
    }
    protected final void defineBody(BodyDef.BodyType bodyType) {
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
}
