package com.game.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.game.Entities.Interfaces.iPlayer;
import com.game.Helper.Entity;

import static com.game.Helper.Constants.*;
import static com.game.Managers.AnimationManager.*;

public class Player extends Entity implements iPlayer {

    public enum State{IDLE, RUNNING, JUMPING, FALLING, DEAD}
    private State currentState;
    private State previousState;

    private final float X_SPEED = 2.5f;
    private final float JUMP_SPEED = 6f;
    private final Animation<TextureRegion> idle;
    private final Animation<TextureRegion> running;
    private float stateTimer = 0f;
    private boolean runningRight = false;

    public Player(World world, float X, float Y) {
        super(world, X, Y);
        defineBody(BodyDef.BodyType.DynamicBody);
        currentState = State.IDLE;
        previousState = State.IDLE;

        //setRegion(atlas.findRegion("something"));
        idle = createAnimationFrame(PlayerPath + "idle.png", 7, 64,64);
        running = createAnimationFrame(PlayerPath + "running.png", 6, 64,64);

        this.setBounds(spawnPoint.x, spawnPoint.y, 64 / PPM, 64 / PPM);
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
        this.setRegion(getFrame(delta));
        handleInput(delta);
    }
    @Override
    public State getState() {
        if((body.getLinearVelocity().y > 0
                || (body.getLinearVelocity().y < 0
                && previousState == State.JUMPING)))
            return State.JUMPING;
        else if(body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.IDLE;
    }
    @Override
    public void restartPosition() {
        body.setTransform(spawnPoint.x, spawnPoint.y + 100f / PPM, 0);
    }
    @Override
    public void draw(Batch batch) {
        super.draw(batch);
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
            input.isKeyJustPressed(Input.Keys.SPACE)
            && currentState != State.JUMPING && currentState != State.FALLING)
            body.applyLinearImpulse(new Vector2(0, ySpeed), body.getWorldCenter(), true);
    }
    private TextureRegion getFrame(float delta) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case RUNNING:
                region = running.getKeyFrame(stateTimer,true);
                break;
            case JUMPING:
            case FALLING:
            case DEAD:
            case IDLE:
            default:
                region = idle.getKeyFrame(stateTimer, true);
        }

        if((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true,false);
            runningRight = false;
        }
        else if((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(false,true);
            runningRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer + delta : 0;
        previousState = currentState;
        return region;
    }
}
