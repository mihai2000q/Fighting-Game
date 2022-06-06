package com.game.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.game.Entities.Interfaces.iPlayer;
import com.game.Helper.Entity;

import static com.game.Helper.Constants.*;

public class Player extends Entity implements iPlayer {

    private final float X_SPEED = 2.5f;
    private final float JUMP_SPEED = 6f;
    private TextureRegion textureRegion;

    public Player(World world, float X, float Y) {
        super(world, X, Y);
        defineBody(BodyDef.BodyType.DynamicBody);
        this.setBounds(spawnPoint.x, spawnPoint.y, 257 / PPM / 100, 259 / PPM / 100);
        textureRegion = new TextureRegion(new Texture(PlayerPath), 64, 64);
    }
    @Override
    protected void defineFixture() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(5.5f / PPM, 14.5f / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0.1f;
        body.createFixture(fixtureDef).setUserData("Player");

        body.setUserData(this);
        massData.mass = 1.5f;
        body.setMassData(massData);
        shape.dispose();
    }
    @Override
    public void update(float delta) {
        this.setCenter(getX(),getY());
        this.setRegion(textureRegion);
        handleInput(delta);
    }
    @Override
    public void getState() {

    }
    @Override
    public void restartPosition() {

    }
    @Override
    public void setDead() {

    }
    private void handleInput(float delta) {
        float xSpeed = X_SPEED + X_SPEED * delta;
        float ySpeed = JUMP_SPEED + JUMP_SPEED * delta;
        Input input = Gdx.input;
        if(input.isKeyPressed(Input.Keys.RIGHT) || input.isKeyPressed(Input.Keys.D))
            body.setLinearVelocity(xSpeed, body.getLinearVelocity().y);
        else if(input.isKeyPressed(Input.Keys.LEFT) || input.isKeyPressed(Input.Keys.A))
            body.setLinearVelocity(-xSpeed, body.getLinearVelocity().y);
        else
            body.setLinearVelocity(0,body.getLinearVelocity().y);

        if(input.isKeyJustPressed(Input.Keys.UP) ||
            input.isKeyJustPressed(Input.Keys.W) ||
            input.isKeyJustPressed(Input.Keys.SPACE))
            body.applyLinearImpulse(new Vector2(0, ySpeed), body.getWorldCenter(), true);
    }
}
